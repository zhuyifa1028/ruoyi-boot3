package com.ruoyi.system.service;

import com.ruoyi.system.dto.SysDictInsertDTO;
import com.ruoyi.system.dto.SysDictUpdateDTO;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.vo.SysDictVO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 字典管理 业务层
 *
 * @author ruoyi
 */
public interface SysDictService {

    /**
     * 根据条件分页查询字典列表
     */
    Page<SysDictVO> selectDictList(SysDictQuery query);

    /**
     * 根据字典类型查询
     */
    List<SysDictVO> selectDictListByType(String dictType);

    /**
     * 查询字典信息
     */
    SysDictVO selectDictById(String dictId);

    /**
     * 新增字典
     */
    void insertDict(@Valid SysDictInsertDTO dto);

    /**
     * 修改字典
     */
    void updateDict(@Valid SysDictUpdateDTO dto);

    /**
     * 批量删除字典
     */
    void deleteDictByIds(List<String> dictIds);

    /**
     * 刷新字典缓存
     */
    void refreshDictCache();

}
