package com.ixinhoo.papers.entity;

/**
 * 实体配置
 *
 * @author cici
 */
public class EntitySetting {
    //阿里大鱼短信begin
    public static final String APPKEY = "LTAIQJbabTAqp2qM";
    public static final String SECRET = "47gMmw1JKruxAIDDAgcLEVxy3uxLHm";
    public static final String SMSTYPE = "normal";
    public static final String URL = "http://gw.api.taobao.com/router/rest";
    //阿里大鱼短信end

    //七牛云文件--begin
    public static final long QINIU_FILE_EXPIRE_SECONDS=3600*24*15;//文件过期时间，3600为1小时
    //七牛云文件--end

    //常量数据都是整型数据
    //通用类型--begin
    public static final Integer COMMON_SUCCESS = 1;//成功、启用、通过
    public static final Integer COMMON_FAIL = 2;//失败、禁用、不通过
    public static final Integer COMMON_WAIT = 3;//等待状态
    //通用类型--end

    //管理员操作记录--begin
    public static final Integer OPERATE_LOGIN = 1;//登录
    public static final Integer OPERATE_OUT = 2;//登出
    public static final Integer OPERATE_AUTHORITY = 3;//授权
    public static final Integer OPERATE_EDIT_DATA = 4;//数据修改
    //管理员操作记录--end

    //用户角色--begin
    public static final Integer USER_TYPE_PC = 1;//网站用户
    public static final Integer USER_TYPE_ADMIN = 2;//管理员用户
    //用户角色--end

    //用户积分--begin
    public static final Integer USER_INTEGRAL_PLUS = 1;//增加
    public static final Integer USER_INTEGRAL_REDUCE = 2;//减少
    public static final Integer USER_INTEGRAL_OPERATOR_USER = USER_TYPE_PC;//用户
    public static final Integer USER_INTEGRAL_OPERATOR_ADMIN = USER_TYPE_ADMIN;//管理员
    public static final Integer USER_INTEGRAL_RECORD_COIN = 1;//备课币
    public static final Integer USER_INTEGRAL_RECORD_INTEGRAL = 2;//积分
    public static final Integer USER_INTEGRAL_SOURCE_CONSUME = 1;//来源--消费
    public static final Integer USER_INTEGRAL_SOURCE_PAYMENT = 2;//来源--充值
    public static final Integer USER_INTEGRAL_SOURCE_REGISTER = 3;//来源--注册
    public static final Integer USER_INTEGRAL_SOURCE_NEW = 4;//来源--拉新
    //用户积分--end

    //资源数据类型--begin
    public static final Integer DATA_TYPE_DOCUMENT = 1;//文档资料
    public static final Integer DATA_TYPE_PAPERS = 2;//试卷
    public static final Integer DATA_TYPE_QUESTION = 20;//试题
    //资源数据类型--end

    //短信验证码--begin
    public static final Integer MESSAGE_CODE_TYPE_REGISTER = 1;//注册
    public static final Integer MESSAGE_CODE_TYPE_CHANGE_PHONE = 2;//更换手机号
    public static final Integer MESSAGE_CODE_TYPE_VALID_PHONE = 3;//验证更换手机号
    public static final Integer MESSAGE_CODE_TYPE_RESET_PASSWORD = 4;//忘记密码
    public static final Integer MESSAGE_CODE_TYPE_SETUP_PHONE = 5;//设置手机号
    //短信验证码--end

    //章节类型--begin
    public static final Integer CHAPTERS_TYPE_VERSION = 1;//版本
    public static final Integer CHAPTERS_TYPE_BOOK = 2;//册别
    public static final Integer CHAPTERS_TYPE_CHAPTER = 3;//同步章节
    public static final Integer CHAPTERS_TYPE_EXAM = 4;//备考章节
    //章节类型--end

    //学期类型--begin
    public static final Integer GRADE_TERM_ALL = 0;//全册
    public static final Integer GRADE_TERM_UP = 1;//上学期
    public static final Integer GRADE_TERM_DOWN = 2;//下学期
    //学期类型--end

    //试卷类型--begin
    public static final Integer PAPER_UESR_SHARE_PRIVATE = 0;//用户私有
    public static final Integer PAPER_UESR_SHARE_AUDIT_PASS = COMMON_SUCCESS;//通过审核
    public static final Integer PAPER_UESR_SHARE_AUDIT_FAIL = COMMON_FAIL;//不通过审核
    public static final Integer PAPER_UESR_SHARE_AUDIT_WAIT = COMMON_WAIT;//待通过审核
    public static final Integer PAPER_UESR_SHARE_DELETE = 4;//丢进回收站
    //试卷类型--end


    //推送通知类型--begin
    public static final Integer MESSAGE_NOTICE_PUSH_TYPE_ALL = 1;//所有
    public static final Integer MESSAGE_NOTICE_PUSH_TYPE_USER = 2;//个人
    public static final Integer MESSAGE_NOTICE_PUSH_TYPE_ROLE = 3;//角色
    public static final Integer MESSAGE_NOTICE_PUSH_TYPE_RANG = 4;//活动范围
    public static final Integer MESSAGE_NOTICE_TYPE_ACTIVITY = 3;//平台活动
    //推送通知类型--end

    //支付类型--begin
    public static final Integer USER_ORDER_PAY_TYPE_COIN = 1;//备课币
    public static final Integer USER_ORDER_PAY_TYPE_WECHAT= 2;//微信
    public static final Integer USER_ORDER_PAY_TYPE_ALIPAY = 3;//支付宝
    //支付类型--end

    //订单类型--begin
    public static final Integer USER_ORDER_TYPE_RECHARGE = 1;//充值
    public static final Integer USER_ORDER_TYPE_DOWNLOAD = 2;//下载
    public static final Integer USER_ORDER_TYPE_PAPERS = 3;//组卷
    //订单类型--end

    //21接口信息--begin
    public static final boolean TEST_ENV = false;//是否为测试环境,true表示为测试环境,false为正式环境
    public static final int INTERFACE_FIRST_PAGE = 1;//分页的开始页数
    public static final int INTERFACE_MAX_PERPAGE = 50;//分页的最大页数
    public static final Integer INTERFACE_AREA = 1;//省市区数据
    public static final Integer INTERFACE_SUBJECTS = 2;//学科接口
    public static final Integer INTERFACE_KNOWLEDGE = 3;//知识点接口
    public static final Integer INTERFACE_VERSION = 4;//版本接口
    public static final Integer INTERFACE_BOOK = 5;//册别接口
    public static final Integer INTERFACE_CHAPTER = 6;//章节接口
    public static final Integer INTERFACE_PAPER_TYPE = 7;//试卷类型接口
    public static final Integer INTERFACE_PAPERS = 8;//试卷接口
    public static final Integer INTERFACE_QUESTION_TYPE = 9;//题目题型接口
    public static final Integer INTERFACE_DOCUMENT_STAGE = 10;//资源列表
    public static final Integer INTERFACE_QUESTION = 11;//试题列表
    //21接口信息--end
}