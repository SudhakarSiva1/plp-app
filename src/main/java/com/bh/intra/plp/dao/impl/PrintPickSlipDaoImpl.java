package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PrintPickSlipDao;
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
public class PrintPickSlipDaoImpl implements PrintPickSlipDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public PrintPickSlipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_PRINT_TPL);
    }

    @Override
    public Map<String, Object> printPickSlip(String orgCode, int pickSlip, int pickerId, int userId, String TPL, String container, int palletTare, String printer) {
        log.info("Entering printPickSlip with picker id: {}, oracle user id: {}, org: {}, pickslip: {}, TPL: {}, container: {}, palletTare: {}, printer: {}",
                pickerId, userId, orgCode, pickSlip, TPL, container, palletTare, printer);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pn_picker_id", pickerId)
                .addValue("pn_oracle_user_id", userId)
                .addValue("pc_organization_code", orgCode)
                .addValue("pn_pick_slip_number", pickSlip)
                .addValue("pc_MPL", TPL)
                .addValue("pc_container_type", container)
                .addValue("pn_pallet_tare", palletTare)
                .addValue("pc_label_printer", printer);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Printer result {}", result);
        return result;
    }
}
