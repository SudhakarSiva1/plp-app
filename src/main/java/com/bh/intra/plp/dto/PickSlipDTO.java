package com.bh.intra.plp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PickSlipDTO {
    int palletTare;
    String container;
    String printer;
    String org;
    int userId;
    int pickerId;
    String tpl;
    int pickSlipNumber;
    String tolLow;
    String tolHigh;
}
