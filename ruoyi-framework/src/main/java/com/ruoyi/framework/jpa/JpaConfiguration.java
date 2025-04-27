package com.ruoyi.framework.jpa;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruoyi.common.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EntityScan("com.ruoyi.**.entity")
@EnableJpaAuditing
@EnableJpaRepositories("com.ruoyi.**.repository")
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityUtils.getUsername());
    }

    @Bean
    public JPQLQueryFactory jpqlQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
