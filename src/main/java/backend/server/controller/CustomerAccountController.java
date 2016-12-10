package backend.server.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.CustomerDetailsDTO;
import backend.server.service.impl.CustomerAccountServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "customers")
@Controller
@RequestMapping(value = "/customers")
public class CustomerAccountController 
{
	@Autowired
    private CustomerAccountServiceImpl customerAccountServiceImpl;

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CustomerDetailsDTO getMenuEntriesForHotel(@Valid @RequestBody CustomerDetailsDTO inputDTO)
    {
        return customerAccountServiceImpl.createCustomerAccountDetails(inputDTO);
    }

}
