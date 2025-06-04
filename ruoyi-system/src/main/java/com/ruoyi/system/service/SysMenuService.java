package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.dto.SysMenuInsertDTO;
import com.ruoyi.system.dto.SysMenuUpdateDTO;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.vo.SysMenuVO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理 业务层
 *
 * @author ruoyi
 */
public interface SysMenuService {

    /**
     * 根据条件查询菜单列表
     */
    List<SysMenuVO> selectMenuList(SysMenuQuery query);

    /**
     * 查询菜单信息
     */
    SysMenuVO selectMenuById(String menuId);

    /**
     * 新增菜单
     */
    void insertMenu(@Valid SysMenuInsertDTO dto);

    /**
     * 修改菜单
     */
    void updateMenu(@Valid SysMenuUpdateDTO dto);

    /**
     * 删除菜单
     */
    void deleteMenuById(String menuId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenuVO> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<String> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenuVO> menus);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param query 菜单列表
     * @return 下拉树结构列表
     */
    List<TreeSelect> buildMenuTreeSelect(SysMenuQuery query);

}
