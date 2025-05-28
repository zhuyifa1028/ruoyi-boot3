package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysDictInsertDTO;
import com.ruoyi.system.dto.SysDictUpdateDTO;
import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.vo.SysDictVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictConverter {

    SysDict toSysDict(SysDictInsertDTO item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSysDict(SysDictUpdateDTO item, @MappingTarget SysDict info);

    SysDictVO toSysDictVO(SysDict item);

    List<SysDictVO> toSysDictVO(List<SysDict> list);

}
