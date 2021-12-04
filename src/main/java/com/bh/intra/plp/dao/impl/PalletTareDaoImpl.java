package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PalletTareDao;
import com.bh.intra.plp.utils.BhConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
@Slf4j
public class PalletTareDaoImpl implements PalletTareDao {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    public PalletTareDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_VALIDATE_PALLET_TARE);
    }

    @Override
    public Map<String, Object> validatePalletTare(String orgCode, String palletTare, String tolLow, String tolHigh) {
        log.info("Entering validatePalletTare with org code: {}, palletTare : {}, tolLow: {}, tolHigh: {}", orgCode, palletTare, tolLow, tolHigh);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pc_organization_code", orgCode)
                .addValue("pc_pallet_tare", palletTare)
                .addValue("pc_low_tolerance", tolLow)
                .addValue("pc_high_tolerance", tolHigh);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Pallet tare validation result {}", result);
        return result;
    }

}
