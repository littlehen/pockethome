package com.yueqian.demo.dao.amdin;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yueqian.demo.model.admin.User;



public interface UserRepository extends CrudRepository<User,String>{
//	Optional<Administor> findByUcount(String ucount);
//
//	@Modifying
//	@Transactional
//	@Query(nativeQuery = true,value = "delete from administor where uid=?1")
//	Integer deleteByMyId(Integer uid);
//
//	@Modifying
//	@Query(nativeQuery = true,value = "SELECT   *   FROM   administor where role <> 'super' ")  
//	List<Administor> findMyAll();
}
