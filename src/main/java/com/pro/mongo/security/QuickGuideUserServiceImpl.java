package com.pro.mongo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pro.mongo.mapper.User_mapper;
import com.pro.mongo.vo.QuickGuideUserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuickGuideUserServiceImpl implements UserDetailsService{
	private final User_mapper dao;
	private final MongoTemplate mongo;

	@Override
	public UserDetails loadUserByUsername(String login_id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Map<String, String> user = dao.getLoginUserInfo(login_id);
		if(user == null) {
			throw new UsernameNotFoundException(login_id + "is not found.");
		}
		QuickGuideUserVO userService = new QuickGuideUserVO();
		userService.setUsername(user.get("user_name"));
		userService.setId(user.get("user_id"));
		userService.setPassword(user.get("user_password"));
		userService.setAuthorities(getAuthorities(user.get("user_role")));
		userService.setEnabled(true);
		userService.setAccountNonExpired(true);
		userService.setAccountNonLocked(true);
		userService.setCredentialsNonExpired(true);

		// TODO Auto-generated method stub

		return userService;
	}		

	// 권한테이블 안의 로그인한 사용자의 권한이 정보를 가져온다
    public Collection<GrantedAuthority> getAuthorities(String role) {
        List<String> authList = new ArrayList<String>();
        authList.add(role);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : authList) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }
}
