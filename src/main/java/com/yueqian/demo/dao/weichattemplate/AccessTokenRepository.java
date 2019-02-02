package com.yueqian.demo.dao.weichattemplate;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.yueqian.demo.model.weichattemplate.AccessToken;

@CacheConfig(cacheNames = "TokenCache")
public interface AccessTokenRepository extends JpaRepository<AccessToken,Integer> {

	@Cacheable
	Optional<AccessToken> findById(Integer id);
	
}
