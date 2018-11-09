package com.yueqian.demo.dao.weichattemplate;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yueqian.demo.model.weichattemplate.WeichatAccessTokenEntity;

/**
 * Created by wujijin on 2018/8/7.
 */
@CacheConfig(cacheNames = "weichat_config_data")
public interface WeichatAccessTokenRepository extends JpaRepository<WeichatAccessTokenEntity,String> {
    @Cacheable
    WeichatAccessTokenEntity findByAppid(String appid);
}
