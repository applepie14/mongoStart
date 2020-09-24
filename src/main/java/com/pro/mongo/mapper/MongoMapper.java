package com.pro.mongo.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pro.mongo.vo.QuickGuideUserVO2;

public interface MongoMapper extends MongoRepository<QuickGuideUserVO2, String>{
	@Query(value = "{ 'user_id' : ?0 }", fields = "{'_id' : 0}")
	QuickGuideUserVO2 getUserPros(String user_id);
}
