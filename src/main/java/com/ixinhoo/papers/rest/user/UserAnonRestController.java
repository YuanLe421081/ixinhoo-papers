package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.user.UserDto;
import com.ixinhoo.papers.dto.user.UserLoginDto;
import com.ixinhoo.papers.dto.user.UserRegisterDto;
import com.ixinhoo.papers.dto.user.UserResetPasswordDto;
import com.ixinhoo.papers.service.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息--不鉴权
 */
@RestController
@RequestMapping(value = "/api/anon/user")
public class UserAnonRestController {

    @Autowired
    private UserService service;

    /**
     * 验证码
     * ；	      	type--1注册用户、2更换手机号、3验证更换手机号、4忘记密码、5--验证用户手机号
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "verify-code", method = RequestMethod.POST)
    public StatusDto verifyCode(@RequestParam("phone") String phone, @RequestParam("type") Integer type) {
        return service.verifyCodeByPhone(phone, type);
    }

    /**
     * 重置密码-验证码-校验
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "reset-code", method = RequestMethod.POST)
    public StatusDto verifyPwdCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return service.verifyResetCodeByPhone(phone, code, false);
    }

    /**
     * 注册
     *
     * @param reqDto
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public DetailDto<UserDto> register(UserRegisterDto reqDto) {
        return service.registerUser(reqDto);
    }


    /**
     * 授权登录
     *
     * @param reqDto
     * @return
     */
    @RequestMapping(value = "authority-login", method = RequestMethod.POST)
    public DetailDto<UserDto> authorityLogin(UserRegisterDto reqDto) {
        return service.authorityLogin(reqDto);
    }


    /**
     * 登陆
     *
     * @param reqDto
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public DetailDto<UserDto> login(UserLoginDto reqDto) {
        return service.loginUser(reqDto);
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public StatusDto logout() {
        Subject s = SecurityUtils.getSubject();
        if (s != null) {
            s.logout();
        }
        return new StatusDto(true);
    }


    /**
     * 重置密码
     *
     * @param reqDto
     * @return
     */
    @RequestMapping(value = "reset-password", method = RequestMethod.POST)
    public StatusDto resetPassword(UserResetPasswordDto reqDto) {
        return service.resetUserPassword(reqDto);
    }


}