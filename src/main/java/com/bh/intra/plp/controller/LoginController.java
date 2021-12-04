package com.bh.intra.plp.controller;

import com.bh.intra.plp.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginController {

    private final OrganizationDao organizationDao;

    private final PickSlipDao pickSlipDao;

    private final PrintPickSlipDao printPickSlipDao;

    private final ContainerTypeDao containerTypeDao;

    private final PalletTareDao palletTareDao;

    private final PickSlipItemDao pickSlipItemDao;

    private final LpnDao lpnDao;

    private final PrinterDao printerDao;

    @Autowired(required=true)
    private HttpServletRequest request;

    public LoginController(OrganizationDao organizationDao, PickSlipDao pickSlipDao, PrintPickSlipDao printPickSlipDao, ContainerTypeDao containerTypeDao,
                           PalletTareDao palletTareDao, PickSlipItemDao pickSlipItemDao, LpnDao lpnDao, PrinterDao printerDao) {
        this.organizationDao = organizationDao;
        this.pickSlipDao = pickSlipDao;
        this.printPickSlipDao = printPickSlipDao;
        this.containerTypeDao = containerTypeDao;
        this.palletTareDao = palletTareDao;
        this.pickSlipItemDao = pickSlipItemDao;
        this.lpnDao = lpnDao;
        this.printerDao = printerDao;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        log.info("================= Fetching model data to load login page =================");
        model.addAttribute("organisations", organizationDao.getAllOrganizations());
        log.info("================= Model binding done. Loading login page =================");
        return "login";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

//    @GetMapping("nextpickslip")
//    public String nextPickSlip(Model model) {
//        CustomUser principal = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        final Map<String, Object> result = pickSlipDao.getNextPickSlip(principal.getPickerId(), principal.getPickerId(), principal.getOrgCode(), principal.getQueueId());
//        if (result.get("XC_RETURN_STATUS").equals("S")) {
//            int pickSlipNumber = result.get("XN_PICKSLIP") != null ? new BigDecimal(result.get("XN_PICKSLIP").toString()).intValue() : 0;
//            String customerName = result.get("XC_CUSTOMER_NAME") != null ? result.get("XC_CUSTOMER_NAME").toString() : null;
//            String TPL = result.get("XC_TPL") != null ? result.get("XC_TPL").toString() : null;
//            String containerType = result.get("XC_CONTAINER_TYPE") != null ? result.get("XC_CONTAINER_TYPE").toString() : null;
//            int palletTare = result.get("XN_PALLET_TARE") != null ? new BigDecimal(result.get("XN_PALLET_TARE").toString()).intValue() : 0;
//            model.addAttribute("msg", result.get("XC_ERROR_MESSAGE").toString());
//            List<ContainerType> containers = containerTypeDao.getAllContainers();
//            SessionUser user = new SessionUser(principal, pickSlipNumber, customerName, TPL, containerType, palletTare);
//            model.addAttribute("containers", containers);
//            model.addAttribute("ps", user);
//            request.getSession().setAttribute("PICK_SLIP", user);
//            model.addAttribute("pickingDTO", new PickingDTO());
//            return "pickslip";
//        } else {
//            model.addAttribute("error", result.get("XC_ERROR_MESSAGE").toString());
//            return "home";
//        }
//    }
//
//    @PostMapping(value = {"/printPickSlip", "/picking"})
//    public String pickSlipPageAction(Model model, @ModelAttribute PickingDTO pickingDTO, HttpServletRequest request) {
//        log.info("Validating printer {}", pickingDTO);
//        String[] selected = pickingDTO.getContainer().split("::");
//        log.info("Selected container {}", selected[3]);
//        SessionUser su = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        Map<String, Object> result = palletTareDao.validatePalletTare(su.getOrgCode(), String.valueOf(pickingDTO.getPalletTare()), selected[1], selected[2]);
//        if (result.get("XC_RETURN_STATUS").equals("S")) {
//            result = printerDao.validatePrinter(su.getOrgCode(), pickingDTO.getPrinter());
//            if (result.get("XC_RETURN_STATUS").equals("S")) {
//                if (request.getServletPath().equals("/printPickSlip")) {
//                    result = printPickSlipDao.printPickSlip(su.getOrgCode(), su.getPickSlipNumber(), su.getPickerId(), su.getUserId(), su.getTPL(), selected[3], pickingDTO.getPalletTare(), pickingDTO.getPrinter());
//                } else {
//                    return "redirect:pickslipDetails";
//                }
//                model.addAttribute(result.get("XC_RETURN_STATUS").equals("S") ? "msg" : "error", result.get("XC_ERROR_MESSAGE").toString());
//            } else {
//                model.addAttribute("error", result.get("XC_ERROR_MESSAGE").toString());
//            }
//        } else {
//            model.addAttribute("error", result.get("XC_ERROR_MESSAGE").toString());
//        }
//        List<ContainerType> containers = containerTypeDao.getAllContainers();
//        model.addAttribute("containers", containers);
//        model.addAttribute("ps", su);
//        model.addAttribute("pickingDTO", pickingDTO);
//        return "pickslip";
//    }
//
//    @GetMapping("/pickslipDetails")
//    public String pickslipDetails(Model model) {
//        SessionUser su = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        Map<String, Object> result = pickSlipItemDao.getItemDetails(su.getPickerId(), su.getUserId(), su.getOrgCode(), su.getPickSlipNumber());
//        ItemDetails item = new ItemDetails();
//        if (result.get("XC_RETURN_STATUS").equals("S")) {
//            int itemQty = result.get("XN_ITEM_QUANTITY") != null ? new BigDecimal(result.get("XN_ITEM_QUANTITY").toString()).intValue() : 0;
//            int pickOrder = result.get("XN_ITEM_PICK_ORDER") != null ? new BigDecimal(result.get("XN_ITEM_PICK_ORDER").toString()).intValue() : 0;
//            String itemNumber = result.get("XC_ITEM_NUMBER") != null ? result.get("XC_ITEM_NUMBER").toString() : null;
//            String itemDesc = result.get("XC_ITEM_DESC") != null ? result.get("XC_ITEM_DESC").toString() : null;
//            String itemLocation = result.get("XC_PICK_LOCATION") != null ? result.get("XC_PICK_LOCATION").toString() : null;
//            item = new ItemDetails(itemNumber, itemDesc, pickOrder, itemLocation, itemQty);
//            model.addAttribute("msg", result.get("XC_ERROR_MESSAGE").toString());
//        } else {
//            model.addAttribute("error", result.get("XC_ERROR_MESSAGE").toString());
//        }
//        su.setItem(item);
//        request.getSession().setAttribute("PICK_SLIP", su);
//        model.addAttribute("ps", su);
//        return "pickslip_details";
//    }
//
//    @PostMapping(value = {"/validateLpn", "/confirmPickingDetails"})
//    public String pickSlipDetailsPageAction(@ModelAttribute SessionUser ps, HttpServletRequest request) {
//        SessionUser user = (SessionUser) request.getSession().getAttribute("PICK_SLIP");
//        if (request.getServletPath().equals("/validateLpn")) {
//            Map<String, Object> result = lpnDao.validateLPN(user.getOrgCode(), user.getPickSlipNumber(), user.getPickerId(), user.getUserId(), user.getItem().getNumber(), ps.getLpn().getLpn());
//        }
//        return "redirect:pickslipDetails";
//    }
}
