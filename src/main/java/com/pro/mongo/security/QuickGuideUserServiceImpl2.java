package com.pro.mongo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pro.mongo.mapper.MongoMapper;
import com.pro.mongo.vo.QuickGuideUserVO;
import com.pro.mongo.vo.QuickGuideUserVO2;
import com.pro.mongo.vo.UserRoleVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuickGuideUserServiceImpl2 implements UserDetailsService{
	private final MongoTemplate mongo;
	
	@Override
	public UserDetails loadUserByUsername(String login_id) throws UsernameNotFoundException {				
		// Query
		String[] arrayValue = {"ALL","MONGO_START"};
		Query query = new Query(Criteria.where("user_id").is(login_id));
		query.addCriteria(Criteria.where("with_pro.pro").in(arrayValue));
		
		QuickGuideUserVO2 user = mongo.findOne(query, QuickGuideUserVO2.class, "users");
		log.info("############################## {} ", user);
				
		if(user == null) {
			throw new UsernameNotFoundException(login_id + "is not found.");
		}
		List<UserRoleVO> roles = user.getUser_role();
		/*
		for(int i = 0; i < user.getUser_role().length; i++) {
			roles.add(user.getUser_role()[i].replaceAll("[role:\"{}]", "").trim());
		}
		*/
		QuickGuideUserVO userService = new QuickGuideUserVO();
		userService.setUsername(user.getUser_name());
		userService.setId(user.getUser_id());
		userService.setPassword(user.getUser_pwd());
		userService.setAuthorities(getAuthorities(roles));
		userService.setEnabled(true);
		userService.setAccountNonExpired(true);
		userService.setAccountNonLocked(true);
		userService.setCredentialsNonExpired(true);

		return userService;
	}		

	// 권한테이블 안의 로그인한 사용자의 권한이 정보를 가져온다
    public Collection<GrantedAuthority> getAuthorities(List<UserRoleVO> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(int i = 0; i < roles.size();i++) {
            authorities.add(new SimpleGrantedAuthority(roles.get(i).getRole()));
        }
        return authorities;
    }
}
