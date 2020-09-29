package com.pro.mongo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pro.mongo.service.MemoService;
import com.pro.mongo.vo.QuickGuideUserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
@Slf4j
public class MemoController {
	private final MemoService memo;

	@PostMapping("/getMyMemo")
	Map<String, Object> getMyMemo(@AuthenticationPrincipal QuickGuideUserVO user){
		return memo.getMyMemo(user.getId());
	}
	
	@PostMapping("/insert")
	int insertMemo(
		@AuthenticationPrincipal QuickGuideUserVO user, 
		@RequestParam Map<String, Object> params,
		MultipartFile memoPhoto
	) throws Exception {
		return memo.insertMemo(user.getId(), memoPhoto, params);
	}
	@PostMapping("/update")
	int updateMemo(
		@AuthenticationPrincipal QuickGuideUserVO user, 
		@RequestParam Map<String, Object> params,
		MultipartFile memoPhoto
	) throws Exception {
		return memo.updateMemo(user.getId(), memoPhoto, params);
	}
	
	@DeleteMapping("/delete/{params}")
	Map<String, Object> deleteMemo(
		@AuthenticationPrincipal QuickGuideUserVO user, 
		@PathVariable String params,
		HttpServletResponse response
	) throws Exception {
		Map<String, Object> result = new HashMap<>();

		try {
			if (memo.deleteMemo(user.getId(), params).getDeletedCount() > 0) {
				result.put("code", 204);
				result.put("result", "DELETE");
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
