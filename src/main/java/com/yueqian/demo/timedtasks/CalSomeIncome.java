package com.yueqian.demo.timedtasks;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yueqian.demo.dao.user.OrderRepository;
import com.yueqian.demo.dao.user.UserInfoRepository;
import com.yueqian.demo.model.user.UserInfo;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月13日    
 * @Description 定时计算可提现余额=账户余额，累计收益。上月预估收入（已结算）。每月25号更新
 */
@Component
public class CalSomeIncome {
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	OrderRepository orderDetailRepository;
	
	@Value("${user.promotionrate}")
	String promotionrate;
	
	private Logger logger = LoggerFactory.getLogger(CalSomeIncome.class);
	
	@Scheduled(cron ="0 0 0 25 * ?")
	public void getSomeIncome() {
		logger.info("定时计算可提现余额=账户余额，累计收益。上月预估收入（已结算）开始啦...............");
		List<UserInfo> userInfoList = userInfoRepository.findAll();
		for(UserInfo userInfo : userInfoList) {
			Long mouthmoneny = orderDetailRepository.findlastmouthmoneny(userInfo.getPid()); //获取自己上月平台已结算状态佣金。用户累加可提现余额=账户余额，累计收益
			Long mymouthmoneny = orderDetailRepository.findmylastmouthmoneny(userInfo.getPid()); //获取自己上月平台确认收货状态佣金，用于上月预估收入（已结算）
			
			List<UserInfo> lowuserIfo = userInfoRepository.findByShareid(userInfo.getUserid());//找到下级
			Long lowlastmouthmoneny = 0L; 
			Long mylowlastmouthmoneny = 0L; 
			Long totallowlastmouthmoneny = 0L;//用来存放所有下级的上月平台已结算状态佣金总和
			Long mytotallowlastmouthmoneny = 0L;//用来存放所有下级的上月平台确认收货状态佣金总和
			for(UserInfo userInfo1 : lowuserIfo) {
			    lowlastmouthmoneny = orderDetailRepository.findlastmouthmoneny(userInfo1.getPid()); //获取下级平台已结算状态佣金。用户累加可提现余额=账户余额，累计收益
			    mylowlastmouthmoneny = orderDetailRepository.findmylastmouthmoneny(userInfo1.getPid()); //获取下级平台确认收货状态佣金，用于上月预估收入（已结算）
			    totallowlastmouthmoneny += lowlastmouthmoneny*Long.valueOf(promotionrate); //下级已结算佣金*20%为自己所有
			    mytotallowlastmouthmoneny += mylowlastmouthmoneny*Long.valueOf(promotionrate); //下级已结算佣金*20%为自己所有
			}
			userInfo.setLastmouthmoney(mylowlastmouthmoneny+mymouthmoneny);//更新上月预估收入（已结算）
			userInfo.setZhanghumoney(totallowlastmouthmoneny+userInfo.getZhanghumoney()+mouthmoneny);//更新可提现余额=账户余额。累加
			userInfo.setLeijimoney(totallowlastmouthmoneny+userInfo.getLeijimoney()+mouthmoneny);//更新累计收益。累加
			userInfoRepository.save(userInfo);
		}
		logger.info("定时计算可提现余额=账户余额，累计收益。上月预估收入（已结算）结束啦...............");
	}
	
}
