package com.yueqian.demo.model.system;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月6日    
 * @Description 拼多多订单更新时间
 */
@Entity
public class PddOrderUpdateTime {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long poutid;
	
	private Long pddupdatetime; //记录最新更新的拼多多订单时间，更新的订单到什么时间了
	
	public Long getPddupdatetime() {
		return pddupdatetime;
	}

	public void setPddupdatetime(Long pddupdatetime) {
		this.pddupdatetime = pddupdatetime;
	}

	private Long updatetime; //记录最新更新的系统时间，什么时间去更新的

	public Long getPoutid() {
		return poutid;
	}

	public void setPoutid(Long poutid) {
		this.poutid = poutid;
	}

	public Long getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Long updatetime) {
		this.updatetime = updatetime;
	}
	
	
	
}
