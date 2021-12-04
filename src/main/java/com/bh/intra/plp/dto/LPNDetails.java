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
public class LPNDetails {
    String lpn;
    String lotNumber;
    String sellByDate;
    String locator;
    int itemQty;
    int itemAvailableQty;
    int itemPickedQty;
    String box;

    public LPNDetails(String lpn, String lotNumber, String sellByDate, String locator, int itemQty, int itemAvailableQty, int itemPickedQty) {
        this.lpn = lpn;
        this.lotNumber = lotNumber;
        this.sellByDate = sellByDate;
        this.locator = locator;
        this.itemQty = itemQty;
        this.itemAvailableQty = itemAvailableQty;
        this.itemPickedQty = itemPickedQty;
    }
}
