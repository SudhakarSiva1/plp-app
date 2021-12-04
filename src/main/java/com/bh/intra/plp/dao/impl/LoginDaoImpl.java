package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.LoginDao;
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
public class LoginDaoImpl implements LoginDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public LoginDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_SIGN_IN_PICKER);
    }

    @Override
    public Map<String, Object> signInUser(String username, String orgCode, int queueId) {
        log.info("Signing in user with username: {}, org: {} and queue: {}",username, orgCode, queueId);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("pc_user_name", username)
                .addValue("pc_organization_code", orgCode)
                .addValue("pc_operation", "ON")
                .addValue("pn_queue", queueId);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        log.info("Sign-in result {}", result);
        return result;
    }
}
