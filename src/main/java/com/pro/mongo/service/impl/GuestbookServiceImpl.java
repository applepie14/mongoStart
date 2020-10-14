package com.pro.mongo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.pro.mongo.service.GuestbookService;
import com.pro.mongo.vo.GuestbookVO;
import com.pro.mongo.vo.MongoUserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {
	private final MongoTemplate mongo;
	
	@Override
	public List<String> userList(String id) {
		Query query = new Query(Criteria.where("user_id").ne(id));
		List<Map> userList = mongo.find(query, Map.class, "users");

		List<String> result = new ArrayList<String>();
		result.add(id);
		for(Map user : userList) {
			result.add((String)user.get("user_id"));
		}
		return result;
	}
	
	@Override
	public List<GuestbookVO> guestbookList(String id) {
		Query query = new Query(Criteria.where("owner").is(id));
		query.with(Sort.by(Sort.Direction.DESC, "_id"));
		
		List<GuestbookVO> result = mongo.find(query ,GuestbookVO.class, "guestbook");

		for(GuestbookVO guestbook : result) {
			guestbook.setObjectId((guestbook.get_id()).toString());
		}
		return result;
	}
	
	@Override
	public int addGuestbook(String sender, Map<String, Object> params) {
		GuestbookVO vo = new GuestbookVO();
		vo.set_id(new ObjectId());
		vo.setObjectId(vo.get_id().toString());
		vo.setOwner((String) params.get("owner"));
		vo.setSend_user(sender);
		vo.setSend_content((String) params.get("send_content"));
		vo.setSend_dttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		GuestbookVO result = mongo.insert(vo, "guestbook");
		return result.getObjectId().length();
	}
	@Override
	public Map<String, Object> addGuestbookComments(String sender, Map<String, Object> params) {
		params.put("send_user", sender);
		params.put("send_dttm", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		Query query = new Query(Criteria.where("_id").is(new ObjectId( (String) params.get("objid") )));

		params.put("objid", new ObjectId().toString());
		Update update = new Update().addToSet("comments", params);
		
		GuestbookVO result = mongo.findAndModify(query, update, GuestbookVO.class, "guestbook");
		params.put("result", result);
		return params;
	}
}
