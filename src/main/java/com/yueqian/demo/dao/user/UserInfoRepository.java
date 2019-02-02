package com.yueqian.demo.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.yueqian.demo.model.user.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo,Integer>{

	UserInfo findByOpenid(String openId);

	List<UserInfo> findByShareid(Long userid);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT *  FROM   user_info WHERE userid=?1") 
	UserInfo findByMyId(Long shareid);

	UserInfo findByYaoqingma(String yaoqingma);


}
