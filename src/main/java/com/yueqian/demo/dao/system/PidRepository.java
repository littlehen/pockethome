package com.yueqian.demo.dao.system;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yueqian.demo.model.system.Pid;


public interface PidRepository  extends CrudRepository<Pid,Long>{

	@Modifying
	@Query(nativeQuery = true,value = "SELECT pddpid FROM pid LIMIT 1")
	Pid getPid();

}
