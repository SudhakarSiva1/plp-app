package com.bh.intra.plp.controller;


import com.bh.intra.plp.dao.LoginDao;
import com.bh.intra.plp.dao.OrganizationDao;
import com.bh.intra.plp.dao.PickSlipDao;
import com.bh.intra.plp.dao.PickerQueueDao;
import com.bh.intra.plp.dto.Organization;
import com.bh.intra.plp.dto.PickerQueue;
import com.bh.intra.plp.exception.BadRequestException;
import com.bh.intra.plp.provider.AuthenticationClient;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin
public class PublicController {

    private final PickerQueueDao pickerQueueDao;

    private final OrganizationDao organizationDao;

    private final AuthenticationClient authenticationClient;

    private final LoginDao loginDao;



    public PublicController(PickerQueueDao pickerQueueDao, OrganizationDao organizationDao, AuthenticationClient authenticationClient, LoginDao loginDao) {
        this.pickerQueueDao = pickerQueueDao;
        this.organizationDao = organizationDao;
        this.authenticationClient = authenticationClient;
        this.loginDao = loginDao;
    }

    @PostMapping(value = "/user/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(@RequestBody MultiValueMap<String, String> params) {
        Map<String, Object> body = new LinkedHashMap<>();
        try {
            if (!params.containsKey("username") || !params.containsKey("password"))
                throw new BadRequestException("username/password is mandatory");
            String details[] = params.get("username").get(0).split("::");
            final Map<String, Object> result = loginDao.signInUser(details[0], details[1], Integer.parseInt(details[2]));
            if (result.get("XC_RETURN_STATUS").equals("E")) {
                body.put("message", result.get("XC_ERROR_MESSAGE").toString());
            } else {
                return authenticationClient.grantToken(params);
            }
        } catch (FeignException e) {
            body.put("message", e.contentUTF8().contains("Bad credentials") ? "Bad credentials" : e.contentUTF8());
        } catch (Exception e) {
            body.put("message", e.getMessage());
        }
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("organizations")
    public List<Organization> getAllQueues() {
        return organizationDao.getAllOrganizations();
    }

    @GetMapping("queues")
    public List<PickerQueue> getAllQueues(@RequestParam("org_code") String orgCode, @RequestParam("username") String username) {
        return pickerQueueDao.getAllPickerQueues(username, orgCode, null);
    }



}
