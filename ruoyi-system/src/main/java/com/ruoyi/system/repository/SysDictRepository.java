package com.ruoyi.system.repository;

import com.ruoyi.framework.jpa.repository.BaseRepository;
import com.ruoyi.system.entity.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
@Repository
public interface SysDictRepository extends BaseRepository<SysDict> {

    List<SysDict> findByDictType(String dictType);

    List<SysDict> findByStatus(Character status);

    SysDict findByDictTypeAndDictValue(String dictType, String dictValue);

    SysDict findByDictTypeAndDictLabel(String dictType, String dictLabel);
}
