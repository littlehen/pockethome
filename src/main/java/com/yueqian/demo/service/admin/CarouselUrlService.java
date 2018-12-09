package com.yueqian.demo.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yueqian.demo.dao.amdin.CarouselUrlRepository;
import com.yueqian.demo.model.admin.CarouselUrl;


@Service
public class CarouselUrlService {
	@Autowired
	CarouselUrlRepository carouselUrlRepository;

	public long queryAllCount() {
		Long number = carouselUrlRepository.count();
		return number;
	}

	public List<CarouselUrl> queryAllDataFromTable(int page, int limit) {
		page=(page-1)*limit;
		List<CarouselUrl> datas = carouselUrlRepository.queryAllDataFromTable(page,limit);
		return datas;
	}

	public CarouselUrl saveCarouselUrl(CarouselUrl carouselUrl) { 
		return carouselUrlRepository.save(carouselUrl);
	
		
	}

	public void cdelete(Integer cid) {
		carouselUrlRepository.deleteById(cid);
	}
}
