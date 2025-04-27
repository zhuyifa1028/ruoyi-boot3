package com.ruoyi.framework.jpa;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EntityScan("com.ruoyi.**.entity")
@EnableJpaAuditing
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityUtils.getUsername());
    }
}
