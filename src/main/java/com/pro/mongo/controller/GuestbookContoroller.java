package com.pro.mongo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pro.mongo.service.GuestbookService;
import com.pro.mongo.vo.GuestbookVO;
import com.pro.mongo.vo.QuickGuideUserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/guestbook")
@RequiredArgsConstructor
@Slf4j
public class GuestbookContoroller {
	private final GuestbookService ser;
	
	@PostMapping("/user-list")
	public Map<String, Object> userList(
		@AuthenticationPrincipal QuickGuideUserVO user,
		HttpServletResponse response
	){
		Map<String, Object> result = new HashMap<>();
		List<String> userIdResult = ser.userList(user.getId());

		try {
			if (userIdResult.size() >  0) {
				result.put("code", 204);
				result.put("result", "UPDATE");
				result.put("userList", userIdResult);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("code", 500);
			result.put("result", "FAILURE");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	@PostMapping("/guestbook-list")
	public Map<String, Object> guestbookList(
		String userId,
		@AuthenticationPrincipal QuickGuideUserVO user, 
		HttpServletResponse response
	){
		if(userId == null) userId = user.getId(); // 다른 사용자 방명록도 불러올 수 있게 id값 받아오는데, 초기값은 null 즉 본인의 방명록 가져오기

		Map<String, Object> result = new HashMap<>();
		List<GuestbookVO> guestbookResult = ser.guestbookList(userId);

		try {
			if (guestbookResult.size() >  0) {
				result.put("code", 204);
				result.put("result", "SELECT");
				result.put("guestbookResult", guestbookResult);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("code", 500);
			result.put("result", "FAILURE");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	@PostMapping("/addGuestbook")
	public Map<String, Object> addGuestbook(
		@AuthenticationPrincipal QuickGuideUserVO user, 
		@RequestParam Map<String, Object> params,
		HttpServletResponse response
	){
		Map<String, Object> result = new HashMap<>();
		int addGuestbookResult = ser.addGuestbook(user.getId(), params);

		try {
			if (addGuestbookResult >  0) {
				result.put("code", 204);
				result.put("result", "INSERT");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("code", 500);
			result.put("result", "FAILURE");
			result.put("msg", e.getMessage());
		}
		return result;
	}
	@PostMapping("/addGuestbookComments")
	public Map<String, Object> addGuestbookComments(
		@AuthenticationPrincipal QuickGuideUserVO user, 
		@RequestParam Map<String, Object> params,
		HttpServletResponse response
	){
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> addGuestbookCommentResult = ser.addGuestbookComments(user.getId(), params);

		try {
			if( ((GuestbookVO) addGuestbookCommentResult.get("result")).get_id().toString().length() >  0) {
				result.put("code", 204);
				result.put("result", "INSERT");
				result.put("addGuestbookCommentResult", addGuestbookCommentResult);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("code", 500);
			result.put("result", "FAILURE");
			result.put("msg", e.getMessage());
		}
		return result;
	}
}
