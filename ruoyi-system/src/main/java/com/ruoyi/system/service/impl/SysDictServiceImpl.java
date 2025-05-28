package com.ruoyi.system.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.converter.SysDictConverter;
import com.ruoyi.system.dto.SysDictInsertDTO;
import com.ruoyi.system.dto.SysDictUpdateDTO;
import com.ruoyi.system.entity.SysDict;
import com.ruoyi.system.query.SysDictQuery;
import com.ruoyi.system.repository.SysDictRepository;
import com.ruoyi.system.service.SysDictService;
import com.ruoyi.system.vo.SysDictVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruoyi.system.entity.QSysDict.sysDict;

/**
 * 字典管理 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private SysDictConverter sysDictConverter;
    @Resource
    private SysDictRepository sysDictRepository;

    /**
     * 根据条件分页查询字典数据
     */
    @Override
    public Page<SysDictVO> selectDictList(SysDictQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getDictType())) {
            predicate.and(sysDict.dictType.contains(query.getDictType()));
        }
        if (StringUtils.isNotBlank(query.getDictValue())) {
            predicate.and(sysDict.dictValue.contains(query.getDictValue()));
        }
        if (StringUtils.isNotBlank(query.getDictLabel())) {
            predicate.and(sysDict.dictLabel.contains(query.getDictLabel()));
        }
        if (ObjectUtils.allNotNull(query.getStatus())) {
            predicate.and(sysDict.status.eq(query.getStatus()));
        }

        Pageable pageable = QPageRequest.of(query.getPageNumber(), query.getPageSize(), sysDict.createTime.desc());

        Page<SysDict> page = sysDictRepository.findAll(predicate, pageable);

        return page.map(sysDictConverter::toSysDictVO);
    }

    /**
     * 根据字典类型查询
     */
    @Override
    public List<SysDictVO> selectDictListByType(String dictType) {
        List<SysDict> dictCache = redisCache.getCacheObject(CacheConstants.SYS_DICT_KEY + dictType);
        if (ObjectUtils.allNotNull(dictCache)) {
            return sysDictConverter.toSysDictVO(dictCache);
        }

        List<SysDict> dictList = sysDictRepository.findByDictType(dictType);
        if (ObjectUtils.allNotNull(dictList)) {
            redisCache.setCacheObject(CacheConstants.SYS_DICT_KEY + dictType, dictList);
            return sysDictConverter.toSysDictVO(dictCache);
        }

        return Collections.emptyList();
    }

    /**
     * 查询字典信息
     */
    @Override
    public SysDictVO selectDictById(String dictId) {
        SysDict info = sysDictRepository.getReferenceById(dictId);
        if (ObjectUtils.allNull(info)) {
            throw new ServiceException("部门信息不存在");
        }

        return sysDictConverter.toSysDictVO(info);
    }

    /**
     * 新增字典
     */
    @Override
    public void insertDict(@Valid SysDictInsertDTO dto) {
        SysDict dict = sysDictConverter.toSysDict(dto);
        checkDictUnique(dict);
        dict.markNew();
        sysDictRepository.save(dict);

        List<SysDict> dictList = sysDictRepository.findByDictType(dto.getDictType());
        redisCache.setCacheObject(CacheConstants.SYS_DICT_KEY + dto.getDictType(), dictList);
    }

    private void checkDictUnique(SysDict dict) {
        SysDict check;

        check = sysDictRepository.findByDictTypeAndDictValue(dict.getDictType(), dict.getDictValue());
        if (ObjectUtils.allNotNull(check) && ObjectUtils.notEqual(check.getDictId(), dict.getDictId())) {
            throw new ServiceException("操作失败，字典值已存在");
        }

        check = sysDictRepository.findByDictTypeAndDictLabel(dict.getDictType(), dict.getDictLabel());
        if (ObjectUtils.allNotNull(check) && ObjectUtils.notEqual(check.getDictId(), dict.getDictId())) {
            throw new ServiceException("操作失败，字典标签已存在");
        }
    }

    /**
     * 修改字典
     */
    @Override
    public void updateDict(@Valid SysDictUpdateDTO dto) {
        SysDict dict = sysDictRepository.getReferenceById(dto.getDictId());
        if (ObjectUtils.allNull(dict)) {
            throw new ServiceException("字典信息不存在");
        }

        String oldType = dict.getDictType();

        sysDictConverter.toSysDict(dto, dict);
        checkDictUnique(dict);
        sysDictRepository.saveAndFlush(dict);

        if (ObjectUtils.notEqual(oldType, dto.getDictType())) {
            List<SysDict> dictList = sysDictRepository.findByDictType(oldType);
            redisCache.setCacheObject(CacheConstants.SYS_DICT_KEY + oldType, dictList);
        }

        List<SysDict> dictList = sysDictRepository.findByDictType(dto.getDictType());
        redisCache.setCacheObject(CacheConstants.SYS_DICT_KEY + dto.getDictType(), dictList);
    }

    /**
     * 批量删除字典
     */
    @Override
    public void deleteDictByIds(List<String> dictIds) {
        List<SysDict> dictList = sysDictRepository.findAllById(dictIds);

        List<String> cacheKeys = Lists.newArrayList();
        for (SysDict dict : dictList) {
            cacheKeys.add(CacheConstants.SYS_DICT_KEY + dict.getDictType());
        }

        sysDictRepository.deleteAll(dictList);
        redisCache.deleteObject(cacheKeys);
    }

    /**
     * 刷新字典缓存
     */
    @PostConstruct
    @Override
    public void refreshDictCache() {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_DICT_KEY + "*");
        redisCache.deleteObject(keys);

        List<SysDict> dictList = sysDictRepository.findByStatus('0');
        Map<String, List<SysDict>> listMap = dictList.stream().collect(Collectors.groupingBy(SysDict::getDictType));

        listMap.forEach((key, value) -> redisCache.setCacheObject(CacheConstants.SYS_DICT_KEY + key, value));
    }

}
