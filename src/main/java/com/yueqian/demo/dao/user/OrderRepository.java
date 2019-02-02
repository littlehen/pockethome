package com.yueqian.demo.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.yueqian.demo.model.user.OrderDetail;



public interface OrderRepository extends JpaRepository<OrderDetail,Long>{

	@Modifying
	@Query(nativeQuery = true,value = "SELECT * FROM   order_detail WHERE ordersn = ?1") 
	OrderDetail fingByOrdersn(String ordersn);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount),COUNT(*)  FROM   order_detail WHERE TO_DAYS(FROM_UNIXTIME(ordercreatetime)) = TO_DAYS(NOW()) AND orderstatus='0' AND pid=?1") 
	List<Object[]> findtodaymoneny(String pid);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount),COUNT(*) FROM order_detail WHERE DATE_FORMAT( FROM_UNIXTIME(ordercreatetime), '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND orderstatus='0' AND pid=?1") 
	List<Object[]> findmouthmoneny(String pid);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount) FROM order_detail WHERE DATE_FORMAT( FROM_UNIXTIME(ordercreatetime), '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' ) AND orderstatus='0' AND pid=?1") 
	Long findmymouthmoneny(String pid);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount)  FROM   order_detail WHERE TO_DAYS(FROM_UNIXTIME(ordercreatetime)) = TO_DAYS(NOW()) AND orderstatus='0' AND pid=?1") 
	Long findmytodaymoneny(String pid);
	
	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount) FROM order_detail WHERE PERIOD_DIFF( DATE_FORMAT( NOW( ) , '%Y%m' ) , DATE_FORMAT( FROM_UNIXTIME(ordercreatetime), '%Y%m' ) ) =1 AND orderstatus='5' AND pid=?1") 
	Long findlastmouthmoneny(String pid);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT SUM(promotionamount) FROM order_detail WHERE PERIOD_DIFF( DATE_FORMAT( NOW( ) , '%Y%m' ) , DATE_FORMAT( FROM_UNIXTIME(ordercreatetime), '%Y%m' ) ) =1 AND orderstatus='2' AND pid=?1") 
	Long findmylastmouthmoneny(String pid);


}
