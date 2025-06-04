package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysMenu;
import org.springframework.stereotype.Repository;

/**
 * 角色和菜单关联表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysMenuRepository extends BaseRepository<SysMenu> {

    SysMenu findByMenuNameAndParentId(String menuName, String parentId);

    boolean existsByParentId(String parentId);

}
