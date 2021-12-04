package com.bh.intra.plp.dao;

import java.util.Map;

public interface PalletTareDao {

    Map<String, Object> validatePalletTare(String orgCode, String palletTare, String tolLow, String tolHigh);
}
