package backend.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.BillCreateDTO;
import backend.business.dto.BillDTO;
import backend.server.service.impl.BillServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "bill")
@Controller
@RequestMapping(value = "/tables/{tableNo}/orders/{orderId}/bill")
public class BillController
{
    @Autowired
    private BillServiceImpl billService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BillDTO generateBill(@PathVariable Integer tableNo, @PathVariable Integer orderId, @Valid @RequestBody BillCreateDTO createDTO)
    {
        return billService.createBill(createDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BillDTO getOrder(@PathVariable Integer tableNo, @PathVariable Integer orderId)
    {
        return billService.getBillForOrder(orderId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public BillDTO regenerateBill(@PathVariable Integer tableNo, @PathVariable Integer orderId)
    {
        return billService.recalculateBill(orderId);
    }
}
