package com.yueqian.demo.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yueqian.demo.dao.amdin.BabyRepository;
import com.yueqian.demo.model.admin.Baby;
import com.yueqian.demo.service.admin.BabyService;


/**
 * 
 * @author 吴佶津  
 * @date 2018年11月26日    
 * @Description 限时抢购 宝贝更换
 */
@RestController
public class BabyController {
		@Autowired
		BabyService babyService;
		
		@Autowired
		BabyRepository babyRepository;
		
		
		//展示宝贝数据
		@RequestMapping("/showBaby")
		 public Map<String,Object> showBaby(
		            @RequestParam(required=false,defaultValue="1") int page,
		            @RequestParam(required=false,defaultValue="10") int limit){
			
	    	List<Baby> datas= babyService.queryAllDataFromTable(page,limit);
	        long countx=  babyService.queryAllCount();
	        Map<String,Object> map = new HashMap<String,Object>();
	        map.put("code",0);
	        map.put("msg","");
	        map.put("count",countx);
	        map.put("data",datas);
	        System.out.println(map.toString());
	        return map;
	    }
		
		//添加宝贝
		@RequestMapping("/addtitle")
		 public Map<String,Object> addtitle(String title1){
			Baby baby = new Baby();
			baby.setTitle(title1);
			Baby Baby1 = babyService.saveBaby(baby);
			Map<String,Object> map = new HashMap<String,Object>();
			if(Baby1 != null) {
			     map.put("states",true);
			}else {
				map.put("states",false);
			}
			System.out.println(map.toString());
	        return map;
	    }

		//删除宝贝
		@RequestMapping("/bdelete")
		 public void bdelete(Integer tid){
			babyService.bdelete(tid);
	   }
		
		//编辑宝贝
		@RequestMapping("/edittitle")
		public Map<String,Object> edittitle(Integer tid,String title){
			Map<String,Object> map = new HashMap<String,Object>();
			Baby baby = new Baby();
			System.out.println(tid);
			Optional<Baby> oadministor = babyRepository.findById(tid);
			baby = oadministor.get();
			baby.setTitle(title);
			if(babyRepository.save(baby) != null) {
				map.put("states",true);
			}else {
				map.put("states",false);
			}
			return map;
			
		}
}
