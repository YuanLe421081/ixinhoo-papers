package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户信息
 *
 * @author cici
 */
@Table(name = "user")
public class User extends AuditEntity {
    @Column
    private String phone;//手机号
    @Column
    private String account;//账号
    @Column
    private String salt;//秘钥
    @Column
    private String password;//密码
    @Column
    private String nickName;//昵称
    @Column
    private String image;//头像
    @Column
    private String name;//姓名
    @Column
    private Integer sex;//性别，1-男、2-女
    @Column
    private Long birthday;//生日
    @Column
    private Integer stage;//学段
    @Column
    private Integer grade;//年级
    @Column
    private Long subjectId;//学科id
    private Long versionId;//版本id
    private String versionName;//版本名称
    @Column
    private String subjectName;//学科名称
    @Column
    private Long schoolId;//学校id
    @Column
    private String schoolName;//学校名称
    @Column
    private String qq;//qq号
    @Column
    private String wechat;//微信号
    @Column
    private String weibo;//微博号
    @Column
    private Long registerTime;//注册时间
    @Column
    private Integer status;//状态
    @Column
    private Integer teachingAge;//教龄
    @Column
    private Long provinceId;//省id
    @Column
    private String provinceName;//省名称
    @Column
    private Integer completed;//是否已经领取了完善资料的备课币,1--领取、3--待领取
    private Long firstPayTime;//第一次支付时间

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getFirstPayTime() {
        return firstPayTime;
    }

    public void setFirstPayTime(Long firstPayTime) {
        this.firstPayTime = firstPayTime;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public Long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Long registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTeachingAge() {
        return teachingAge;
    }

    public void setTeachingAge(Integer teachingAge) {
        this.teachingAge = teachingAge;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}