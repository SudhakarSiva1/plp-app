package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.OrganizationDao;
import com.bh.intra.plp.dto.Organization;
import com.bh.intra.plp.utils.BhConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class OrganizationDaoImpl implements OrganizationDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public OrganizationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(BhConstants.PICKING_PKG)
                .withProcedureName(BhConstants.PROC_ORG_CODE_LOV)
                .returningResultSet("x_org_code",
                        BeanPropertyRowMapper.newInstance(Organization.class));
    }

    @Override
    public List<Organization> getAllOrganizations() {
        log.info("Entering getAllOrganizations()");
        Map<String, Object> result = simpleJdbcCall.execute(new HashMap<>(0));
        List<Organization> organizations = (List) result.get("x_org_code");
        log.info("Exiting getAllOrganizations() with {} results.", organizations.size());
        return organizations;
    }
}
