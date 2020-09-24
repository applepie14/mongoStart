package com.pro.mongo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pro.mongo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserService ser;
	
	@PostMapping("/reg")
	public void reg(String reg_id,String reg_name,String reg_email,String reg_pwd,HttpServletResponse response) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("reg_id", reg_id);
		params.put("reg_name", reg_name);
		params.put("reg_email", reg_email);
		params.put("reg_pwd", reg_pwd);
		
		log.info("############################## {} ", params);
		ser.register(params);
		response.sendRedirect("/login");
	}

	@PostMapping("/idCheck/{reg_id}")
	public int idCheck(@PathVariable String reg_id) {
		
		return -1;
	}
	
}
