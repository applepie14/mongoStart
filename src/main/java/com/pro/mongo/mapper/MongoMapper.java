package com.pro.mongo.mapper;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pro.mongo.vo.MongoUserVO;

public interface MongoMapper extends MongoRepository<MongoUserVO, String>{
	
	@Query("db.users.find().sort({'user_no' : -1}).limit(1)")
	int getMaxUserNo();
}
