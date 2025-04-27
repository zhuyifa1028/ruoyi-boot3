package com.ruoyi.system.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.converter.SysConfigConverter;
import com.ruoyi.system.dto.SysConfigInsertDTO;
import com.ruoyi.system.dto.SysConfigUpdateDTO;
import com.ruoyi.system.entity.SysConfig;
import com.ruoyi.system.query.SysConfigQuery;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.SysConfigService;
import com.ruoyi.system.vo.SysConfigVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static com.ruoyi.system.entity.QSysConfig.sysConfig;

/**
 * 配置管理 业务层实现
 *
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Resource
    private SysConfigConverter sysConfigConverter;
    @Resource
    private SysConfigRepository sysConfigRepository;

    @Resource
    private RedisCache redisCache;

    /**
     * 根据条件分页查询配置列表
     */
    @Override
    public Page<SysConfigVO> selectConfigList(SysConfigQuery query) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotBlank(query.getConfigName())) {
            predicate.and(sysConfig.configName.contains(query.getConfigName()));
        }
        if (StringUtils.isNotBlank(query.getConfigKey())) {
            predicate.and(sysConfig.configKey.contains(query.getConfigKey()));
        }
        if (StringUtils.isNotBlank(query.getConfigType())) {
            predicate.and(sysConfig.configType.eq(query.getConfigType()));
        }
        if (ObjectUtils.allNotNull(query.getStartTime())) {
            predicate.and(sysConfig.createTime.goe(LocalDateTime.of(query.getStartTime(), LocalTime.MIN)));
        }
        if (ObjectUtils.allNotNull(query.getEndTime())) {
            predicate.and(sysConfig.createTime.loe(LocalDateTime.of(query.getEndTime(), LocalTime.MAX)));
        }

        Pageable pageable = QPageRequest.of(query.getPageNumber(), query.getPageSize(), sysConfig.createTime.desc());

        Page<SysConfig> page = sysConfigRepository.findAll(predicate, pageable);

        return page.map(sysConfigConverter::toSysConfigVO);
    }

    /**
     * 查询配置信息
     */
    @DataSource(DataSourceType.MASTER)
    @Override
    public SysConfigVO selectConfigById(String configId) {
        SysConfig info = sysConfigRepository.getReferenceById(configId);
        if (ObjectUtils.allNull(info)) {
            throw new ServiceException("配置信息不存在，请检查配置ID【{}】", configId);
        }

        return sysConfigConverter.toSysConfigVO(info);
    }

    /**
     * 查询配置值
     */
    @Override
    public String selectConfigValue(String configKey) {
        String configValue = redisCache.getCacheObject(CacheConstants.SYS_CONFIG_KEY + configKey);
        if (ObjectUtils.allNull(configValue)) {
            return configValue;
        }

        SysConfig info = sysConfigRepository.findByConfigKey(configKey);
        if (ObjectUtils.allNotNull(info)) {
            redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + configKey, info.getConfigValue());
            return info.getConfigValue();
        }

        return StringUtils.EMPTY;
    }

    /**
     * 查询验证码开关
     */
    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigValue("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return false;
        }
        return BooleanUtils.toBoolean(captchaEnabled);
    }

    /**
     * 新增配置
     */
    @Override
    public void insertConfig(SysConfigInsertDTO dto) {
        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjectUtils.allNotNull(check)) {
            throw new ServiceException("新增配置【{}】失败，配置键名已存在", dto.getConfigKey());
        }

        SysConfig entity = sysConfigConverter.toSysConfig(dto);
        entity.markNew();
        entity.setConfigType("N");
        sysConfigRepository.save(entity);

        redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + dto.getConfigKey(), dto.getConfigValue());
    }

    /**
     * 修改配置
     */
    @Override
    public void updateConfig(SysConfigUpdateDTO dto) {
        SysConfig info = sysConfigRepository.getReferenceById(dto.getConfigId());
        if (ObjectUtils.allNull(info)) {
            throw new ServiceException("配置信息不存在，请检查配置ID【{}】", dto.getConfigId());
        }
        if (StringUtils.equals(UserConstants.YES, info.getConfigType())) {
            throw new ServiceException("内置配置【{}】不能修改", info.getConfigKey());
        }

        SysConfig check = sysConfigRepository.findByConfigKey(dto.getConfigKey());
        if (ObjectUtils.allNotNull(check) && ObjectUtils.notEqual(check.getConfigId(), dto.getConfigId())) {
            throw new ServiceException("修改配置【{}】失败，配置键名已存在", dto.getConfigKey());
        }

        sysConfigConverter.toSysConfig(dto, info);
        sysConfigRepository.saveAndFlush(info);

        if (ObjectUtils.notEqual(info.getConfigKey(), dto.getConfigKey())) {
            redisCache.deleteObject(CacheConstants.SYS_CONFIG_KEY + info.getConfigKey());
        }
        redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + dto.getConfigKey(), dto.getConfigValue());
    }

    /**
     * 批量删除配置
     */
    @Override
    public void deleteConfigByIds(List<String> configIds) {
        List<SysConfig> configList = sysConfigRepository.findAllById(configIds);

        List<String> cacheKeys = Lists.newArrayList();
        for (SysConfig config : configList) {
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException("内置配置【{}】不能删除", config.getConfigKey());
            }
            cacheKeys.add(CacheConstants.SYS_CONFIG_KEY + config.getConfigKey());
        }

        sysConfigRepository.deleteAll(configList);
        redisCache.deleteObject(cacheKeys);
    }

    /**
     * 刷新配置缓存
     */
    @PostConstruct
    @Override
    public void refreshConfigCache() {
        Collection<String> cacheKeys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(cacheKeys);

        List<SysConfig> configList = sysConfigRepository.findAll();
        for (SysConfig config : configList) {
            redisCache.setCacheObject(CacheConstants.SYS_CONFIG_KEY + config.getConfigKey(), config.getConfigValue());
        }
    }

}
