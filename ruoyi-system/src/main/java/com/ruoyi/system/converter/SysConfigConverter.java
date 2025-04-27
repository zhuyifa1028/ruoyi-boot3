package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.vo.SysConfigVO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysConfigConverter {

    SysConfig toSysConfig(SysConfigInsertDTO item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSysConfig(SysConfigUpdateDTO item, @MappingTarget SysConfig info);

    SysConfigVO toSysConfigVO(SysConfig item);

}
