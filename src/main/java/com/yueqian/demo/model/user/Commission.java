package com.yueqian.demo.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月1日    
 * @Description 佣金比例
 */
@Entity
public class Commission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer comid;
	
	private String comtype;
	
	private double proportion;//比例

	public Integer getComid() {
		return comid;
	}

	public void setComid(Integer comid) {
		this.comid = comid;
	}

	public String getComtype() {
		return comtype;
	}

	public void setComtype(String comtype) {
		this.comtype = comtype;
	}

	public double getProportion() {
		return proportion;
	}

	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	
	

}
