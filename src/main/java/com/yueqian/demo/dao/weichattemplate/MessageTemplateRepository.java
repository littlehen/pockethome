package com.yueqian.demo.dao.weichattemplate;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yueqian.demo.model.weichattemplate.MessageTemplateEntity;

/**
 * Created by wujijin on 2018/8/10.
 */
@CacheConfig(cacheNames = "message_template_data")
public interface MessageTemplateRepository extends JpaRepository<MessageTemplateEntity,String> {
//    @Cacheable
//    MessageTemplateEntity fingByTemplateId(String templateId);
}
