package com.yueqian.demo.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yueqian.demo.service.user.UserInfoService;

@RestController
public class UserInfoController {
	
	@Autowired
	UserInfoService userInfoService;
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月11日    
	 * @Description 绑定填写手机号
	 * @param phone
	 * @param openid
	 * @return
	 */
	@RequestMapping("/addUserPhone")
	public Map<String,Object> addUserPhone(String phone,String openid) {
		return userInfoService.addUserPhone(phone,openid);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月11日    
	 * @Description 获取验证码
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/yanzheng")
	public Map<String,Object> yanzheng(String mobile){
		return userInfoService.yanzhengma(mobile);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月11日    
	 * @Description 获取本月佣金和付款订单数，今日佣金和付款订单数
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getpromotionmoney")
	public Map<String,Object> getpromotionmoney(String openId){
		return userInfoService.getpromotionmoney(openId);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月13日    
	 * @Description 获取本月佣金,今日佣金和团队人数
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getmypromotionmoney")
	public Map<String,Object> getmypromotionmoney(String openId){
		return userInfoService.getmypromotionmoney(openId);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月13日    
	 * @Description 获取可提现余额 
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getketixian")
	public Map<String,Object> getketixian(String openId){
		return userInfoService.getketixian(openId);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月13日    
	 * @Description 获取每月25号更新的账号余额，累计收益，上月预估收入（已结算） 
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getmouthupdate")
	public Map<String,Object> getmouthupdate(String openId){
		return userInfoService.getmouthupdate(openId);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月14日    
	 * @Description 获取邀请码，邀请人昵称和团队店长数
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getxianguang")
	public Map<String,Object> getxianguang(String openId){
		return userInfoService.getxianguang(openId);
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2019年1月24日    
	 * @Description 输入邀请码，确认之后的操作
	 * @param openId
	 * @param yaoqingma
	 * @return
	 */
	@RequestMapping("/yaoqing")
	public Map<String, String> yaoqing(String openId,String yaoqingma){
		return userInfoService.yaoqing(openId,yaoqingma);
	}
	
}
