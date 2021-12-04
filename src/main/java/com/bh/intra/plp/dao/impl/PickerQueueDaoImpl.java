package com.bh.intra.plp.dao.impl;

import com.bh.intra.plp.dao.PickerQueueDao;
import com.bh.intra.plp.dto.PickerQueue;
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
public class PickerQueueDaoImpl implements PickerQueueDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall simpleJdbcCall;

    public PickerQueueDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void postConstruct() {
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withCatalogName(BhConstants.PICKING_PKG).withProcedureName(BhConstants.PROC_PICKER_QUEUE_LOV)
                .returningResultSet("x_dock_door",
                        BeanPropertyRowMapper.newInstance(PickerQueue.class));
    }

    @Override
    public List<PickerQueue> getAllPickerQueues(String username, String orgCode, String queueId) {
        log.info("Entering getAllPickerQueues() with username={} and org_coe={}", username, orgCode);
        SqlParameterSource in = new MapSqlParameterSource()
            .addValue("pc_user_name", username)
            .addValue("pc_org_cd", orgCode)
            .addValue("pc_queue_id", queueId);
        Map<String, Object> result = simpleJdbcCall.execute(in);
        List<PickerQueue> queues = (List) result.get("x_dock_door");
        log.info("Exiting getAllPickerQueues() with {} results.", queues.size());
        return queues;
    }
}
