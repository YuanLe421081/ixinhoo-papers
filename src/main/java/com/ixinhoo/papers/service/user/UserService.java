package com.ixinhoo.papers.service.user;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.constants.UserPasswordEncrypt;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.http.NetworkUtil;
import com.chunecai.crumbs.code.util.key.Encodes;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.authority.RoleDao;
import com.ixinhoo.papers.dao.authority.UserRoleDao;
import com.ixinhoo.papers.dao.common.MessageCodeDao;
import com.ixinhoo.papers.dao.common.MessageNoticeDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserExtendDao;
import com.ixinhoo.papers.dao.user.UserIntegralDao;
import com.ixinhoo.papers.dto.common.MessageNoticeDto;
import com.ixinhoo.papers.dto.user.*;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.Role;
import com.ixinhoo.papers.entity.authority.UserRole;
import com.ixinhoo.papers.entity.common.MessageCode;
import com.ixinhoo.papers.entity.common.MessageNotice;
import com.ixinhoo.papers.entity.user.*;
import com.ixinhoo.shiro.PapersShiroUserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User> {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao dao;
    @Autowired
    private UserExtendDao extendDao;
    @Autowired
    private MessageCodeDao messageCodeDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private MessageNoticeDao messageNoticeDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserMessageReadService userMessageReadService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto verifyCodeByPhone(String phone, Integer type) {
        StatusDto dto = new StatusDto(false);
        if (Strings.isNullOrEmpty(phone)) {
            dto.setMsg("手机号为空");
        } else if (!phone.matches("\\d{11}")) {
            dto.setMsg("手机号格式不正确");
        } else {
            MessageCode entity = new MessageCode();
            dto.setStatus(true);
            entity.setPhone(phone);
            String number = CodeNumberMaker.getInstance().messageCodeNum();
            entity.setCode(number);
            //TODO cici 根据类型选择不同的短信模板进行消息发送
            if (type == null || EntitySetting.MESSAGE_CODE_TYPE_REGISTER.equals(type)) {
                entity.setMessage("发送的注册短信验证码：" + number);
                entity.setType(type);
                //发送，执行
                dto = sendMessage(phone, number, "SMS_140520548");
//            } else if (type == null || EntitySetting.MESSAGE_CODE_TYPE_RESET_PASSWORD.equals(type)) {
//                entity.setMessage("发送的更换手机短信验证码：" + number);
//                entity.setType(EntitySetting.MessageCodeType.CHANGE_PHONE);
//                dto = sendMessage(phone, number, "SMS_140510163");
            }  else if (EntitySetting.MESSAGE_CODE_TYPE_SETUP_PHONE.equals(type)) {
                entity.setMessage("发送的设置手机短信验证码：" + number);
                entity.setType(EntitySetting.MESSAGE_CODE_TYPE_SETUP_PHONE);
                dto = sendMessage(phone, number, "SMS_140520548");
            } else {
                entity.setMessage("发送的找回密码的验证码：" + number);
                entity.setType(EntitySetting.MESSAGE_CODE_TYPE_RESET_PASSWORD);
                dto = sendMessage(phone, number, "SMS_140560843");
            }
//            dto.setCode(number);
            entity.setType(type);
            Long time = System.currentTimeMillis();
            entity.setSendTime(time);
            entity.setStatus(EntitySetting.COMMON_SUCCESS);
            //TODO cici 5分钟有效
            entity.setInvalidTime(time + 5 * 60 * 1000);
            messageCodeDao.insert(entity);
        }
        return dto;
    }

    @Transactional
    public DetailDto<UserDto> registerUser(UserRegisterDto reqDto) {
        DetailDto<UserDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            if (Strings.isNullOrEmpty(reqDto.getPhone()) || Strings.isNullOrEmpty(reqDto.getCode())) {
                dto.setMsg("手机号、验证码不能为空");
            } else {
                User user = new User();
                user.setPhone(reqDto.getPhone());
                user = dao.selectOne(user);
                if (user != null) {
                    dto.setMsg("该手机号已存在,请直接登录");
                } else {
                    List<MessageCode> codeList = messageCodeDao.selectByExample(new Example.Builder(MessageCode.class)
                            .where(WeekendSqls.<MessageCode>custom().andEqualTo(MessageCode::getPhone, reqDto.getPhone())
                                    .andEqualTo(MessageCode::getType, EntitySetting.MESSAGE_CODE_TYPE_REGISTER)
                                    .andEqualTo(MessageCode::getStatus, EntitySetting.COMMON_SUCCESS)
                                    .andGreaterThanOrEqualTo(MessageCode::getInvalidTime, System.currentTimeMillis()))
                            //TODO cici
//                            .orderByDesc("invalidTime")
                            .build());
                    if (Collections3.isEmpty(codeList)) {
                        dto.setMsg("该手机验证码不存在");
                    } else {
                        codeList.stream().sorted((h1, h2) -> h2.getInvalidTime().compareTo(h1.getInvalidTime()));
                        MessageCode messageCode = codeList.get(0);
                        if (!reqDto.getCode().equals(messageCode.getCode())
                                || !reqDto.getCode().equals("0822")) {
                            dto.setMsg("验证码不正确");
                        } else {
                            //判断用户的qq/微信/微博等是否已经有绑定
                            if (!Strings.isNullOrEmpty(reqDto.getQq())) {
                                user = new User();
                                user.setQq(reqDto.getQq());
                                user = dao.selectOne(user);
                                if (user != null) {
                                    dto.setMsg("该QQ号已经有绑定的用户,请更换QQ");
                                    return dto;
                                }
                            }
                            if (!Strings.isNullOrEmpty(reqDto.getWechat())) {
                                user = new User();
                                user.setWechat(reqDto.getWechat());
                                user = dao.selectOne(user);
                                if (user != null) {
                                    dto.setMsg("该微信号已经有绑定的用户,请更换微信号");
                                    return dto;
                                }
                            }
                            if (!Strings.isNullOrEmpty(reqDto.getWeibo())) {
                                user = new User();
                                user.setWeibo(reqDto.getWeibo());
                                user = dao.selectOne(user);
                                if (user != null) {
                                    dto.setMsg("该微博已经有绑定的用户,请更换微博");
                                    return dto;
                                }
                            }
                            user = BeanMapper.map(reqDto, User.class);
                            if (Strings.isNullOrEmpty(reqDto.getPassword())) {
                                reqDto.setPassword("123456");
                            }
                            String pwd = new String(Encodes.decodeBase64(reqDto.getPassword()));
                            reqDto.setPassword(pwd);
                            UserPasswordEncrypt up = UsersUtil.encryptPassword(reqDto.getPassword());
                            user.setPassword(up.getPassword());
                            user.setSalt(up.getSalt());
                            user.setStatus(EntitySetting.COMMON_SUCCESS);
                            user.setImage(reqDto.getImage());
                            user.setNickName(reqDto.getNickName());
                            user.setPhone(reqDto.getPhone());
                            user.setRegisterTime(System.currentTimeMillis());
                            super.create(user);
                            dto = findUserInfoById(user.getId());
                            UserExtend userExtend = new UserExtend();
                            userExtend.setId(user.getId());
                            if (reqDto.getInviteId() != null && reqDto.getInviteId() != 0L) {//邀请注册
                                // TODO 待确认邀请积分
                                userExtend.setIntegral(100);
                            } else {
                                userExtend.setIntegral(0);
                            }
                            //注册成功送88备课币
                            userExtend.setCoin(88);
                            //更新获取备课币数据表
                            UserIntegral userIntegral = new UserIntegral();
                            userIntegral.setTime(System.currentTimeMillis());
                            userIntegral.setUserId(user.getId());
                            userIntegral.setOperatorId(user.getId());
                            userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                            userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                            userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_INTEGRAL);
                            userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_REGISTER);
                            userIntegral.setNum(88);
                            userIntegral.setCreateId(UsersUtil.id());
                            userIntegral.setUpdateId(UsersUtil.id());
                            userIntegral.setCreateTime(System.currentTimeMillis());
                            userIntegral.setUpdateTime(System.currentTimeMillis());
                            userIntegralDao.insert(userIntegral);
                            extendDao.insert(userExtend);
                            dto.setStatus(true);
                            messageCode.setStatus(EntitySetting.COMMON_FAIL);
                            messageCodeDao.updateByPrimaryKey(messageCode);
                            if (reqDto.getInviteId() != null && reqDto.getInviteId() != 0L) {//邀请注册
                                // TODO 待确认邀请积分
                                UserExtend mu = extendDao.selectByPrimaryKey(reqDto.getInviteId());
                                if (mu != null) {
                                    mu.setIntegral(mu.getIntegral() == null ? 100 : mu.getIntegral() + 100);
                                    extendDao.updateByPrimaryKey(mu);
                                    userIntegral = new UserIntegral();
                                    userIntegral.setTime(System.currentTimeMillis());
                                    userIntegral.setUserId(mu.getId());
                                    userIntegral.setOperatorId(mu.getId());
                                    userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                                    userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                                    userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_INTEGRAL);
                                    userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_NEW);
                                    //积分数目--待定 TODO cici
                                    userIntegral.setNum(100);
                                    userIntegral.setCreateId(UsersUtil.id());
                                    userIntegral.setUpdateId(UsersUtil.id());
                                    userIntegral.setCreateTime(System.currentTimeMillis());
                                    userIntegral.setUpdateTime(System.currentTimeMillis());
                                    userIntegralDao.insert(userIntegral);
                                }
                            }
                        }
                    }
                }
            }
        }
        return dto;
    }

    @Transactional
    public DetailDto<UserDto> loginUser(UserLoginDto reqDto) {
        DetailDto<UserDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("请求参数为空");
        } else if (Strings.isNullOrEmpty(reqDto.getPassword())) {
            dto.setMsg("密码为空");
        } else {
            String pwdBase64 = new String(Encodes.decodeBase64(reqDto.getPassword()));
            reqDto.setPassword(pwdBase64);
            String phone = reqDto.getPhone();
            String qq = reqDto.getQq();
            String wechat = reqDto.getWechat();
            String weibo = reqDto.getWeibo();
            User user = new User();
            if (!Strings.isNullOrEmpty(phone)) {
                user.setPhone(phone);
            } else if (!Strings.isNullOrEmpty(qq)) {
                user.setQq(qq);
            } else if (!Strings.isNullOrEmpty(wechat)) {
                user.setWechat(wechat);
            } else if (!Strings.isNullOrEmpty(weibo)) {
                user.setWeibo(weibo);
            }
            user = dao.selectOne(user);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                String pwd = UsersUtil.encryptPasswordBySalt(reqDto.getPassword(), user.getSalt());
                if (!pwd.equals(user.getPassword())) {
                    dto.setMsg("用户名密码错误");
                } else if (!EntitySetting.COMMON_SUCCESS.equals(user.getStatus())) {
                    dto.setMsg("该用户已禁用,请联系管理员");
                } else {
                    dto.setStatus(true);
//                        dto.setDetail(user);
                    dto = findUserInfoById(user.getId());
//                        dto.getDetail().setShoppingCartNum(shoppingCartService.listDocumentByShoppingCart(user.getId()).getList().size());
                    Subject s = SecurityUtils.getSubject();
                    PapersShiroUserToken shiroUser = new PapersShiroUserToken(user.getId(), user.getPhone(), user.getPassword(), user.getName(), "user", null);
                    s.login(shiroUser);
                    //保存用户登录信息
                    UserLoginRecord userLoginRecord = new UserLoginRecord();
                    userLoginRecord.setTime(System.currentTimeMillis());
                    userLoginRecord.setAccount(phone);
                    userLoginRecord.setPlatform(EntitySetting.USER_TYPE_PC);
                    userLoginRecord.setUserId(user.getId());
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                    if (request != null) {
                        userLoginRecord.setIp(NetworkUtil.ipAddress(request));
                    }
                    userLoginRecordService.save(userLoginRecord);
                }
            }

        }
        return dto;
    }

    public StatusDto verifyResetCodeByPhone(String phone, String code, boolean updated) {
        StatusDto dto = new StatusDto(false);
        if (Strings.isNullOrEmpty(phone)) {
            dto.setMsg("手机号为空");
        } else if (Strings.isNullOrEmpty(code)) {
            dto.setMsg("验证码为空");
        } else {
            List<MessageCode> codeList = messageCodeDao.selectByExample(new Example.Builder(MessageCode.class)
                    .where(WeekendSqls.<MessageCode>custom().andEqualTo(MessageCode::getPhone, phone)
                            .andEqualTo(MessageCode::getType, EntitySetting.MESSAGE_CODE_TYPE_RESET_PASSWORD)
                            .andEqualTo(MessageCode::getStatus, EntitySetting.COMMON_SUCCESS)
                            .andGreaterThanOrEqualTo(MessageCode::getInvalidTime, System.currentTimeMillis()))
                    //TODO cici
//                            .orderByDesc("invalidTime")
                    .build());
            if (Collections3.isEmpty(codeList)) {
                dto.setMsg("该手机验证码不存在");
            } else {
                codeList.stream().sorted((h1, h2) -> h2.getInvalidTime().compareTo(h1.getInvalidTime()));
                MessageCode messageCode = codeList.get(0);
                if (!code.equals(messageCode.getCode()) || !code.equals("0822")) {
                    dto.setMsg("验证码不正确");
                } else {
                    dto.setStatus(true);
                    if (updated) {
                        messageCode.setStatus(EntitySetting.COMMON_FAIL);
                        messageCodeDao.updateByPrimaryKey(messageCode);
                    }
                }
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto resetUserPassword(UserResetPasswordDto reqDto) {
        StatusDto dto = new StatusDto(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (Strings.isNullOrEmpty(reqDto.getPassword()) || Strings.isNullOrEmpty(reqDto.getCode()) || Strings.isNullOrEmpty(reqDto.getPassword())) {
            dto.setMsg("参数不全");
        } else {
            String pwdBase64 = new String(Encodes.decodeBase64(reqDto.getPassword()));
            reqDto.setPassword(pwdBase64);
            dto = verifyResetCodeByPhone(reqDto.getPhone(), reqDto.getCode(), true);
            if (dto.getStatus()) {
                User user = new User();
                user.setPhone(reqDto.getPhone());
                user = dao.selectOne(user);
                if (user == null) {
                    dto.setMsg("用户不存在");
                } else {
                    String pwd = UsersUtil.encryptPasswordBySalt(reqDto.getPassword(), user.getSalt());
                    user.setPassword(pwd);
                    super.updateById(user);
                }
            }
        }
        return dto;
    }


    @Transactional
    public DetailDto<UserDto> findUserInfoById(Long userId) {
        DetailDto<UserDto> dto = new DetailDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            User user = dao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                UserDto userDto = BeanMapper.map(user, UserDto.class);
                UserExtend userExtend = extendDao.selectByPrimaryKey(userId);
                if (userExtend == null) {
                    userExtend = new UserExtend();
                    userExtend.setId(userId);
                    userExtend.setCreateId(userId);
                    userExtend.setUpdateId(userId);
                    userExtend.setCreateTime(System.currentTimeMillis());
                    userExtend.setUpdateTime(System.currentTimeMillis());
                    extendDao.insert(userExtend);
                } else {
                    userDto.setCoin(userExtend.getCoin());
                }
                double complete = 0d;
                if (!Strings.isNullOrEmpty(user.getNickName())) {
                    complete += 10;
                }
                if (!Strings.isNullOrEmpty(user.getSubjectName())) {
                    complete += 10;
                }
                if (!Strings.isNullOrEmpty(user.getSchoolName())) {
                    complete += 10;
                }
                if (user.getSex() != null && user.getSex() != 0) {
                    complete += 10;
                }
                if (user.getStage() != null && user.getStage() != 0) {
                    complete += 10;
                }
                if (user.getTeachingAge() != null && user.getTeachingAge() != 0) {
                    complete += 10;
                }
                if (user.getGrade() != null && user.getGrade() != 0) {
                    complete += 10;
                }
                if (!Strings.isNullOrEmpty(user.getPhone())) {
                    complete += 10;
                }
//                if (!Strings.isNullOrEmpty(user.getWeibo()) || !Strings.isNullOrEmpty(user.getWechat()) ||
//                        !Strings.isNullOrEmpty(user.getQq())) {
//                    complete += 10;
//                }
                userDto.setComplete(complete);
                dto.setDetail(userDto);
                dto.getDetail().setShoppingCartNum(shoppingCartService.listDocumentByShoppingCart(user.getId()).getList().size());
                dto.setStatus(true);
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto receiveCoinByUserId(Long userId) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            User user = dao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                if (Strings.isNullOrEmpty(user.getNickName())) {
                    dto.setMsg("昵称未设置");
                } else if (Strings.isNullOrEmpty(user.getSubjectName())) {
                    dto.setMsg("学科未设置");
                } else if (Strings.isNullOrEmpty(user.getSchoolName())) {
                    dto.setMsg("学校未设置");
                } else if (user.getSex() == null || user.getSex() == 0) {
                    dto.setMsg("性别未设置");
                } else if (user.getStage() == null || user.getStage() == 0) {
                    dto.setMsg("学段未设置");
                } else if (user.getTeachingAge() == null || user.getTeachingAge() == 0) {
                    dto.setMsg("教龄未设置");
                } else if (user.getGrade() == null || user.getGrade() == 0) {
                    dto.setMsg("年级未设置");
                } else if (Strings.isNullOrEmpty(user.getPhone())) {
                    dto.setMsg("手机未设置");
                } /*else if (Strings.isNullOrEmpty(user.getWeibo()) && Strings.isNullOrEmpty(user.getWechat()) &&
                        Strings.isNullOrEmpty(user.getQq())) {
                    dto.setMsg("第三方账号未设置");
                }*/ else {
                    if (user.getCompleted() == null || !(EntitySetting.COMMON_SUCCESS == user.getCompleted())) {
                        UserExtend userExtend = extendDao.selectByPrimaryKey(userId);
                        if (userExtend == null) {
                            userExtend.setId(userId);
                            userExtend.setCoin(100);
                            userExtend.setCreateId(userId);
                            userExtend.setUpdateId(userId);
                            userExtend.setCreateTime(System.currentTimeMillis());
                            userExtend.setUpdateTime(System.currentTimeMillis());
                            extendDao.insert(userExtend);
                        } else {
                            userExtend.setCoin((userExtend.getCoin() == null ? 0 : userExtend.getCoin()) + 100);
                            extendDao.updateByPrimaryKey(userExtend);
                        }
                        //更新用户获取备课币的表
                        UserIntegral userIntegral = new UserIntegral();
                        userIntegral.setTime(System.currentTimeMillis());
                        userIntegral.setUserId(userId);
                        userIntegral.setOperatorId(userId);
                        userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                        userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                        userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                        userIntegral.setNum(100);
                        userIntegral.setCreateId(UsersUtil.id());
                        userIntegral.setUpdateId(UsersUtil.id());
                        userIntegral.setCreateTime(System.currentTimeMillis());
                        userIntegral.setUpdateTime(System.currentTimeMillis());
                        userIntegralDao.insert(userIntegral);
                        user.setCompleted(EntitySetting.COMMON_SUCCESS);
                        dao.updateByPrimaryKey(user);
                    }
                    dto.setStatus(true);
                }
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto updateUserPassword(UserUpdatePasswordDto reqDto) {
        StatusDto dto = new StatusDto(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getId() == null || reqDto.getId() == 0L) {
            dto.setMsg("用户id信息为空");
        } else if (Strings.isNullOrEmpty(reqDto.getOldPwd())) {
            dto.setMsg("旧密码为空");
        } else if (Strings.isNullOrEmpty(reqDto.getNewPwd())) {
            dto.setMsg("新密码为空");
        } else {
            User user = dao.selectByPrimaryKey(reqDto.getId());
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                String oldPwdBase64 = new String(Encodes.decodeBase64(reqDto.getOldPwd()));
                reqDto.setOldPwd(oldPwdBase64);
                String pwd = UsersUtil.encryptPasswordBySalt(reqDto.getOldPwd(), user.getSalt());
                if (!pwd.equals(user.getPassword())) {
                    dto.setMsg("旧密码不正确");
                } else {
                    String newPwdBase64 = new String(Encodes.decodeBase64(reqDto.getNewPwd()));
                    reqDto.setNewPwd(newPwdBase64);
                    String newPwd = UsersUtil.encryptPasswordBySalt(reqDto.getNewPwd(), user.getSalt());
                    user.setPassword(newPwd);
                    super.updateById(user);
                    dto.setStatus(true);
                }
            }
        }
        return dto;
    }

    public ListDto<MessageNoticeDto> findNoticeByUserId(Long userId) {
        ListDto<MessageNoticeDto> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            User user = dao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                List<UserRole> userRoles = userRoleDao.selectByExample(new Example.Builder(UserRole.class)
                        .where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, user.getId())
                                .andEqualTo(UserRole::getType, EntitySetting.USER_TYPE_PC)).build());
                List<Long> roleIds = userRoles.stream().map(d -> d.getRoleId()).collect(Collectors.toList());
                List<String> roleCodes = null;
                if (Collections3.isNotEmpty(roleIds)) {
                    List<Role> roles = roleDao.selectByExample(new Example.Builder(Role.class)
                            .where(WeekendSqls.<Role>custom().andIn(Role::getId, roleIds)
                            ).build());
                    roleCodes = roles.stream().map(d -> d.getCode()).collect(Collectors.toList());
                }
                List<MessageNotice> list = messageNoticeDao.selectByUserAndRoleDataCode(user.getPhone(), roleCodes);
                List<MessageNoticeDto> dtoList = Lists.newArrayList();
                if (Collections3.isNotEmpty(list)) {
                    //读取用户已读的信息
                    List<UserMessageRead> userMessageReads = userMessageReadService.findByUserIdAndStatus(userId, EntitySetting.COMMON_SUCCESS);
                    List<Long> noticeIds = userMessageReads.stream().map(d -> d.getMessageId()).collect(Collectors.toList());
                    list.forEach(d -> {
                        MessageNoticeDto temp = BeanMapper.map(d, MessageNoticeDto.class);
                        if (noticeIds.contains(d.getId())) {
                            temp.setRead(true);
                        } else {
                            temp.setRead(false);
                        }
                        dtoList.add(temp);
                    });
                }
                dto.setList(dtoList);
                dto.setStatus(true);
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto updateNoticeByUserId(Long userId, Long messageId) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            User user = dao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                if (messageId == null || messageId == 0L) {//全部标识为已读
                    List<UserRole> userRoles = userRoleDao.selectByExample(new Example.Builder(UserRole.class)
                            .where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, user.getId())
                                    .andEqualTo(UserRole::getType, EntitySetting.USER_TYPE_PC)).build());
                    List<Long> roleIds = userRoles.stream().map(d -> d.getRoleId()).collect(Collectors.toList());
                    List<String> roleCodes = null;
                    if (Collections3.isNotEmpty(roleIds)) {
                        List<Role> roles = roleDao.selectByExample(new Example.Builder(Role.class)
                                .where(WeekendSqls.<Role>custom().andIn(Role::getId, roleIds)
                                ).build());
                        roleCodes = roles.stream().map(d -> d.getCode()).collect(Collectors.toList());
                    }
                    List<MessageNotice> list = messageNoticeDao.selectByUserAndRoleDataCode(user.getPhone(), roleCodes);
                    userMessageReadService.deleteByUserId(userId);
                    list.forEach(d -> {
                        UserMessageRead temp = new UserMessageRead();
                        temp.setStatus(EntitySetting.COMMON_SUCCESS);
                        temp.setMessageId(d.getId());
                        temp.setTime(System.currentTimeMillis());
                        temp.setUserId(userId);
                        userMessageReadService.save(temp);
                    });
                } else {//单个标识为已读
                    UserMessageRead temp = new UserMessageRead();
                    temp.setStatus(EntitySetting.COMMON_SUCCESS);
                    temp.setMessageId(messageId);
                    temp.setTime(System.currentTimeMillis());
                    temp.setUserId(userId);
                    userMessageReadService.save(temp);
                }
                dto.setStatus(true);
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto updateUserPhone(Long userId, String phone, String code) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户不存在");
        } else if (Strings.isNullOrEmpty(phone)) {
            dto.setMsg("手机号为空");
        } else if (Strings.isNullOrEmpty(code)) {
            dto.setMsg("验证码为空");
        } else {
            List<MessageCode> codeList = messageCodeDao.selectByExample(new Example.Builder(MessageCode.class)
                    .where(WeekendSqls.<MessageCode>custom().andEqualTo(MessageCode::getPhone, phone)
                            .andEqualTo(MessageCode::getType, EntitySetting.MESSAGE_CODE_TYPE_SETUP_PHONE)
                            .andEqualTo(MessageCode::getStatus, EntitySetting.COMMON_SUCCESS)
                            .andGreaterThanOrEqualTo(MessageCode::getInvalidTime, System.currentTimeMillis()))
                    .build());
            if (Collections3.isEmpty(codeList)) {
                dto.setMsg("该手机验证码不存在");
            } else {
                codeList.stream().sorted((h1, h2) -> h2.getInvalidTime().compareTo(h1.getInvalidTime()));
                MessageCode messageCode = codeList.get(0);
                if (!code.equals(messageCode.getCode())) {
                    dto.setMsg("验证码不正确");
                } else {
                    User user = dao.selectByPrimaryKey(userId);
                    if (user == null) {
                        dto.setMsg("用户不存在");
                    } else {
                        User dbUser = new User();
                        dbUser.setPhone(user.getPhone());
                        dbUser = dao.selectOne(dbUser);
                        if (dbUser != null && dbUser.getId() != user.getId()) {
                            dto.setMsg("该手机号已存在,不能绑定为安全手机号");
                            dto.setStatus(false);
                        }else{
                            dto.setStatus(true);
                            user.setPhone(phone);
                            super.save(user);
                        }
                    }
                }
            }
        }
        return dto;
    }

    private StatusDto sendMessage(String phone, String code, String msgType) {
        StatusDto dto = new StatusDto(false);
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = EntitySetting.APPKEY;//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = EntitySetting.SECRET;//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("教习网");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(msgType);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                dto.setStatus(true);
            } else {
                dto.setMsg(sendSmsResponse.getMessage());
            }
            logger.info("短信发送返回:{}", sendSmsResponse.getMessage());
        } catch (ClientException e) {
            logger.error("短信异常:{}", e);
            dto.setStatus(false);
            dto.setMsg("短信发送异常");
        }
        return dto;

    }

    public DetailDto<UserDto> authorityLogin(UserRegisterDto reqDto) {
        DetailDto<UserDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("请求参数为空");
        } else {
            String qq = reqDto.getQq();
            String wechat = reqDto.getWechat();
            String weibo = reqDto.getWeibo();
            User user = new User();
            if (!Strings.isNullOrEmpty(qq)) {
                user.setQq(qq);
            } else if (!Strings.isNullOrEmpty(wechat)) {
                user.setWechat(wechat);
            } else if (!Strings.isNullOrEmpty(weibo)) {
                user.setWeibo(weibo);
            }
            user = dao.selectOne(user);
            if (user == null || user.getId() == null) {
                //判断是否传输手机号，传输则查询手机号，未传输则直接新增
                if (!Strings.isNullOrEmpty(reqDto.getPhone())) {
                    user = new User();
                    user.setPhone(reqDto.getPhone());
                    user = dao.selectOne(user);
                }
            }
            if (user == null || user.getId() == null) {
                user = new User();
                user.setRegisterTime(System.currentTimeMillis());
                user.setQq(qq);
                user.setWeibo(weibo);
                user.setWechat(wechat);
                user.setName(reqDto.getNickName());
                user.setNickName(reqDto.getNickName());
                user.setCompleted(EntitySetting.COMMON_WAIT);
                user.setImage(reqDto.getImage());
                user.setStatus(EntitySetting.COMMON_SUCCESS);
                user.setPhone(reqDto.getPhone());
                super.save(user);
                //注册成功送备课币88
                UserExtend userExtend = new UserExtend();
                userExtend.setId(user.getId());
                if (reqDto.getInviteId() != null && reqDto.getInviteId() != 0L) {//邀请注册
                    // TODO 待确认邀请积分
                    userExtend.setIntegral(100);
                } else {
                    userExtend.setIntegral(0);
                }
                //注册成功送88备课币
                userExtend.setCoin(88);
                //更新获取备课币数据表
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setTime(System.currentTimeMillis());
                userIntegral.setUserId(user.getId());
                userIntegral.setOperatorId(user.getId());
                userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_INTEGRAL);
                userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_REGISTER);
                userIntegral.setNum(88);
                userIntegral.setCreateId(UsersUtil.id());
                userIntegral.setUpdateId(UsersUtil.id());
                userIntegral.setCreateTime(System.currentTimeMillis());
                userIntegral.setUpdateTime(System.currentTimeMillis());
                userIntegralDao.insert(userIntegral);
                extendDao.insert(userExtend);
                if (reqDto.getInviteId() != null && reqDto.getInviteId() != 0L) {//邀请注册
                    // TODO 待确认邀请积分
                    UserExtend mu = extendDao.selectByPrimaryKey(reqDto.getInviteId());
                    if (mu != null) {
                        mu.setIntegral(mu.getIntegral() == null ? 100 : mu.getIntegral() + 100);
                        extendDao.updateByPrimaryKey(mu);
                        userIntegral = new UserIntegral();
                        userIntegral.setTime(System.currentTimeMillis());
                        userIntegral.setUserId(mu.getId());
                        userIntegral.setOperatorId(mu.getId());
                        userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                        userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                        userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_INTEGRAL);
                        userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_NEW);
                        //积分数目--待定 TODO cici
                        userIntegral.setNum(100);
                        userIntegral.setCreateId(UsersUtil.id());
                        userIntegral.setUpdateId(UsersUtil.id());
                        userIntegral.setCreateTime(System.currentTimeMillis());
                        userIntegral.setUpdateTime(System.currentTimeMillis());
                        userIntegralDao.insert(userIntegral);
                    }
                }
            } else {//判断是否传输昵称等信息，更新昵称
                if (!Strings.isNullOrEmpty(reqDto.getNickName())) {
                    user.setNickName(reqDto.getNickName());
                    user.setName(reqDto.getNickName());
                }
                if (!Strings.isNullOrEmpty(reqDto.getImage())) {
                    user.setImage(reqDto.getImage());
                }
                super.save(user);
            }
            if (!EntitySetting.COMMON_SUCCESS.equals(user.getStatus())) {
                dto.setMsg("该用户已禁用,请联系管理员");
            } else {
                dto.setStatus(true);
                dto = findUserInfoById(user.getId());
                Subject s = SecurityUtils.getSubject();
                PapersShiroUserToken shiroUser = new PapersShiroUserToken(user.getId(), user.getPhone() == null ? "0" : user.getPhone(), user.getPassword() == null ? "123456" : user.getPassword(), user.getName() == null ? "0" : user.getName(), "user", null);
                s.login(shiroUser);
                //保存用户登录信息
                UserLoginRecord userLoginRecord = new UserLoginRecord();
                userLoginRecord.setTime(System.currentTimeMillis());
                userLoginRecord.setPlatform(EntitySetting.USER_TYPE_PC);
                userLoginRecord.setUserId(user.getId());
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                if (request != null) {
                    userLoginRecord.setIp(NetworkUtil.ipAddress(request));
                }
                userLoginRecordService.save(userLoginRecord);
            }

        }
        return dto;
    }

    /**
     * 绑定第三方账号;;废弃；
     *
     * @param userId
     * @param qq
     * @param weibo
     * @param wechat
     * @return
     */
    @Transactional
    public StatusDto updateThirdAccount(Long userId, String qq, String weibo, String wechat) {
        StatusDto dto = new StatusDto(false);
        if (userId == null) {
            dto.setMsg("用户不存在");
        } else if (Strings.isNullOrEmpty(qq) && Strings.isNullOrEmpty(wechat) && Strings.isNullOrEmpty(weibo)) {
            dto.setMsg("至少要绑定一个第三方账号");
        } else {
            User user = findById(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                boolean flag = false;
                if (!Strings.isNullOrEmpty(qq)) {//绑定QQ
                    User temp = new User();
                    temp.setQq(qq);
                    temp = dao.selectOne(temp);
                    if (temp != null && temp.getId() != user.getId()) {
                        dto.setMsg("该QQ已绑定账号");
                    } else {
                        user.setQq(qq);
                        flag = true;
                    }
                } else if (!Strings.isNullOrEmpty(wechat)) {
                    User temp = new User();
                    temp.setWechat(wechat);
                    temp = dao.selectOne(temp);
                    if (temp != null && temp.getId() != user.getId()) {
                        dto.setMsg("该微信已绑定账号");
                    } else {
                        user.setWechat(wechat);
                        flag = true;
                    }
                } else if (!Strings.isNullOrEmpty(weibo)) {
                    User temp = new User();
                    temp.setWeibo(weibo);
                    temp = dao.selectOne(temp);
                    if (temp != null && temp.getId() != user.getId()) {
                        dto.setMsg("该微信已绑定账号");
                    } else {
                        user.setWeibo(weibo);
                        flag = true;
                    }
                }
                if (flag) {
                    dto.setStatus(true);
                    super.save(user);
                }
            }
        }
        return dto;
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @Transactional
    public StatusDto updateUser(User user) {
        StatusDto dto = new StatusDto(true);
        if (!Strings.isNullOrEmpty(user.getPhone())) {//查询手机号是否一致，并且唯一
            User dbUser = new User();
            dbUser.setPhone(user.getPhone());
            dbUser = dao.selectOne(dbUser);
            if (dbUser != null && dbUser.getId() != user.getId()) {
                dto.setMsg("该手机号已存在,不能绑定为安全手机号");
                dto.setStatus(false);
            }
        }
        //查看QQ、微信、微博是否有传输绑定
        if(!(Strings.isNullOrEmpty(user.getQq()) && Strings.isNullOrEmpty(user.getWechat()) && Strings.isNullOrEmpty(user.getWeibo()))) {
            if (!Strings.isNullOrEmpty(user.getQq())) {//绑定QQ
                User temp = new User();
                temp.setQq(user.getQq());
                temp = dao.selectOne(temp);
                if (temp != null && temp.getId() != user.getId()) {
                    dto.setMsg("该QQ已绑定账号");
                    dto.setStatus(false);
                }
            } else if (!Strings.isNullOrEmpty(user.getWechat())) {
                User temp = new User();
                temp.setWechat(user.getWechat());
                temp = dao.selectOne(temp);
                if (temp != null && temp.getId() != user.getId()) {
                    dto.setMsg("该微信已绑定账号");
                    dto.setStatus(false);
                }
            } else if (!Strings.isNullOrEmpty(user.getWeibo())) {
                User temp = new User();
                temp.setWeibo(user.getWeibo());
                temp = dao.selectOne(temp);
                if (temp != null && temp.getId() != user.getId()) {
                    dto.setMsg("该微信已绑定账号");
                    dto.setStatus(false);
                }
            }
        }
        if (dto.getStatus()) {
            super.updateByIdSelective(user);
        }
        return dto;
    }

    @Transactional
    public StatusDto updateStatusByIds(List<Long> ids, Integer status) {
        StatusDto dto = new StatusDto(false);
        if(Collections3.isEmpty(ids)||status==null){
            dto.setMsg("参数为空");
        }else{
            ids.forEach(d->{
                User user = new User();
                user.setId(d);
                user.setStatus(status);
                super.updateByIdSelective(user);
            });
        }
        return dto;
    }
}