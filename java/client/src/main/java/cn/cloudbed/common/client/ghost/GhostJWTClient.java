package cn.cloudbed.common.client.ghost;

import cn.cloudbed.common.util.DataConverter;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GhostJWTClient {

    /**
     * admin api key
     */
    public static final String adminKey = "5d6faca530aefc26ab53824f:586104ffdb778d9ef7f3c793fdc8dfd793dfe4b2a21409382dfeb272265d294f";
    public static final String hostname = "http://139.196.142.70";
    //public static final String hostname = "http://localhost";
    /**
     * 生成token
     * @return
     */
    public static String generateToken(String adminKey)  {
        //1. 将admin api key分成两部分：id 和 secret;
        String[] arr = adminKey.split(":");
        String id = arr[0];
        String secret = arr[1];
        //2. 将16进制的secret，解码成2进制的字节数组
        byte[] secretArr = DataConverter.hexToByte(secret);

        //3. 将id和secret放入jwt库对象中，并存入相应的header和payload
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("kid", id);
        header.put("typ", "JWT");
        Map<String, Object> payload = new HashMap<>();
        Long now = new Date().getTime() / 1000;
        payload.put("iat", now);
        payload.put("exp", now + 30 * 24 * 60 * 60);
        payload.put("aud", "/v2/admin/");
        String token = Jwts.builder()
                .addClaims(payload)
                .setHeader(header)
                .signWith(SignatureAlgorithm.HS256, secretArr)
                .compact();
        return token;
    }

    /**
     * 构建feign服务对象
     * */
    private static GhostJWTService buildeGhostClientService(String host, String token) {

        //在所有的请求中，加入ghost admin token的header
        RequestInterceptor headerInterceptor = requestTemplate -> {
            //在header中放入ghost admin认证使用的token
            requestTemplate.header("Authorization", "Ghost "+token);
        };
        GhostJWTService service = Feign.builder()
                .logLevel(Logger.Level.FULL)
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .requestInterceptor(headerInterceptor) //添加ghost admin token的header
                .decoder(new JacksonDecoder()) //设置解码器，来解码json
                .encoder(new JacksonEncoder()) //设置编码器，来将对象转换为json
                .target(GhostJWTService.class, host); //设置feign接口，以及目标主机

        return service;//返回接口对象
    }

    /**
     * 获取feign服务对象
     */
    private static GhostJWTService ghostJWTService =null;
    public static GhostJWTService getGhostJWTService(){
        return getGhostJWTService(false);

    }

    public static GhostJWTService getGhostJWTService(boolean refreshToken){

        if(ghostJWTService ==null || refreshToken ){
            String hostname = GhostJWTClient.hostname;
            String token = GhostJWTClient.generateToken(GhostJWTClient.adminKey);
            //String token="eyJraWQiOiI1ZDZmYWNhNTMwYWVmYzI2YWI1MzgyNGYiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIvdjIvYWRtaW4vIiwiZXhwIjoxNTgwMzMxOTExLCJpYXQiOjE1ODAzMTM5MTF9.ncGDN8pNdKmswyYQShYacOLSeevYnnib9qpEFtk1YDA";
            System.out.println(token);
            ghostJWTService = GhostJWTClient.buildeGhostClientService(hostname, token);
        }
        return ghostJWTService;
    }

}

