package com.bh.intra.plp.dao;

import java.util.Map;

public interface PrinterDao {

    Map<String, Object> validatePrinter(String orgCode, String printer);
}
