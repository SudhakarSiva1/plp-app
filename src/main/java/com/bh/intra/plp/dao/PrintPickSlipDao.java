package com.bh.intra.plp.dao;

import java.util.Map;

public interface PrintPickSlipDao {

    Map<String, Object> printPickSlip(String orgCode, int pickSlip, int pickerId, int userId,  String TPL, String container, int palletTare, String printer);
}
