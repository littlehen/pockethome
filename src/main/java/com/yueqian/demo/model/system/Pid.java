package com.yueqian.demo.model.system;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月24日    
 * @Description 拼多多pid
 */
@Entity
public class Pid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mypid;
	
	private String pddpid;

	public Long getMypid() {
		return mypid;
	}

	public void setMypid(Long mypid) {
		this.mypid = mypid;
	}

	public String getPddpid() {
		return pddpid;
	}

	public void setPddpid(String pddpid) {
		this.pddpid = pddpid;
	}
	
	
}
