package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PrinterDao;
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
public class PrinterDaoImpl implements PrinterDao {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    public PrinterDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_VALIDATE_PRINTER);
    }

    @Override
    public Map<String, Object> validatePrinter(String orgCode, String printer) {
        log.info("Entering validatePrinter with org code: {}, printer : {}", orgCode, printer);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pc_organization_code", orgCode)
                .addValue("pc_label_printer", printer);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Printer validation result {}", result);
        return result;
    }

}
