package cn.cloudbed.mylibrary.api.auth;

import cn.cloudbed.mylibrary.framework.domain.ucenter.request.LoginRequest;
import cn.cloudbed.mylibrary.framework.domain.ucenter.response.JwtResult;
import cn.cloudbed.mylibrary.framework.domain.ucenter.response.LoginResult;
import cn.cloudbed.mylibrary.framework.model.response.ResponseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Administrator.
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthControllerApi {
    @ApiOperation("登录")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("退出")
    public ResponseResult logout();

    @ApiOperation("查询用户jwt令牌")
    public JwtResult userjwt();
}
