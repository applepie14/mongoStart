package com.pro.mongo.service;

import java.util.Map;

public interface UserService {
	int register(Map<String, Object> params);
	int idCheck(String reg_id);
}
