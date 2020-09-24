package com.pro.mongo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pro.mongo.mapper.MongoMapper;
import com.pro.mongo.service.UserService;
import com.pro.mongo.vo.QuickGuideUserVO2;
import com.pro.mongo.vo.UserProVO;
import com.pro.mongo.vo.UserRoleVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	private final MongoTemplate mongo;
	private final MongoMapper mapper;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public int register(Map<String, Object> params) {
		QuickGuideUserVO2 regUser = new QuickGuideUserVO2();
		regUser.setUser_email((String)params.get("reg_email"));
		regUser.setUser_id((String)params.get("reg_id"));
		regUser.setUser_name((String)params.get("reg_name"));
		
		String encryptPassword = passwordEncoder.encode((String)params.get("reg_pwd"));
		regUser.setUser_pwd(encryptPassword);

		Query query = new Query();
		query.limit(1);
		query.with(Sort.by(Sort.Direction.DESC, "user_no"));
		
		int maxUserNo = ((QuickGuideUserVO2)mongo.findOne(query, QuickGuideUserVO2.class, "users")).getUser_no() + 1;
		regUser.setUser_no(maxUserNo);
		
		UserRoleVO role = new UserRoleVO();
		role.setRole("ROLE_USER");
		List<UserRoleVO> rolesList = new ArrayList<UserRoleVO>(){{ add(role); }};
		regUser.setUser_role(rolesList);
		
	    UserProVO pro = new UserProVO();
	    pro.setPro("MONGO_START");
		List<UserProVO> ProsList = new ArrayList<UserProVO>(){{ add(pro); }};
		regUser.setWith_pro(ProsList);
		
		
		log.debug("{}", mongo.insert(regUser, "users"));
		
		return 0;
	}

}
