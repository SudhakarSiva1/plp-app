package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.LpnDao;
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
public class LpnDaoImpl implements LpnDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public LpnDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_VALIDATE_LPN);
    }

    @Override
    public Map<String, Object> validateLPN(String orgCode, int pickSlip, int pickerId, int userId, String itemNumber, String lpn) {
        log.info("Entering validateLPN with picker id: {}, oracle user id: {}, org: {}, pickslip: {}, itemNumber: {}, lpn: {},", pickerId, userId, orgCode, pickSlip, itemNumber, lpn);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pn_picker_id", pickerId)
                .addValue("pn_oracle_user_id", userId)
                .addValue("pc_organization_code", orgCode)
                .addValue("pn_pickslip_number", pickSlip)
                .addValue("pc_item_number", itemNumber)
                .addValue("pc_LPN_scanned", lpn);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("LPN validation result {}", result);
        return result;
    }

}
