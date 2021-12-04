package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PickSlipDao;
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
public class PickSlipDaoImpl implements PickSlipDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public PickSlipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_GET_NEXT_PICKSLIP);
    }

    @Override
    public Map<String, Object> getNextPickSlip(int pickerId, int userId, String orgCode, int queueId) {
        log.info("Entering getNextPickSlip with picker id: {}, oracle user id: {}, org: {} and queue id: {}", pickerId, userId, orgCode, queueId);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pn_picker_id", pickerId)
                .addValue("pn_oracle_user_id", userId)
                .addValue("pc_organization_code", orgCode)
                .addValue("pn_queue_id", queueId);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Next slip result {}", result);
        return result;
    }
}
