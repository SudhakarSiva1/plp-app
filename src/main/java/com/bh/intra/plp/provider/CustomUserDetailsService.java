package com.bh.intra.plp.provider;

import com.bh.intra.plp.dao.LoginDao;
import com.bh.intra.plp.dto.CustomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String details[] = username.split("::");
        final Map<String, Object> result = loginDao.signInUser(details[0], details[1], Integer.parseInt(details[2]));
        if (result == null || !result.containsKey("XC_RETURN_STATUS")) {
            throw new RuntimeException("Something went wrong!!!");
        } else if (result.get("XC_RETURN_STATUS").equals("S")) {
            String pwd = null;
            if (result.get("XC_USER_PASSWORD") != null) pwd = result.get("XC_USER_PASSWORD").toString();
            return new CustomUser(
                    new BigDecimal(result.get("XN_ORACLE_USER_ID").toString()).intValue(),
                    new BigDecimal(result.get("XN_PICKER_ID").toString()).intValue(), details[1], Integer.parseInt(details[2]), details[0], pwd);
        } else {
            throw new UsernameNotFoundException(result.get("XC_ERROR_MESSAGE").toString());
        }
    }

}
