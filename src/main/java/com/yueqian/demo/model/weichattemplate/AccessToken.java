package com.yueqian.demo.model.weichattemplate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccessToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	 
    private String accessToken;
 
    private String expiresin;
 
    private Date createdate;

	public AccessToken(Integer id, String accessToken, String expiresin, Date createdate) {
		super();
		this.id = id;
		this.accessToken = accessToken;
		this.expiresin = expiresin;
		this.createdate = createdate;
	}

	public AccessToken() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresin() {
		return expiresin;
	}

	public void setExpiresin(String expiresin) {
		this.expiresin = expiresin;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
    
    

}
