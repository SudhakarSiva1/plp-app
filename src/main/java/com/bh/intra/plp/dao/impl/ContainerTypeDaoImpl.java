package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.ContainerTypeDao;
import com.bh.intra.plp.dto.ContainerType;
import com.bh.intra.plp.utils.BhConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ContainerTypeDaoImpl implements ContainerTypeDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public ContainerTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName(BhConstants.PICKING_PKG)
                .withProcedureName(BhConstants.PROC_VALIDATE_CONTAINER)
                .returningResultSet("x_container", BeanPropertyRowMapper.newInstance(ContainerType.class));;
    }

    @Override
    public List<ContainerType> getAllContainers() {
        log.info("Entering getAllContainers()");
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("PC_CONTAINER_TYPE", "%");
        Map<String, Object> result = simpleJdbcCall.execute(in);
        List<ContainerType> containers = (List) result.get("x_container");
        log.info("Exiting getAllContainers() with {} results.", containers.size());
        return containers;
    }
}
