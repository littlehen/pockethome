package com.yueqian.demo.dao.amdin;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.yueqian.demo.model.admin.CarouselUrl;


public interface CarouselUrlRepository extends CrudRepository<CarouselUrl,Integer>{

	@Modifying
	@Query(nativeQuery = true,value = "SELECT   *   FROM   carousel_url limit ?1,?2 ") 
	List<CarouselUrl> queryAllDataFromTable(int page, int limit);

	@Modifying
	@Query(nativeQuery = true,value = "SELECT   *   FROM   carousel_url") 
	List<CarouselUrl> findAllList();

}
