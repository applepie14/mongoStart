package com.pro.mongo.service;

import java.util.List;
import java.util.Map;

import com.mongodb.client.result.DeleteResult;
import com.pro.mongo.vo.GuestbookVO;

public interface GuestbookService {
	List<String> userList(String id);
	List<GuestbookVO> guestbookList(String id);
	int addGuestbook(String sender, Map<String, Object> params);
	Map<String, Object> addGuestbookComments(String sender, Map<String, Object> params);
	DeleteResult deleteGuestbook(String params);
}
