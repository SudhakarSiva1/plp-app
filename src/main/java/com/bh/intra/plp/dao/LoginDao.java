package com.bh.intra.plp.dao;

import java.util.Map;

public interface LoginDao {
    Map<String, Object> signInUser(String username, String orgCode, int queueId);
}
