package com.yueqian.demo.model.system;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月24日    
 * @Description 邀请码
 */
@Entity
public class Yaoqingma {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long yid;
	
	private String yqm;

	public Long getYid() {
		return yid;
	}

	public void setYid(Long yid) {
		this.yid = yid;
	}

	public String getYqm() {
		return yqm;
	}

	public void setYqm(String yqm) {
		this.yqm = yqm;
	} 
	
	
}
