package com.pro.mongo.vo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("blogs")
public class MemoVO {
	
	@Id
	ObjectId _id;
	
	String objectId;
	String user_id;
	String type;
	String title;
	String img;
	String desc;
}
