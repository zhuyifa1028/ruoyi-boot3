package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.dto.SysDictInsertDTO;
import com.ruoyi.system.dto.SysDictUpdateDTO;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.service.SysDictService;
import com.ruoyi.system.vo.SysDictVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "【系统管理】字典管理")
@Validated
@RestController
@RequestMapping("/system/dict")
public class SysDictController extends BaseController {

    @Resource
    private SysDictService sysDictService;

    @Operation(summary = "根据条件分页查询字典列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public R<List<SysDictVO>> selectDictList(SysDictQuery query) {
        return R.ok(sysDictService.selectDictList(query));
    }

    @Operation(summary = "根据字典类型查询")
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictVO>> dictType(@PathVariable String dictType) {
        return R.ok(sysDictService.selectDictListByType(dictType));
    }

    @Operation(summary = "查询字典信息")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public R<SysDictVO> selectDictById(@PathVariable String dictId) {
        return R.ok(sysDictService.selectDictById(dictId));
    }

    @Operation(summary = "新增字典")
    @Log(title = "字典管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping
    public R<Void> insertDict(@RequestBody @Valid SysDictInsertDTO dto) {
        sysDictService.insertDict(dto);
        return R.ok();
    }

    @Operation(summary = "修改字典")
    @Log(title = "字典管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public R<Void> updateDict(@RequestBody @Valid SysDictUpdateDTO dto) {
        sysDictService.updateDict(dto);
        return R.ok();
    }

    @Operation(summary = "批量删除字典")
    @Log(title = "字典管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/{dictIds}")
    public R<Void> remove(@PathVariable List<String> dictIds) {
        sysDictService.deleteDictByIds(dictIds);
        return R.ok();
    }

    @Operation(summary = "刷新字典缓存")
    @Log(title = "字典管理", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @DeleteMapping("/refreshCache")
    public R<Void> refreshDictCache() {
        sysDictService.refreshDictCache();
        return R.ok();
    }

}
