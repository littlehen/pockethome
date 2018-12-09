package com.yueqian.demo.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yueqian.demo.service.admin.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping("/login")
	public Map<String,Object> login(String username,String password,HttpSession session){
		System.out.println(username+"--------");
		Map<String,Object> ishave = userService.checkUser(username,password,session);
		//System.out.println(EncryptPassword.encryptBasedDes("czfadmin"));
		return ishave;
	}
}
