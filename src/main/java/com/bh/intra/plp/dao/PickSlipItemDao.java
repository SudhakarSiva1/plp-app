package com.bh.intra.plp.dao;

import java.util.Map;

public interface PickSlipItemDao {
    Map<String, Object> getItemDetails(int pickerId, int userId, String orgCode, int pickSlip);
}
