package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.common.MessageNoticeDto;
import com.ixinhoo.papers.dto.user.UserDto;
import com.ixinhoo.papers.dto.user.UserUpdatePasswordDto;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息--
 */
@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestController {

    @Autowired
    private UserService service;


    /**
     * 用户信息--个人中心
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public DetailDto<UserDto> userInfo(Long userId) {
        return service.findUserInfoById(userId);
    }

    /**
     * 用户信息--个人中心--修改密码
     *
     * @return
     */
    @RequestMapping(value = "update-pwd", method = RequestMethod.POST)
    public StatusDto updatePassword(UserUpdatePasswordDto reqDto) {
        return service.updateUserPassword(reqDto);
    }

    /**
     * 用户信息--个人中心--更新用户信息，传输哪个更新哪个
     *
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public StatusDto updateUser(User user) {
        return service.updateUser(user);
    }

    /**
     * 用户信息--个人中心--更新用户手机号
     *
     * @return
     */
    @RequestMapping(value = "update-phone", method = RequestMethod.POST)
    public StatusDto updateUserPhone(Long userId,String phone,String code) {
        return service.updateUserPhone(userId,phone,code);
    }

    /**
     * 用户信息--绑定第三方账号；TODO 废弃，
     *
     * @return
     */
    @RequestMapping(value = "update-third-account", method = RequestMethod.POST)
    public StatusDto updateThirdAccount(Long userId,String qq,String weibo,String wechat) {
        return service.updateThirdAccount(userId,qq,weibo,wechat);
    }


    /**
     * 用户信息--个人中心--领取备课币
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "receive-coin", method = RequestMethod.POST)
    public StatusDto receiveCoin(Long userId) {
        return service.receiveCoinByUserId(userId);
    }


    /**
     * 用户信息--个人中心--用户消息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "notice", method = RequestMethod.POST)
    public ListDto<MessageNoticeDto> notice(Long userId) {
        return service.findNoticeByUserId(userId);
    }

    /**
     * 用户信息--个人中心--用户消息--全部标记或单个为已读;
     * messageId 不传输表示所有标识为已读，
     * 传输就是单个标识为已读
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "notice-read", method = RequestMethod.POST)
    public StatusDto noticeRead(Long userId, Long messageId) {
        return service.updateNoticeByUserId(userId, messageId);
    }


}