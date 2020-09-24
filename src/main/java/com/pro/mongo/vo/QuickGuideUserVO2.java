package com.pro.mongo.vo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="users")
@Data
public class QuickGuideUserVO2 {

	private String user_name;
	private int user_no;
	private String user_id;
	private String user_email;
    private String user_pwd;
    private List<UserRoleVO> user_role;
    private List<UserProVO> with_pro;
    
}

