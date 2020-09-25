package com.pro.mongo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.pro.mongo.service.MemoService;
import com.pro.mongo.vo.MemoVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
	private final MongoTemplate mongo;

	@Override
	public Map<String, Object> getMyMemo(String login_id) {
		Query query = new Query(Criteria.where("user_id").is(login_id));

		List<MemoVO> memos = mongo.find(query, MemoVO.class, "blogs");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("memos", memos);
		
		return result;
	}

}
