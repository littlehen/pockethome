package com.yueqian.demo.service.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yueqian.demo.dao.amdin.UserRepository;
import com.yueqian.demo.model.admin.User;
import com.yueqian.demo.utils.EncryptPassword;


@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	public Map<String,Object> checkUser(String username, String password,HttpSession session){
		System.out.println(password+"--------");
		Optional<User> user = null;
		user = userRepository.findById(username);
		Map<String,Object> map = new HashMap<>();
		if(user.isPresent() && user != null) {
			if(EncryptPassword.decryptBasedDes(user.get().getUpassword()).equals(password)) {
				//map.put("role", user.get().getRole());
				map.put("states", true);
				map.put("username", user.get().getUname());
				session.setAttribute("ucount", username);
				return map;
			}
		}
		map.put("states", false);
		return map;
	}
}
