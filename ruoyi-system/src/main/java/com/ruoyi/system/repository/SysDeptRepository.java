package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysDept;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 部门表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysDeptRepository extends BaseRepository<SysDept> {

    @Query("select SysDept from SysDept where parentId = :parentId and deptName = :deptName")
    SysDept findByDeptNameUnique(String parentId, String deptName);

    @Query(value = "update sys_dept set parent_all = replace(parent_all, :oldParentAll, :newParentAll) where find_in_set(:deptId, parent_all) > 0", nativeQuery = true)
    @Modifying
    void updateDeptChildren(String deptId, String newParentAll, String oldParentAll);

    boolean existsByParentId(String parentId);

}
