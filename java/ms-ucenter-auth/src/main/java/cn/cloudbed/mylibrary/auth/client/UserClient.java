package cn.cloudbed.mylibrary.auth.client;


import cn.cloudbed.mylibrary.framework.domain.ucenter.ext.XcUserExt;
import cn.cloudbed.mylibrary.framework.client.MSList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Administrator.
 */
@FeignClient(value = MSList.MS_UCENTER)
public interface UserClient {
    //根据账号查询用户信息
    @GetMapping("/ucenter/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username);
}
