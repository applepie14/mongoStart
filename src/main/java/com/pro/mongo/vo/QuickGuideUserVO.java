package com.pro.mongo.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class QuickGuideUserVO implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122010914145260476L;
	private String username;
	private String id;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
}

