package com.ruoyi.system.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TreeSelectUtils;
import com.ruoyi.system.converter.SysMenuConverter;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.dto.SysMenuInsertDTO;
import com.ruoyi.system.dto.SysMenuUpdateDTO;
import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.repository.SysMenuRepository;
import com.ruoyi.system.repository.SysRoleMenuRepository;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.vo.SysMenuVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ruoyi.system.entity.QSysDict.sysDict;
import static com.ruoyi.system.entity.QSysMenu.sysMenu;
import static com.ruoyi.system.entity.QSysRoleMenu.sysRoleMenu;
import static com.ruoyi.system.entity.QSysUserRole.sysUserRole;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private JPQLQueryFactory jpqlQueryFactory;

    @Resource
    private SysMenuRepository sysMenuRepository;
    @Resource
    private SysMenuConverter sysMenuConverter;

    @Resource
    private SysRoleMenuRepository sysRoleMenuRepository;

    /**
     * 根据条件查询菜单列表
     */
    @Override
    public List<SysMenuVO> selectMenuList(SysMenuQuery query) {
        Iterable<SysMenu> list = selectMenuList(query, SecurityUtils.getUserId());

        return sysMenuConverter.toSysMenuVO(list);
    }

    /**
     * 查询菜单信息
     */
    @Override
    public SysMenuVO selectMenuById(String menuId) {
        SysMenu info = sysMenuRepository.getReferenceById(menuId);
        if (ObjectUtils.allNull(info)) {
            throw new ServiceException("菜单信息不存在");
        }

        return sysMenuConverter.toSysMenuVO(info);
    }

    /**
     * 新增菜单
     */
    @Override
    public void insertMenu(@Valid SysMenuInsertDTO dto) {
        if (Objects.equals(dto.getIsFrame(), UserConstants.YES_FRAME) && !StringUtils.ishttp(dto.getPath())) {
            throw new ServiceException("新增菜单'{}'失败，地址必须以http(s)://开头", dto.getMenuName());
        }

        SysMenu info = sysMenuRepository.findByMenuNameAndParentId(dto.getMenuName(), dto.getParentId());
        if (ObjectUtils.allNotNull(info)) {
            throw new ServiceException("新增菜单'{}'失败，菜单名称已存在", dto.getMenuName());
        }

        SysMenu menu = sysMenuConverter.toSysMenu(dto);
        menu.markNew();
        sysMenuRepository.save(menu);
    }

    /**
     * 修改菜单
     */
    @Override
    public void updateMenu(SysMenuUpdateDTO dto) {
        if (Objects.equals(dto.getMenuId(), dto.getParentId())) {
            throw new ServiceException("修改菜单'{}'失败，上级菜单不能选择自己", dto.getMenuName());
        }
        if (Objects.equals(dto.getIsFrame(), UserConstants.YES_FRAME) && !StringUtils.ishttp(dto.getPath())) {
            throw new ServiceException("修改菜单'{}'失败，地址必须以http(s)://开头", dto.getMenuName());
        }

        SysMenu menu = sysMenuRepository.getReferenceById(dto.getMenuId());
        if (ObjectUtils.allNull(menu)) {
            throw new ServiceException("修改菜单'{}'失败，菜单不存在", dto.getMenuName());
        }
        SysMenu check = sysMenuRepository.findByMenuNameAndParentId(dto.getMenuName(), dto.getParentId());
        if (ObjectUtils.allNotNull(check) && ObjectUtils.notEqual(menu.getMenuId(), check.getMenuId())) {
            throw new ServiceException("新增菜单'{}'失败，菜单名称已存在", dto.getMenuName());
        }

        sysMenuConverter.toSysMenu(dto, menu);
        sysMenuRepository.save(menu);
    }

    /**
     * 删除菜单
     */
    @Override
    public void deleteMenuById(String menuId) {
        if (sysMenuRepository.existsByParentId(menuId)) {
            throw new ServiceException("存在子菜单，不允许删除");
        }
        if (sysRoleMenuRepository.existsByMenuId(menuId)) {
            throw new ServiceException("菜单已分配，不允许删除");
        }
        sysMenuRepository.deleteById(menuId);
    }

    /**
     * 查询系统菜单列表
     */
    public Iterable<SysMenu> selectMenuList(SysMenuQuery query, Long userId) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getMenuName())) {
            predicate.and(sysDict.dictType.contains(query.getMenuName()));
        }
        if (ObjectUtils.allNotNull(query.getVisible())) {
            predicate.and(sysDict.status.eq(query.getVisible()));
        }
        if (ObjectUtils.allNotNull(query.getStatus())) {
            predicate.and(sysDict.status.eq(query.getStatus()));
        }
        // 管理员显示所有菜单信息
        if (!SysUser.isAdmin(userId)) {
            predicate.and(sysMenu.menuId.in(
                    jpqlQueryFactory.select(sysRoleMenu.menuId)
                            .from(sysRoleMenu)
                            .leftJoin(sysUserRole).on(sysUserRole.roleId.eq(sysRoleMenu.roleId))
                            .where(sysUserRole.userId.eq(userId))
            ));
        }

        return sysMenuRepository.findAll(predicate, sysDict.dictSort.asc());
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param query 菜单列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(SysMenuQuery query) {
        Iterable<SysMenu> iterable = selectMenuList(query, SecurityUtils.getUserId());

        return TreeSelectUtils.build(iterable, item -> {
            TreeSelect ts = new TreeSelect();
            ts.setId(item.getId());
            ts.setParentId(item.getParentId());
            ts.setLabel(item.getMenuName());
            return ts;
        });
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = jpqlQueryFactory.select(sysMenu.perms)
                .from(sysMenu)
                .leftJoin(sysRoleMenu).on(sysRoleMenu.menuId.eq(sysMenu.menuId))
                .leftJoin(sysUserRole).on(sysUserRole.roleId.eq(sysRoleMenu.roleId))
                .where(sysUserRole.userId.eq(userId))
                .fetch();
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = jpqlQueryFactory.select(sysMenu.perms)
                .from(sysMenu)
                .leftJoin(sysRoleMenu).on(sysRoleMenu.menuId.eq(sysMenu.menuId))
                .where(sysRoleMenu.roleId.eq(String.valueOf(roleId)))
                .fetch();
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenuVO> selectMenuTreeByUserId(Long userId) {
        JPQLQuery<SysMenu> query = jpqlQueryFactory.selectFrom(sysMenu)
                .where(sysMenu.menuType.in(UserConstants.TYPE_DIR, UserConstants.TYPE_MENU))
                .where(sysMenu.status.eq(UserConstants.NORMAL));

        if (!SecurityUtils.isAdmin(userId)) {
            query.leftJoin(sysRoleMenu).on(sysRoleMenu.menuId.eq(sysMenu.menuId))
                    .leftJoin(sysUserRole).on(sysUserRole.roleId.eq(sysRoleMenu.roleId))
                    .where(sysUserRole.userId.eq(userId));
        }

        List<SysMenu> list = query.fetch();

        return TreeSelectUtils.build(sysMenuConverter.toSysMenuVO(list), SysMenuVO::getMenuId, SysMenuVO::getParentId, SysMenuVO::setChildren);
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<String> selectMenuListByRoleId(Long roleId) {
        return jpqlQueryFactory.select(sysRoleMenu.menuId)
                .from(sysRoleMenu)
                .where(sysRoleMenu.roleId.eq(String.valueOf(roleId)))
                .fetch();
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenuVO> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenuVO menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(Objects.equals(menu.getVisible(), '1'));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), Objects.equals(1, menu.getIsCache()), menu.getPath()));
            List<SysMenuVO> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(getRouteName(menu.getRouteName(), menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), Objects.equals(1, menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().equals("0") && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(getRouteName(menu.getRouteName(), routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuVO menu) {
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            return StringUtils.EMPTY;
        }
        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {
        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuVO menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (ObjectUtils.notEqual(menu.getParentId(), "0") && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (Objects.equals(menu.getParentId(), "0") && UserConstants.TYPE_DIR.equals(menu.getMenuType()) && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuVO menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && ObjectUtils.notEqual(menu.getParentId(), "0") && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuVO menu) {
        return Objects.equals(menu.getParentId(), "0") && UserConstants.TYPE_MENU.equals(menu.getMenuType()) && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenuVO menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuVO menu) {
        return ObjectUtils.notEqual(menu.getParentId(), "0") && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"}, new String[]{"", "", "", "/", "/"});
    }

}
