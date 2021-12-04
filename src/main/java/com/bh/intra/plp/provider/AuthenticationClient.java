package com.bh.intra.plp.provider;

import com.bh.intra.plp.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "auth-service", url = "${entry.point}", configuration = FeignClientConfiguration.class)
public interface AuthenticationClient {
    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> grantToken(MultiValueMap params);
}
