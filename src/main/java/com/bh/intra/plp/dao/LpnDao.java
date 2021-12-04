package com.bh.intra.plp.dao;

import java.util.Map;

public interface LpnDao {

    Map<String, Object> validateLPN(String orgCode, int pickSlip, int pickerId, int userId,  String itemNumber, String lpn);
}
