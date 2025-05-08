package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysDeptInsertDTO;
import com.ruoyi.system.dto.SysDeptUpdateDTO;
import com.ruoyi.system.entity.SysDept;
import com.ruoyi.system.vo.SysDeptVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDeptConverter {

    SysDept toSysDept(SysDeptInsertDTO item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSysDept(SysDeptUpdateDTO item, @MappingTarget SysDept info);

    SysDeptVO toSysDeptVO(SysDept item);

    List<SysDeptVO> toSysDeptVO(List<SysDept> list);

}
