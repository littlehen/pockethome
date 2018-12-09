package com.yueqian.demo.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yueqian.demo.dao.amdin.CarouselUrlRepository;
import com.yueqian.demo.model.admin.CarouselUrl;
import com.yueqian.demo.service.admin.CarouselUrlService;
/**
 * 
 * @author 吴佶津  
 * @date 2018年11月26日    
 * @Description 轮播图活动
 */
@RestController
public class CarouselUrlController {
	@Autowired
	CarouselUrlService carouselUrlService;
	
	@Autowired
	CarouselUrlRepository carouselUrlRepository;
	
	//展示轮播图数据
	@RequestMapping("/showData")
	 public Map<String,Object> showData(
	            @RequestParam(required=false,defaultValue="1") int page,
	            @RequestParam(required=false,defaultValue="10") int limit){
		System.out.println("-----"+page);
    	List<CarouselUrl> datas= carouselUrlService.queryAllDataFromTable(page,limit);
        long countx=  carouselUrlService.queryAllCount();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",countx);
        map.put("data",datas);
        System.out.println(map.toString());
        return map;
    }
	

	//添加轮播图
	@RequestMapping("/addhdurl")
	 public Map<String,Object> addhdurl(String imaurl1,String hdurl1,String color){
		CarouselUrl carouselUrl = new CarouselUrl();
		carouselUrl.setHdurl(hdurl1);
		carouselUrl.setImgurl(imaurl1);
		carouselUrl.setColor(color);
		carouselUrl.setIsstart("0");
		CarouselUrl carouselUrl1 = carouselUrlService.saveCarouselUrl(carouselUrl);
		Map<String,Object> map = new HashMap<String,Object>();
		if(carouselUrl1 != null) {
		     map.put("states",true);
		}else {
			map.put("states",false);
		}
		System.out.println(map.toString());
        return map;
    }

	//删除轮播图
	@RequestMapping("/cdelete")
	 public void cdelete(Integer cid){
		carouselUrlService.cdelete(cid);
   }
	
	//编辑轮播图
	@RequestMapping("/edithdurl")
	public Map<String,Object> edithdurl(Integer cid,String imaurl,String hdurl,String color){
		Map<String,Object> map = new HashMap<String,Object>();
		CarouselUrl carouselUrl = new CarouselUrl();
		System.out.println(cid);
		Optional<CarouselUrl> oadministor = carouselUrlRepository.findById(cid);
		carouselUrl = oadministor.get();
		carouselUrl.setHdurl(hdurl);
		carouselUrl.setImgurl(imaurl);
		carouselUrl.setColor(color);
		if(carouselUrlRepository.save(carouselUrl) != null) {
			map.put("states",true);
		}else {
			map.put("states",false);
		}
		return map;
		
	}
}
