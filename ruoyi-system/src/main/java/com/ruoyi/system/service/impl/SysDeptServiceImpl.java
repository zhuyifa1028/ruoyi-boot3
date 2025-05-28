package com.ruoyi.system.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TreeSelectUtils;
import com.ruoyi.system.converter.SysDeptConverter;
import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.query.SysDeptQuery;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.service.SysDeptService;
import com.ruoyi.system.vo.SysDeptVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ruoyi.system.entity.QSysDept.sysDept;
import static com.ruoyi.system.entity.QSysRoleDept.sysRoleDept;

/**
 * 部门管理 业务层实现
 *
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptConverter sysDeptConverter;
    @Resource
    private SysDeptRepository sysDeptRepository;

    @Resource
    private JPQLQueryFactory jpqlQueryFactory;

    /**
     * 根据条件查询部门列表
     *
     * @param query 部门信息
     * @return 部门信息集合
     */
    @Override
    public List<SysDeptVO> selectDeptList(SysDeptQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getDeptName())) {
            predicate.and(sysDept.deptName.contains(query.getDeptName()));
        }
        if (StringUtils.isNotBlank(query.getStatus())) {
            predicate.and(sysDept.status.eq(query.getStatus()));
        }
        if (StringUtils.isNotBlank(query.getDeptId())) {
            predicate.and(sysDept.deptId.eq(query.getDeptId()));
        }
        predicate.and(sysRoleDept.roleId.in(SecurityUtils.getLoginUser().getUser().getRoleIds()));

        List<SysDept> fetch = jpqlQueryFactory.selectFrom(sysDept)
                .leftJoin(sysRoleDept).on(sysRoleDept.deptId.eq(sysDept.deptId))
                .where(predicate)
                .orderBy(sysDept.parentId.asc(), sysDept.orderNum.asc())
                .fetch();

        return sysDeptConverter.toSysDeptVO(fetch);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @Override
    public List<SysDeptVO> selectDeptExclude(String deptId) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(sysRoleDept.roleId.in(SecurityUtils.getLoginUser().getUser().getRoleIds()));
        if (StringUtils.isNotBlank(deptId)) {
            predicate.and(sysDept.deptId.ne(deptId).or(sysDept.parentAll.notLike(StringUtils.wrapLike(deptId))));
        }

        List<SysDept> fetch = jpqlQueryFactory.selectFrom(sysDept)
                .leftJoin(sysRoleDept).on(sysRoleDept.deptId.eq(sysDept.deptId))
                .where(predicate)
                .orderBy(sysDept.parentId.asc(), sysDept.orderNum.asc())
                .fetch();

        return sysDeptConverter.toSysDeptVO(fetch);
    }

    /**
     * 查询部门信息
     */
    @Override
    public SysDeptVO selectDeptById(String deptId) {
        SysDept info = sysDeptRepository.getReferenceById(deptId);
        if (ObjectUtils.allNull(info)) {
            throw new ServiceException("部门信息不存在，请检查部门ID【{}】", deptId);
        }

        return sysDeptConverter.toSysDeptVO(info);
    }

    /**
     * 新增部门
     */
    @Override
    public void insertDept(SysDeptInsertDTO dto) {
        SysDept parent = sysDeptRepository.getReferenceById(dto.getParentId());
        if (ObjectUtils.allNull(parent)) {
            throw new ServiceException("新增部门【{}】失败，上级部门不存在", dto.getDeptName());
        }
        if (ObjectUtils.notEqual(parent.getStatus(), UserConstants.DEPT_NORMAL)) {
            throw new ServiceException("上级部门停用，不允许新增");
        }

        SysDept unique = sysDeptRepository.findByDeptNameUnique(dto.getParentId(), dto.getDeptName());
        if (ObjectUtils.allNotNull(unique)) {
            throw new ServiceException("新增部门【{}】失败，部门名称已存在", dto.getDeptName());
        }

        SysDept entity = sysDeptConverter.toSysDept(dto);
        entity.markNew();
        entity.setParentAll(parent.getParentAll() + "," + parent.getDeptId());
        sysDeptRepository.save(entity);
    }

    /**
     * 修改部门
     */
    @Override
    public void updateDept(SysDeptUpdateDTO dto) {
        SysDept dept = sysDeptRepository.getReferenceById(dto.getDeptId());
        if (ObjectUtils.allNull(dept)) {
            throw new ServiceException("部门信息不存在，请检查部门ID【{}】", dto.getDeptId());
        }
        if (StringUtils.equals(dto.getDeptId(), dto.getParentId())) {
            throw new ServiceException("修改部门【{}】失败，上级部门不能是自己", dto.getDeptName());
        }

        SysDept parent = sysDeptRepository.getReferenceById(dto.getParentId());
        if (ObjectUtils.allNull(parent)) {
            throw new ServiceException("修改部门【{}】失败，上级部门不存在", dto.getDeptName());
        }

        SysDept unique = sysDeptRepository.findByDeptNameUnique(dto.getParentId(), dto.getDeptName());
        if (ObjectUtils.allNotNull(unique) && ObjectUtils.notEqual(unique.getDeptId(), dto.getDeptId())) {
            throw new ServiceException("修改部门【{}】失败，部门名称已存在", dto.getDeptName());
        }

        String newAncestors = parent.getParentAll() + "," + parent.getDeptId();
        String oldAncestors = dept.getParentAll();
        sysDeptRepository.updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);

        sysDeptConverter.toSysDept(dto, dept);
        dept.setParentAll(newAncestors);
        sysDeptRepository.save(dept);
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     */
    @Override
    public void deleteDeptById(String deptId) {
        if (sysDeptRepository.existsByParentId(deptId)) {
            throw new ServiceException("存在下级部门，不允许删除");
        }
        //if (sysDeptService.checkDeptExistUser(deptId)) {
        //    warn("部门存在用户,不允许删除");
        //    return;
        //}
        sysDeptRepository.deleteById(deptId);
    }

    /**
     * 查询部门树结构信息
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDeptQuery query) {
        List<SysDeptVO> depts = this.selectDeptList(query);
        return TreeSelectUtils.build(depts, item -> {
            TreeSelect ts = new TreeSelect();
            ts.setId(item.getDeptId());
            ts.setParentId(item.getParentId());
            ts.setLabel(item.getDeptName());
            ts.setDisabled(StringUtils.equals(UserConstants.DEPT_DISABLE, query.getStatus()));
            return ts;
        });
    }

    /**
     * 根据角色ID查询部门树信息
     */
    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        return Collections.emptyList();
    }

    /**
     * 校验部门是否有数据权限
     */
    @Override
    public void checkDeptDataScope(String deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(deptId)) {
            SysDeptQuery dept = new SysDeptQuery();
            dept.setDeptId(deptId);
            List<SysDeptVO> depts = this.selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

}
