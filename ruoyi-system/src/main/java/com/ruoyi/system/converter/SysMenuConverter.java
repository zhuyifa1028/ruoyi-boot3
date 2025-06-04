package com.ruoyi.system.converter;

import com.ruoyi.system.dto.SysMenuInsertDTO;
import com.ruoyi.system.dto.SysMenuUpdateDTO;
import com.ruoyi.system.entity.SysMenu;
import com.ruoyi.system.vo.SysMenuVO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuConverter {

    SysMenu toSysMenu(SysMenuInsertDTO item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toSysMenu(SysMenuUpdateDTO item, @MappingTarget SysMenu info);

    SysMenuVO toSysMenuVO(SysMenu item);

    List<SysMenuVO> toSysMenuVO(Iterable<SysMenu> list);

}
