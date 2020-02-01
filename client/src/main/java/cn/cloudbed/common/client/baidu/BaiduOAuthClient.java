package cn.cloudbed.common.client.baidu;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * 通过clientid和secretid获取到授权码后，再换取token及refreshtoken
 */
public class BaiduOAuthClient {

    static final String host="https://openapi.baidu.com";
    static final String defaultApiKey="wG5cNEsdwp6KIAZHZuK26nMN";
    static final String defaultSecretKey="HNtGUzlcDXn2fFDVyjUj0kyP7ceqXXBu";
    static final String defaultRedirectUri="http://139.196.142.70/mylibrary";
    static  BaiduOAuthService baiduOAuthService;

    public static String authorize(){
        return authorize(defaultApiKey,defaultRedirectUri)  ;
    }
    public static String authorize(String apiKey,String redirectUri){

        BaiduOAuthService baiduOAuthService=getBaiduOauthService();
        return (String)baiduOAuthService.authorize(apiKey,redirectUri,"code");
    }

    private static BaiduOAuthService buildBaiduOauthService(){
        RequestInterceptor headerInterceptor= template -> {};
        JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                .withMarshallerSchemaLocation("http://apihost http://apihost/schema.xsd")
                .build();
         BaiduOAuthService service= Feign.builder()
                 .options(new Request.Options(1000, 3500))
                 .retryer(new Retryer.Default(5000,5000,3))
                 .logger(new Logger.JavaLogger())
                 .logLevel(Logger.Level.FULL)
                 .requestInterceptor(headerInterceptor)
                 .mapAndDecode(((response, type) -> {
                     try {
                         Reader reader = response.body().asReader();
                         StringBuilder builder = new StringBuilder();
                         char[] cbuf = new char[50];
                         int len=0;
                         while((len=reader.read(cbuf))!=-1){
                             builder.append(cbuf);
                         }
                         String body=builder.toString();
                         System.out.println(body);
                     }catch (IOException e){
                         e.printStackTrace();
                     }
                     return response;
                 }),new JacksonDecoder())
                 //.decoder(new JacksonDecoder())
                 //.decoder(new JAXBDecoder(jaxbFactory))
                 .encoder(new JacksonEncoder())
                 //.encoder(new JAXBEncoder(jaxbFactory))
                 .target(BaiduOAuthService.class,host);
        return service;
    }

    public static BaiduOAuthService getBaiduOauthService(){
        if(baiduOAuthService==null){
            baiduOAuthService=buildBaiduOauthService();
        }
        return baiduOAuthService;
    }

    public static void main(String[] args){
        String resp= authorize();
        System.out.println(resp);
        return;
    }
}
