package com.yueqian.demo.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.yueqian.demo.controller.PinDuoDuoController;
import com.yueqian.demo.dao.system.PidRepository;
import com.yueqian.demo.dao.system.YaoqingmaRepository;
import com.yueqian.demo.dao.user.OrderRepository;
import com.yueqian.demo.dao.user.UserInfoRepository;
import com.yueqian.demo.model.system.Pid;
import com.yueqian.demo.model.system.Yaoqingma;
import com.yueqian.demo.model.user.OrderDetail;
import com.yueqian.demo.model.user.UserInfo;

import net.sf.json.util.JSONUtils;

@Service
public class UserInfoService {
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	OrderRepository orderDetailRepository;
	
	@Autowired
	PidRepository pidRepository;
	
	@Autowired
	YaoqingmaRepository yaoqingmaRepository;
	
	@Value("${user.promotionrate}")
	String promotionrate;

	public Map<String, Object> addUserPhone(String phone, String openid) {
		UserInfo userInfo = userInfoRepository.findByOpenid(openid);
		userInfo.setPhone(phone);
		userInfo.setStatus("1");
		Map<String,Object> map = new HashMap<>();
		if(userInfoRepository.save(userInfo) != null) {
			map.put("state", true);
			map.put("url", "/login.html");
		}else 
			map.put("state", false);
		return map;
	}

	public Map<String, Object> yanzhengma(String mobile) {
		Map<String,Object> map = new HashMap<>();
		int random=(int)(Math.random()*10000);
	//	String a = Api.Sendinfo(mobile, random);
		System.out.println(random);
	//	System.out.println(a);
		map.put("yzm", random);
		return map;
	}

	public Map<String, Object> getpromotionmoney(String openId) {
		double lowamounttodaymoneny = 0; //用户下级今日佣金
		double lowamountmouthmoneny = 0; //用户下级本月佣金
		
		double amounttodaymoneny = 0L; //用户总的今日佣金（包括下级）
		double amountmouthmoneny = 0L; //用户总的本月佣金（包括下级）
		
		Integer todaycount = 0; //用户总的今日订单数（包括下级）
		Integer mouthcount = 0;//用户总的本月订单数（包括下级）
		
		Integer lowamounttodaycount = 0; //用户下级今日订单数
		Integer lowamountmouthcount = 0; //用户下级本月订单数
		
		
		UserInfo userIfo = userInfoRepository.findByOpenid(openId);
		List<Object[]> todaymoneny = orderDetailRepository.findtodaymoneny(userIfo.getPid()); //获取自己今日佣金和订单数
		List<Object[]> mouthmoneny = orderDetailRepository.findmouthmoneny(userIfo.getPid()); //获取自己本月佣金和订单数
		
		Long selftodaymoneny = (Long.valueOf(todaymoneny.get(0)[0].toString()));//用户个人今日佣金
		Long selfmouthmoneny = (Long.valueOf(mouthmoneny.get(0)[0].toString()));//用户个人本月佣金
		
		Long selftodaycount = (Long.valueOf(todaymoneny.get(0)[1].toString()));//用户个人今日订单数
		Long selfmouthcount = (Long.valueOf(mouthmoneny.get(0)[1].toString()));//用户个人本月订单数
		
		
		List<UserInfo> lowuserIfo = userInfoRepository.findByShareid(userIfo.getUserid());//找到下级
		
		List<Object[]> lowtodaymoneny = null;
		List<Object[]> lowmouthmoneny = null;
		for(UserInfo userInfo1 : lowuserIfo) {
		    lowtodaymoneny = orderDetailRepository.findtodaymoneny(userInfo1.getPid()); //获取下级今日佣金和订单数
			lowmouthmoneny = orderDetailRepository.findmouthmoneny(userInfo1.getPid()); //获取下级本月佣金和订单数
			
			lowamounttodaymoneny +=  (Double)lowtodaymoneny.get(0)[0]*Double.valueOf(promotionrate);
			lowamountmouthmoneny +=  (Double)lowmouthmoneny.get(0)[0]*Double.valueOf(promotionrate);
			lowamounttodaycount +=  (Integer)lowtodaymoneny.get(0)[1];
			lowamountmouthcount +=  (Integer)lowtodaymoneny.get(0)[1];
		}
		
		
//		amounttodaymoneny = selftodaymoneny + lowamounttodaymoneny;
		amountmouthmoneny = selfmouthmoneny + lowamountmouthmoneny;
//		todaycount = (Integer)todaymoneny.get(0)[1] + lowamounttodaycount;
//		mouthcount = (Integer)mouthmoneny.get(0)[1] + lowamountmouthcount;
		
		Map<String, Object> map = new HashMap<>();
//		map.put("amounttodaymoneny", amounttodaymoneny);//用户总的今日佣金（包括下级）
		map.put("amountmouthmoneny", amountmouthmoneny);//用户总的本月佣金（包括下级）
//		
//		map.put("todaycount", todaycount);//用户总的今日订单数（包括下级）
//		map.put("mouthcount", mouthcount);//用户总的本月订单数（包括下级）
		
		map.put("selftodaymoneny",selftodaymoneny);//用户个人今日佣金
		map.put("selfmouthmoneny", selfmouthmoneny);//用户个人本月佣金
		
		map.put("lowamounttodaymoneny", lowamounttodaymoneny);//用户下级今日佣金
		map.put("lowamountmouthmoneny", lowamountmouthmoneny);//用户下级本月佣金
		
		map.put("lowamounttodaycount", lowamounttodaycount);//用户下级今日订单数
		map.put("lowamountmouthcount", lowamountmouthcount);//用户下级本月订单数
		
		map.put("selftodaycount", selftodaycount);//用户个人今日订单数
		map.put("selfmouthcount", selfmouthcount);//用户个人本月订单数
		return map;
	}

	public Map<String, Object> getmypromotionmoney(String openId) {
		double lowamounttodaymoneny = 0; //用户下级今日佣金
		double lowamountmouthmoneny = 0; //用户下级本月佣金
		
		double amounttodaymoneny = 0; //用户总的今日佣金（包括下级）
		double amountmouthmoneny = 0; //用户总的本月佣金（包括下级）		
		
		UserInfo userIfo = userInfoRepository.findByOpenid(openId);
		
		Long todaymoneny = orderDetailRepository.findmytodaymoneny(userIfo.getPid()); //获取自己今日佣金
		Long mouthmoneny = orderDetailRepository.findmymouthmoneny(userIfo.getPid()); //获取自己本月佣金
		
		List<UserInfo> lowuserIfo = userInfoRepository.findByShareid(userIfo.getUserid());//找到下级
		
		Long lowtodaymoneny = null;
		Long lowmouthmoneny = null;
		for(UserInfo userInfo1 : lowuserIfo) {
		    lowtodaymoneny = orderDetailRepository.findmytodaymoneny(userInfo1.getPid()); //获取下级今日佣金
			lowmouthmoneny = orderDetailRepository.findmymouthmoneny(userInfo1.getPid()); //获取下级本月佣金
			
			lowamounttodaymoneny +=  Double.valueOf(lowtodaymoneny)*Double.valueOf(promotionrate);
			lowamountmouthmoneny +=  Double.valueOf(lowmouthmoneny)*Double.valueOf(promotionrate);
		}
		
		amounttodaymoneny = Double.valueOf(todaymoneny) + lowamounttodaymoneny;
		amountmouthmoneny = Double.valueOf(mouthmoneny) + lowamountmouthmoneny;
		
		Map<String, Object> map = new HashMap<>();
		map.put("amounttodaymoneny", amounttodaymoneny);//用户总的今日佣金（包括下级）
		map.put("amountmouthmoneny", amountmouthmoneny);//用户总的本月佣金（包括下级）
		
		map.put("teamnumber", lowuserIfo.size());//团队成员人数
		
		return map;
	}

	public Map<String, Object> getketixian(String openId) {
		UserInfo userIfo = userInfoRepository.findByOpenid(openId);
		Map<String, Object> map = new HashMap<>();
		map.put("ketixian", userIfo.getZhanghumoney());//可提现余额
		return map;
	}

	public Map<String, Object> getmouthupdate(String openId) {
		UserInfo userIfo = userInfoRepository.findByOpenid(openId);
		Map<String, Object> map = new HashMap<>();
		map.put("zhanghumoney", userIfo.getZhanghumoney());//账户余额
		map.put("lastmouthmoney", userIfo.getLastmouthmoney());//上月预估收入（已结算）
		map.put("leijimoney", userIfo.getLeijimoney());//累计收益
		return map;
	}

	public Map<String, Object> getxianguang(String openId) {
		UserInfo userIfo = userInfoRepository.findByOpenid(openId);
		UserInfo userIfo1 = userInfoRepository.findByMyId(userIfo.getShareid());
		List<UserInfo> lowuserIfo = userInfoRepository.findByShareid(userIfo.getUserid());//找到下级
		Map<String, Object> map = new HashMap<>();
		map.put("nickname", userIfo1.getNickname());//邀请人昵称
		map.put("teamnumber", lowuserIfo.size());//团队店长人数
		map.put("yaoqingma", userIfo.getYaoqingma());//邀请码
		return map;
	}

	@Transactional
	public Map<String, String> yaoqing(String openId, String yaoqingma) {
		Map<String, String> map = new HashMap<>();
		UserInfo userIfo = userInfoRepository.findByYaoqingma(yaoqingma);
		if(userIfo != null) {
			UserInfo userIfo1 = userInfoRepository.findByOpenid(openId);
			if(userIfo1 != null) {
				PinDuoDuoController pinDuoDuoController = new PinDuoDuoController();
				userIfo1.setShareid(userIfo.getUserid()); //设置当前用户的分享人为拥有这个邀请码的用户。
				Pid mpid = pidRepository.getPid(); //从数据库里取出已经存好的拼多多pid，都是取第一条，取一条删一条
				pidRepository.deleteById(mpid.getMypid());  //取一条删一条
				Yaoqingma yaoqingma1 = yaoqingmaRepository.getYaoqingma(); //从数据库里取出已经存好的邀请码，都是取第一条，取一条删一条
				yaoqingmaRepository.deleteById(yaoqingma1.getYid());  //取一条删一条
				userIfo1.setStatus("2");
				userIfo1.setPid(mpid.getPddpid()); //设置当前用户pid
				userIfo1.setYaoqingma(yaoqingma1.getYqm()); //设置当前用户邀请码
				userInfoRepository.save(userIfo1);
				map.put("state", "true");
			}else {
				map.put("state", "false");
			}
		}else {
			map.put("state", "noyaoqingma");
		}
		
		return map;
	}
}
