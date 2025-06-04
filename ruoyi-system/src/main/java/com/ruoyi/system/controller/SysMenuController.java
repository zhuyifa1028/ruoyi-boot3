package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysMenuInsertDTO;
import com.ruoyi.system.dto.SysMenuUpdateDTO;
import com.ruoyi.system.query.SysMenuQuery;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.vo.SysMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "【系统管理】菜单管理")
@Validated
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    @Resource
    private SysMenuService menuService;

    @Operation(summary = "根据条件查询菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public R<List<SysMenuVO>> selectMenuList(SysMenuQuery query) {
        return R.ok(menuService.selectMenuList(query));
    }

    @Operation(summary = "查询菜单信息")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public R<SysMenuVO> selectMenuById(@PathVariable String menuId) {
        return R.ok(menuService.selectMenuById(menuId));
    }

    @Operation(summary = "新增菜单")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping
    public R<Void> insertMenu(@RequestBody @Valid SysMenuInsertDTO dto) {
        menuService.insertMenu(dto);
        return R.ok();
    }

    @Operation(summary = "修改菜单")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PutMapping
    public R<Void> updateMenu(@RequestBody @Valid SysMenuUpdateDTO dto) {
        menuService.updateMenu(dto);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @DeleteMapping("/{menuId}")
    public R<Void> remove(@PathVariable String menuId) {
        menuService.deleteMenuById(menuId);
        return R.ok();
    }

    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public R<List<TreeSelect>> treeselect(SysMenuQuery query) {
        return R.ok(menuService.buildMenuTreeSelect(query));
    }

    @Operation(summary = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(new SysMenuQuery()));
        return ajax;
    }
}
