package com.yueqian.demo.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;
	
	private String openid;
	
	private String nickname;
	
	private String headimgurl;
	
	private String phone;
	
	private String city;//微信用户所在城市
	
	private String province;//微信用户所在省份
	
	private String sex;
	
	private String yaoqingma;
	
	private Long shareid;//邀请人id
	
	private Integer comid;//佣金比例表id
	
	private double teammoney;
	
	private double personalmoney;
	
	private String pid;//推广位ID
	
	private String status;//状态0:只有openId,没有手机号。 1:有手机号，没有邀请码。2:有邀请码，即是店长
	
	private Long zhanghumoney;//账户余额（=可提现余额）
	
	private Long lastmouthmoney;//上月预估收入（我们自己是已结算，平台是确认收货）
	
	private Long leijimoney;//累计收益
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getYaoqingma() {
		return yaoqingma;
	}

	public void setYaoqingma(String yaoqingma) {
		this.yaoqingma = yaoqingma;
	}

	public Long getShareid() {
		return shareid;
	}

	public void setShareid(Long shareid) {
		this.shareid = shareid;
	}

	public Integer getComid() {
		return comid;
	}

	public void setComid(Integer comid) {
		this.comid = comid;
	}

	public double getTeammoney() {
		return teammoney;
	}

	public void setTeammoney(double teammoney) {
		this.teammoney = teammoney;
	}

	public double getPersonalmoney() {
		return personalmoney;
	}

	public void setPersonalmoney(double personalmoney) {
		this.personalmoney = personalmoney;
	}

	public Long getZhanghumoney() {
		return zhanghumoney;
	}

	public void setZhanghumoney(Long zhanghumoney) {
		this.zhanghumoney = zhanghumoney;
	}

	public Long getLastmouthmoney() {
		return lastmouthmoney;
	}

	public void setLastmouthmoney(Long lastmouthmoney) {
		this.lastmouthmoney = lastmouthmoney;
	}

	public Long getLeijimoney() {
		return leijimoney;
	}

	public void setLeijimoney(Long leijimoney) {
		this.leijimoney = leijimoney;
	}
	
}
