package com.pro.mongo.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pro.mongo.vo.QuickGuideUserVO2;

public interface MongoMapper extends MongoRepository<QuickGuideUserVO2, String>{
	
	@Query("db.users.find().sort({'user_no' : -1}).limit(1)")
	int getMaxUserNo();
}
