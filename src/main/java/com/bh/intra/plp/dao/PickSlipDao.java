package com.bh.intra.plp.dao;

import java.util.Map;

public interface PickSlipDao {

    Map<String, Object> getNextPickSlip(int pickerId, int userId, String orgCode, int queueId);
}
