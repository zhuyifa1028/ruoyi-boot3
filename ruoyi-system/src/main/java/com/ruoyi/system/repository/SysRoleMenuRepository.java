package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysRoleMenu;
import org.springframework.stereotype.Repository;

/**
 * 菜单表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysRoleMenuRepository extends BaseRepository<SysRoleMenu> {

    boolean existsByMenuId(String menuId);

}
