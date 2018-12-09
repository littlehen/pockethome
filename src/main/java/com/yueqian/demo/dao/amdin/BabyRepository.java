package com.yueqian.demo.dao.amdin;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yueqian.demo.model.admin.Baby;


public interface BabyRepository extends CrudRepository<Baby,Integer>{

	@Modifying
	@Query(nativeQuery = true,value = "SELECT   *   FROM   Baby limit ?1,?2 ") 
	List<Baby> queryAllDataFromTable(int page, int limit);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT   *   FROM   Baby") 
	List<Baby> findAllList();

}
