package cn.cloudbed.common.client.baidu;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface BaiduOAuthService {


    @Headers({"Content-type:application/json","Accept:application/json"})
    @RequestLine("GET /oauth/2.0/authorize?redirect_uri={redirect_uri}&client_id={client_id}&response_type={response_type}")
    Object authorize(@Param("client_id") String appKey,@Param("redirect_uri") String redirectUri,@Param("response_type") String responseType);


}
