package com.ixinhoo.papers.service.authority;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.DataTableMeta;
import com.chunecai.crumbs.api.code.DataTableRequest;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.authority.RoleDao;
import com.ixinhoo.papers.dao.authority.RoleMenuDao;
import com.ixinhoo.papers.dao.authority.UserRoleDao;
import com.ixinhoo.papers.dto.authority.RoleMenuDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.Role;
import com.ixinhoo.papers.entity.authority.RoleMenu;
import com.ixinhoo.papers.entity.authority.UserRole;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;

@Service
public class RoleService extends BaseService<Role> {
    @Autowired
    private RoleDao dao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleMenuDao roleMenuDao;


    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto saveRoleMenu(Long roleId, List<Long> menuIds) {
        StatusDto dto = new StatusDto(false);
        if (roleId == null) {
            dto.setMsg("参数为空");
        } else {
            //删除角色下的菜单
            dto.setStatus(true);
            Role dbRole = super.findById(roleId);
            if (dbRole != null) {
                roleMenuDao.deleteByExample(new Example.Builder(RoleMenu.class)
                        .where(WeekendSqls.<RoleMenu>custom().andEqualTo(RoleMenu::getRoleId, roleId)).build());
                //重新设置菜单
                menuIds.forEach(d -> {
                    RoleMenu menu = new RoleMenu();
                    menu.setMenuId(d);
                    menu.setRoleId(roleId);
                    roleMenuDao.insert(menu);
                });
            }
        }
        return dto;
    }

    public DetailDto<RoleMenuDto> findRoleById(Long id) {
        DetailDto<RoleMenuDto> dto = new DetailDto<>(true);
        Role role = super.findById(id);
        RoleMenuDto roleMenuDto = BeanMapper.map(role, RoleMenuDto.class);
        //查询菜单
        List<RoleMenu> menuList = roleMenuDao.selectByExample(new Example.Builder(RoleMenu.class)
                .where(WeekendSqls.<RoleMenu>custom().andEqualTo(RoleMenu::getRoleId, id)).build());
        if (Collections3.isNotEmpty(menuList)) {
            List<Long> ids = Lists.newArrayList();
            menuList.forEach(d -> {
                ids.add(d.getMenuId());
            });
            roleMenuDto.setMenuIds(ids);
        }
        dto.setDetail(roleMenuDto);
        return dto;
    }

    /**
     * 只查询默认账号的
     *
     * @return
     */
    public DataTable<Role> listDatatableRole() {
        DataTable<Role> dto = new DataTable<>();
        DataTableMeta meta = new DataTableMeta();
        List<Role> list = getBaseDao().selectByExample(new Example.Builder(Role.class)
                .where(WeekendSqls.<Role>custom().andIn(Role::getId, Lists.newArrayList(1, 2, 3))).build());
        dto.setData(list);
        int count = getBaseDao().selectCountByExample(new Example.Builder(Role.class)
                .where(WeekendSqls.<Role>custom().andIn(Role::getId, Lists.newArrayList(1, 2, 3))).build());
        meta.setTotal(count);
        /**
         *     private int page;//当前页
         private int pages;//总页数
         private int perpage;//页大小
         */
        meta.setPerpage(10);
        meta.setPages(1);
        meta.setPage(1);
        dto.setMeta(meta);
        return dto;
    }

    public DataTable<Role> listDatatableRoleNotice(DataTableRequest dataTable) {
        DataTable<Role> dto = new DataTable<>();
        DataTableMeta meta = new DataTableMeta();
        RowBounds rowBounds = null;
        if (dataTable != null && dataTable.getPagination().getPerpage()!=0) {
            rowBounds = new RowBounds(
                    (dataTable.getPagination().getPage() == 0
                            ? 0 : dataTable.getPagination().getPage() - 1)
                            * dataTable.getPagination().getPerpage(),
                    dataTable.getPagination().getPerpage());
            meta.setPerpage(dataTable.getPagination().getPerpage());
            meta.setPages(dataTable.getPagination().getPages());
            meta.setPage(dataTable.getPagination().getPage());
        }
        List<Role> list = getBaseDao().selectByExampleAndRowBounds(new Example.Builder(Role.class)
                .where(WeekendSqls.<Role>custom().andNotIn(Role::getId, Lists.newArrayList(1, 2, 3))).build(), rowBounds);
        dto.setData(list);
        int count = getBaseDao().selectCountByExample(new Example.Builder(Role.class)
                .where(WeekendSqls.<Role>custom().andNotIn(Role::getId, Lists.newArrayList(1, 2, 3))).build());
        meta.setTotal(count);
        dto.setMeta(meta);
        return dto;
    }

    public ListDto<UserRole> findUserRoleByUserId(Long userId) {
        ListDto<UserRole> dto = new ListDto<>(true);
        List<UserRole> list = userRoleDao.selectByExample(new Example.Builder(UserRole.class)
                .where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, userId)
                        .andEqualTo(UserRole::getType, EntitySetting.USER_TYPE_PC)).build());
        dto.setList(list);
        return dto;
    }

    @Transactional
    public StatusDto saveUserRole(Long userId, List<Long> roleIds) {
        StatusDto dto = new StatusDto(false);
        if (userId == null) {
            dto.setMsg("参数为空");
        } else {
            //删除用户下的角色
            dto.setStatus(true);
            userRoleDao.deleteByExample(new Example.Builder(UserRole.class)
                    .where(WeekendSqls.<UserRole>custom().andEqualTo(UserRole::getUserId, userId)
                            .andEqualTo(UserRole::getType, EntitySetting.USER_TYPE_PC)).build());
            //重新设置角色
            roleIds.forEach(d -> {
                UserRole menu = new UserRole();
                menu.setUserId(userId);
                menu.setRoleId(d);
                menu.setType(EntitySetting.USER_TYPE_PC);
                userRoleDao.insert(menu);
            });

        }
        return dto;
    }

    public ListDto<Role> listAllRoleNotice() {
        ListDto<Role> dto = new ListDto<>(true);
        List<Role> list = getBaseDao().selectByExample(new Example.Builder(Role.class)
                .where(WeekendSqls.<Role>custom().andNotIn(Role::getId, Lists.newArrayList(1, 2, 3))).build());
        dto.setList(list);
        return dto;
    }
}