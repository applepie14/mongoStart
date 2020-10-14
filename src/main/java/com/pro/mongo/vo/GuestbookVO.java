package com.pro.mongo.vo;


import java.security.Timestamp;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="guestbook")
public class GuestbookVO {
	
	@Id
	private ObjectId _id;

	String objectId;
	private String owner;
	private String send_user;
	private String send_content;
	private String send_dttm;
	
	private List<GuestbookVO> comments;
}
