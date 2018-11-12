package com.ixinhoo.papers.service.authority;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.DataTableRequest;
import com.chunecai.crumbs.api.constants.UserPasswordEncrypt;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.authority.RoleMenuDao;
import com.ixinhoo.papers.dao.authority.SystemUserDao;
import com.ixinhoo.papers.dao.authority.UserRoleDao;
import com.ixinhoo.papers.dto.authority.SystemUserDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.RoleMenu;
import com.ixinhoo.papers.entity.authority.SystemUser;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import com.ixinhoo.papers.entity.authority.UserRole;
import com.ixinhoo.shiro.PapersShiroUserToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemUserService extends BaseService<SystemUser> {
    @Autowired
    private SystemUserDao dao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private SystemUserOperateService operateService;
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 新增、编译用户
     *
     * @param userDto
     * @return
     */
    @Transactional
    public StatusDto saveSystemUser(SystemUserDto userDto) {
        StatusDto dto = new StatusDto(false);
        if (userDto == null) {
            dto.setMsg("参数不能为空");
        } else {
            SystemUser user = BeanMapper.map(userDto, SystemUser.class);
            if (user.getStatus() == null) {
                user.setStatus(EntitySetting.COMMON_SUCCESS);
            }
            SystemUser dbSelectUser = new SystemUser();
            dbSelectUser.setLoginName(userDto.getLoginName());
            SystemUser loginNameUser = dao.selectOne(dbSelectUser);
            if (user.getId() == null || user.getId() == 0L) {
                if (loginNameUser != null) {
                    dto.setMsg("登陆账号已存在,请重新输入");
                } else {
                    if (Strings.isNullOrEmpty(user.getPassword())) {
                        user.setPassword("123456");
                    }
                    UserPasswordEncrypt up = UsersUtil.encryptPassword(user.getPassword());
                    user.setPassword(up.getPassword());
                    user.setSalt(up.getSalt());
                    super.create(user);
                    if (userDto.getRoleId() != null && userDto.getRoleId() != 0L) {
                        UserRole userRole = new UserRole();
                        userRole.setType(EntitySetting.USER_TYPE_ADMIN);
                        userRole.setRoleId(userDto.getRoleId());
                        userRole.setUserId(user.getId());
                        userRoleDao.insert(userRole);
                    }
                    dto.setStatus(true);
                }
            } else {
                if (loginNameUser != null && !userDto.getLoginName().equals(loginNameUser.getLoginName())) {
                    dto.setMsg("登陆账号已存在,请重新输入");
                } else {
                    SystemUser dbUser = dao.selectByPrimaryKey(user.getId());
                    if (dbUser == null) {
                        dto.setMsg("用户不存在");
                    } else {
                        if (!Strings.isNullOrEmpty(user.getPassword())) {
                            String pwd = UsersUtil.encryptPasswordBySalt(user.getPassword(), dbUser.getSalt());
                            dbUser.setPassword(pwd);
                        }
                        dbUser.setLoginName(user.getLoginName());
                        dbUser.setName(user.getName());
                        super.updateById(dbUser);
                        if (dbUser.getId() != 1L) {//超级管理员角色不能修改
                            UserRole userRole = new UserRole();
                            userRole.setType(EntitySetting.USER_TYPE_ADMIN);
                            userRole.setRoleId(userDto.getRoleId());
                            userRole.setUserId(dbUser.getId());
                            //删除后重新加入
                            userRoleDao.delete(userRole);
                            if (userDto.getRoleId() != null && userDto.getRoleId() != 0L) {
                                userRoleDao.insert(userRole);
                            }
                        }
                        dto.setStatus(true);
                    }
                }

            }
        }
        return dto;
    }

    public DataTable<SystemUserDto> listSystemUser(DataTableRequest req) {
        DataTable<SystemUser> dataTable = super.listDatatable(req);
        DataTable<SystemUserDto> dtoDataTable = new DataTable<>();
        if (dataTable != null && Collections3.isNotEmpty(dataTable.getData())) {
            dtoDataTable.setMeta(dataTable.getMeta());
            List<SystemUser> list = dataTable.getData();
            List<Long> ids = list.stream().map(d -> d.getId()).collect(Collectors.toList());
            //java8的语法，UserRole::getUserId 自动转换成对应的列名
            List<UserRole> userRoleList = userRoleDao.selectByExample(new Example.Builder(UserRole.class)
                    .where(WeekendSqls.<UserRole>custom().andIn(UserRole::getUserId, ids))
                    .build());
            Map<Long, Long> map = Maps.newHashMap();
            if (Collections3.isNotEmpty(userRoleList)) {
                userRoleList.forEach(d -> {
                    map.put(d.getUserId(), d.getRoleId());
                });
            }
            List<SystemUserDto> dtoList = Lists.newArrayList();
            list.forEach(d -> {
                SystemUserDto systemUserDto = BeanMapper.map(d, SystemUserDto.class);
                if (map.containsKey(d.getId())) {
                    systemUserDto.setRoleId(map.get(d.getId()));
                }
                dtoList.add(systemUserDto);
            });
            dtoDataTable.setData(dtoList);
        }
        return dtoDataTable;
    }

    @Transactional(readOnly = false)
    public DetailDto<SystemUserDto> loginUser(SystemUserDto userDto, String ip) {
        DetailDto<SystemUserDto> dto = new DetailDto(false);
        if (userDto == null) {
            dto.setMsg("参数为空");
        } else if (Strings.isNullOrEmpty(userDto.getLoginName())) {
            dto.setMsg("登陆账号为空");
        } else if (Strings.isNullOrEmpty(userDto.getPassword())) {
            dto.setMsg("密码为空");
        } else {
            SystemUser dbSelectUser = new SystemUser();
            dbSelectUser.setLoginName(userDto.getLoginName());
            SystemUser user = dao.selectOne(dbSelectUser);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                if (!EntitySetting.COMMON_SUCCESS.equals(user.getStatus())) {
                    dto.setMsg("用户已禁用");
                } else {
                    String password = UsersUtil.encryptPasswordBySalt(userDto.getPassword(), user.getSalt());
                    if (!password.equals(user.getPassword())) {
                        dto.setMsg("用户名或密码不正确");
                    } else {
                        //查询用户的角色
                        UserRole userRole = new UserRole();
                        userRole.setType(EntitySetting.USER_TYPE_ADMIN);
                        userRole.setUserId(user.getId());
                        List<UserRole> list = userRoleDao.select(userRole);
                        if (Collections3.isEmpty(list)) {
                            dto.setMsg("该用户没有授权角色,请联系超级管理员");
                        } else {
                            userRole = list.get(0);
                            dto.setStatus(true);
                            SystemUserDto systemUserDto = BeanMapper.map(user, SystemUserDto.class);
                            systemUserDto.setRoleId(userRole.getRoleId());
                            //角色菜单
                            List<RoleMenu> menuList = roleMenuDao.selectByExample(new Example.Builder(RoleMenu.class)
                                    .where(WeekendSqls.<RoleMenu>custom().andEqualTo(RoleMenu::getRoleId,userRole.getRoleId())).build());
                            if(Collections3.isNotEmpty(menuList)){
                                List<Long> ids = Lists.newArrayList();
                                menuList.forEach(d->{
                                    ids.add(d.getMenuId());
                                });
                                systemUserDto.setMenuIds(ids);
                            }
                            dto.setDetail(systemUserDto);
                            Subject s = SecurityUtils.getSubject();
                            PapersShiroUserToken shiroUser = new PapersShiroUserToken(user.getId(), user.getLoginName(), userDto.getPassword(), user.getName(), "server", userRole.getId());
                            s.login(shiroUser);
                            SystemUserOperate operate = new SystemUserOperate();
                            operate.setIp(ip);
                            operate.setTime(System.currentTimeMillis());
                            operate.setType(EntitySetting.OPERATE_LOGIN);
                            operate.setUserId(user.getId());
                            operate.setUserName(user.getName());
                            operate.setContent("管理员登陆后台");
                            operateService.loggerInsert(operate);
                        }
                    }
                }
            }
        }
        return dto;
    }

    @Transactional(readOnly = false)
    public StatusDto deleteSystemUserByIds(List<Long> ids) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids)) {
            dto.setMsg("参数为空");
        } else if (ids.contains(1)) {
            dto.setMsg("超级管理员不能删除");
        } else {
            super.deleteByIds(ids);
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional(readOnly = false)
    public void logout(String ip) {
        SystemUserOperate operate = new SystemUserOperate();
        operate.setIp(ip);
        operate.setTime(System.currentTimeMillis());
        operate.setType(EntitySetting.OPERATE_OUT);
        operate.setUserId(UsersUtil.id());
        operate.setUserName(UsersUtil.name());
        operate.setContent("管理员登出后台");
        operateService.loggerInsert(operate);
        Subject s = SecurityUtils.getSubject();
        s.logout();
    }

    @Transactional
    public StatusDto test() {
        Map map = new HashMap();
        map.put("id", 1L);
        map.put("userName", "");
        map.put("userCode", "");
        //可以调用，但是返回的数据在map中原路返回，注意在注解中固定格式和输出方式
        Object object = dao.callGenCiPropertyValue(map);
        System.out.println(object);
        System.out.println(map);
        return null;
    }

    public DetailDto<SystemUserDto> findSystemUserDtoById(Long id) {
        DetailDto<SystemUserDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("参数为空");
        } else {
            SystemUser user = super.findById(id);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                List<UserRole> userRoleList = userRoleDao.selectByExample(new Example.Builder(UserRole.class)
                        .where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, id))
                        .build());
                SystemUserDto detail = BeanMapper.map(user, SystemUserDto.class);
                if (Collections3.isNotEmpty(userRoleList)) {
                    detail.setRoleId(userRoleList.get(0).getRoleId());
                }
                dto.setDetail(detail);
                dto.setStatus(true);
            }
        }
        return dto;
    }
}