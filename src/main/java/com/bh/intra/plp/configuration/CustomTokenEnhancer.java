package com.bh.intra.plp.configuration;


import com.bh.intra.plp.dto.CustomUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
        info.put("user_id", user.getId());
        info.put("picker_id", user.getPickerId());
        info.put("username", user.getUsername());
        info.put("queue_id", user.getQueueId());
        info.put("org_code", user.getOrgCode());
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);
        return super.enhance(customAccessToken, authentication);
    }
}
