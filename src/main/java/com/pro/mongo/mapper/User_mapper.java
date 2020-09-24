package com.pro.mongo.mapper;

import java.util.Map;

public interface User_mapper {

	Map<String, String> login(String login_id, String login_pwd);
	Map<String, String> getLoginUserInfo(String login_id);

}
