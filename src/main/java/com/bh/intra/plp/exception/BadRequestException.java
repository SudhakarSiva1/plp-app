package com.bh.intra.plp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}