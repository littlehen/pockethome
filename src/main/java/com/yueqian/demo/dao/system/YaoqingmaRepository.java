package com.yueqian.demo.dao.system;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yueqian.demo.model.system.Yaoqingma;


public interface YaoqingmaRepository extends CrudRepository<Yaoqingma,Long>{

	@Modifying
	@Query(nativeQuery = true,value = "SELECT * FROM yaoqingma LIMIT 1")
	Yaoqingma getYaoqingma();

}
