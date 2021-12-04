package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PickSlipItemDao;
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
public class PickSlipItemDaoImpl implements PickSlipItemDao{

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public PickSlipItemDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PICKING_ITEM_DETAILS);
    }

    @Override
    public Map<String, Object> getItemDetails(int pickerId, int userId, String orgCode, int pickSlip) {
        log.info("Entering getItemDetails with picker id: {}, oracle user id: {}, pickSlip: {} and org: {}", pickerId, userId, pickSlip, orgCode);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pn_picker_id", pickerId)
                .addValue("pn_oracle_user_id", userId)
                .addValue("pc_organization_code", orgCode)
                .addValue("pn_pickslip_number", pickSlip);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Item details result {}", result);
        return result;
    }
}
