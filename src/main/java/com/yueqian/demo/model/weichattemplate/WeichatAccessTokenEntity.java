package com.yueqian.demo.model.weichattemplate;

import javax.persistence.*;

/**
 * Created by wujijin on 2018/8/7.
 */
@Entity
public class WeichatAccessTokenEntity {
	
	@Id
    private String appid;
    private String accessToken;
    private Integer expiresIn;
    private String appSecret;
    
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

  
}
