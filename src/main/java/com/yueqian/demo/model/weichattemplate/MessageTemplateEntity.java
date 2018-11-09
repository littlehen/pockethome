package com.yueqian.demo.model.weichattemplate;

import javax.persistence.*;

/**
/* Created by wujijin on 2018/8/10.
 */
@Entity
public class MessageTemplateEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private String templateId;
    private String typeName;
    private String firstData;
    private String remarkData;
    private String url;
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getFirstData() {
		return firstData;
	}
	public void setFirstData(String firstData) {
		this.firstData = firstData;
	}
	public String getRemarkData() {
		return remarkData;
	}
	public void setRemarkData(String remarkData) {
		this.remarkData = remarkData;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

   
}
