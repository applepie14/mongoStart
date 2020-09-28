package com.pro.mongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.pro.mongo.security.MyLoginSeccessHandler;
import com.pro.mongo.security.QuickGuideUserServiceImpl2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final QuickGuideUserServiceImpl2 authImpl;

	private static final String[] RESOURCE_LOCATIONS = {
		"classpath:/static/"
	};	


	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
        		.antMatchers("/favicon.ico")
				.antMatchers("/js/**")
				.antMatchers("/google/**")
				.antMatchers("/lib/**")
				.antMatchers("/css/**")
				.antMatchers("/image/**")
				.antMatchers("/mongoboard/**")
				.antMatchers("/member/**")
				.antMatchers("/error");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();		// 개발 시 에만

		/*
		http
			.headers().frameOptions().sameOrigin()
		.and()
			.authorizeRequests()
			.antMatchers("/","/*").permitAll() // USER OR ADMIN
		.and()
			.formLogin()
				.loginPage("/")
				//.loginPage("/login")
				.loginProcessingUrl("/loginUser")
				.usernameParameter("login_id")
				.passwordParameter("login_pwd")
				.successHandler(new MyLoginSeccessHandler())
		.and() // 로그아웃 설정
            .logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID") // 쿠키삭제?
			.permitAll()
		.and()
			//.exceptionHandling().accessDeniedPage("/accessDenied")
			;
		*/
		http
			.headers().frameOptions().sameOrigin()
		.and()
			.authorizeRequests()
			//.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/login**").permitAll() // ALL, ALSO ANONYMOUS USER
			.antMatchers("/admin", "/admin/*").hasRole("ADMIN") // 
			.antMatchers("/","/*").authenticated() // USER OR ADMIN
		.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/loginUser")
				.usernameParameter("login_id")
				.passwordParameter("login_pwd")
				.successHandler(new MyLoginSeccessHandler())
		.and() // 로그아웃 설정
            .logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID") // 쿠키삭제?
			.permitAll()
		.and()
			//.exceptionHandling().accessDeniedPage("/accessDenied")
			;
	}


	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authImpl);
    }

	@Bean
	PasswordEncoder passwordEncoder(){
	    return new BCryptPasswordEncoder();
	}
}
