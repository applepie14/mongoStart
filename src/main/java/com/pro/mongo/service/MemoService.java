package com.pro.mongo.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.result.DeleteResult;

public interface MemoService {
	Map<String, Object> getMyMemo(String login_id);
	int insertMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception ;
	int updateMemo(String login_id, MultipartFile memoPhoto, Map<String, Object> params) throws Exception ;
	DeleteResult deleteMemo(String login_id, String params) throws Exception ;
}
