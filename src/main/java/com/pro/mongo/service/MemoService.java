package com.pro.mongo.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface MemoService {
	Map<String, Object> getMyMemo(String login_id);
	int insertMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception ;
}
