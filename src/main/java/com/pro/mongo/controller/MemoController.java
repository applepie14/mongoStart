package com.pro.mongo.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
