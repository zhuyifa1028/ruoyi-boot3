package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysDept;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysDeptRepository extends BaseRepository<SysDept> {

    @Query("select SysDept from SysDept where parentId = :parentId and deptName = :deptName")
    SysDept findByDeptNameUnique(String parentId, String deptName);

    @Query("update SysDept set parentAll = replace(parentAll, :oldParentAll, :newParentAll) where find_in_set(:deptId, parentAll)")
    @Modifying
    void updateDeptChildren(String deptId, String newParentAll, String oldParentAll);

    boolean existsByParentId(String parentId);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

}
