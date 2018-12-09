package com.yueqian.demo.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yueqian.demo.dao.amdin.BabyRepository;
import com.yueqian.demo.model.admin.Baby;


@Service
public class BabyService {
	@Autowired
	BabyRepository babyRepository;
	
	public long queryAllCount() {
		Long number = babyRepository.count();
		return number;
	}

	public List<Baby> queryAllDataFromTable(int page, int limit) {
		page=(page-1)*limit;
		List<Baby> datas = babyRepository.queryAllDataFromTable(page,limit);
		return datas;
	}

	public Baby saveBaby(Baby baby) {
		return babyRepository.save(baby);
	}

	public void bdelete(Integer tid) {
		babyRepository.deleteById(tid);	
	}

}
