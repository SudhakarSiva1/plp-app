package com.bh.intra.plp.controller;

import com.bh.intra.plp.dao.*;
import com.bh.intra.plp.dto.ContainerType;
import com.bh.intra.plp.dto.PickSlipDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api")
@Slf4j
public class ApiController {

    private final PickerQueueDao pickerQueueDao;

    private final PalletTareDao palletTareDao;

    private final PrinterDao printerDao;

    private final OrganizationDao organizationDao;

    private final PickSlipDao pickSlipDao;

    private final PrintPickSlipDao printPickSlipDao;

    private final ContainerTypeDao containerTypeDao;

    private final PickSlipItemDao pickSlipItemDao;

    private final LpnDao lpnDao;

    @Autowired
    private HttpServletRequest request;

    public ApiController(PickerQueueDao pickerQueueDao, PalletTareDao palletTareDao, PrinterDao printerDao, OrganizationDao organizationDao, PickSlipDao pickSlipDao, PrintPickSlipDao printPickSlipDao, ContainerTypeDao containerTypeDao, PickSlipItemDao pickSlipItemDao, LpnDao lpnDao) {
        this.pickerQueueDao = pickerQueueDao;
        this.palletTareDao = palletTareDao;
        this.printerDao = printerDao;
        this.organizationDao = organizationDao;
        this.pickSlipDao = pickSlipDao;
        this.printPickSlipDao = printPickSlipDao;
        this.containerTypeDao = containerTypeDao;
        this.pickSlipItemDao = pickSlipItemDao;
        this.lpnDao = lpnDao;
    }


//    @GetMapping("/validatePallet")
//    public Map validatePallet(@RequestParam("palletTare") String palletTare, @RequestParam("container") String container) {
//        SessionUser su = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        String[] selected = container.split("::");
//        log.info("Selected container {}", selected[3]);
//        return palletTareDao.validatePalletTare(su.getOrgCode(), String.valueOf(palletTare), selected[1], selected[2]);
//    }
//
//    @GetMapping("/validatePrinter")
//    public Map validatePrinter(@RequestParam("printer") String printer) {
//        SessionUser su = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        return printerDao.validatePrinter(su.getOrgCode(), printer);
//    }
//
//    @GetMapping("/validateLPN")
//    public Map validateLPN(@RequestParam("lpn") String lpn) {
//        SessionUser user = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        return lpnDao.validateLPN(user.getOrgCode(), user.getPickSlipNumber(), user.getPickerId(), user.getUserId(), user.getItem().getNumber(), lpn);
//    }

    @GetMapping("nextpickslip")
    public Map getNextSlip(
            @RequestParam(name = "picker_id") int pickerId,
            @RequestParam(name = "user_id") int userId,
            @RequestParam(name = "org_code") String orgCode,
            @RequestParam(name = "queue_id") int queueId
    ) {
        return pickSlipDao.getNextPickSlip(pickerId, userId, orgCode, queueId);
    }

    @GetMapping("containers")
    public List<ContainerType> getContainers() {
        return containerTypeDao.getAllContainers();
    }

    @GetMapping("validatePallet")
    public Map validatePallet(@RequestParam String org, @RequestParam String pallet, @RequestParam String tolLow, @RequestParam String tolHigh) {
        return palletTareDao.validatePalletTare(org, pallet, tolLow, tolHigh);
    }

    @GetMapping("validatePrinter")
    public Map validatePrinter(@RequestParam String org, @RequestParam String printer) {
        return printerDao.validatePrinter(org, printer);
    }

    @PostMapping("printPickSlip")
    public Map printPickSlip(@RequestBody PickSlipDTO pickSlip) {
        Map<String, Object> result = palletTareDao.validatePalletTare(pickSlip.getOrg(), String.valueOf(pickSlip.getPalletTare()), pickSlip.getTolLow(), pickSlip.getTolHigh());
        if (result.get("XC_RETURN_STATUS").equals("S")) {
            result = printerDao.validatePrinter(pickSlip.getOrg(), pickSlip.getPrinter());
            if (result.get("XC_RETURN_STATUS").equals("S")) {
                result = printPickSlipDao.printPickSlip(pickSlip.getOrg(), pickSlip.getPickSlipNumber(), pickSlip.getPickerId(), pickSlip.getUserId(), pickSlip.getTpl(), pickSlip.getContainer(), pickSlip.getPalletTare(), pickSlip.getPrinter());
            }
        }
        return result;
    }

    @PostMapping("picking")
    public Map picking(@RequestBody PickSlipDTO pickSlip) {
        Map<String, Object> result = palletTareDao.validatePalletTare(pickSlip.getOrg(), String.valueOf(pickSlip.getPalletTare()), pickSlip.getTolLow(), pickSlip.getTolHigh());
        if (result.get("XC_RETURN_STATUS").equals("S")) {
            result = printerDao.validatePrinter(pickSlip.getOrg(), pickSlip.getPrinter());
            if (result.get("XC_RETURN_STATUS").equals("S")) {
                result = pickSlipItemDao.getItemDetails(pickSlip.getPickerId(), pickSlip.getUserId(), pickSlip.getOrg(), pickSlip.getPickSlipNumber());
            }
        }
        return result;
    }
}
