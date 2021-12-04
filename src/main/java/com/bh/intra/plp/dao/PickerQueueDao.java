package com.bh.intra.plp.dao;

import com.bh.intra.plp.dto.PickerQueue;

import java.util.List;

public interface PickerQueueDao {
    List<PickerQueue> getAllPickerQueues(String username, String orgCode, String queueId);
}
