package backend.server.service.impl;

import java.util.Random;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import backend.business.dto.CustomerDetailsDTO;
import backend.db.dao.CustomerAccountDetailsDAO;
import backend.db.dao.CustomerDetailsDAO;
import backend.db.entity.CustomerAccountDetailsEntity;
import backend.db.entity.CustomerDetailsEntity;

@Service
public class CustomerAccountServiceImpl 
{
	
	@Autowired
	private CustomerAccountDetailsDAO customerAccountDetailsDAO;
	
	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;
	
	@Autowired
	 private Mapper mapper;
	public CustomerDetailsDTO createCustomerAccountDetails(CustomerDetailsDTO inputDTO)
	{
		CustomerDetailsEntity customerAccDetails = mapper.map(inputDTO, CustomerDetailsEntity.class);
		customerAccDetails.setRegistrationId(generateResgistrationId(customerAccDetails));
		customerDetailsDAO.save(customerAccDetails);
		CustomerAccountDetailsEntity loginDetails = createAccountLoginDetails(customerAccDetails, inputDTO);
		CustomerDetailsDTO customerDetailsDTO = mapper.map(customerAccDetails, CustomerDetailsDTO.class);
		customerDetailsDTO.setUsername(loginDetails.getUserName());
		customerDetailsDTO.setPassword(loginDetails.getPassword());
		return customerDetailsDTO;
	}
	
	public CustomerDetailsDTO getCustomer(Integer user)
	{
		CustomerDetailsEntity customerAccDetails = customerDetailsDAO.findOne(user);
		CustomerDetailsDTO customerDetailsDTO = mapper.map(customerAccDetails, CustomerDetailsDTO.class);
		customerDetailsDTO.setUsername(customerAccDetails.getLoginDetails().getUserName());
		customerDetailsDTO.setPassword(customerAccDetails.getLoginDetails().getPassword());
		return  customerDetailsDTO;
	}
	
	private Integer generateResgistrationId(CustomerDetailsEntity customerAccDetails) {
		boolean validIdGenerated = false;
		Integer registrationId = 0;
		while(!validIdGenerated)
		{
			 Random ran = new Random();
			registrationId =  ran.nextInt(10000) + 1;
			CustomerDetailsEntity customerDetails = customerDetailsDAO.findByRegistrationId(registrationId);
			if(customerDetails == null)
			{
				validIdGenerated = true;
			}
		}
		return registrationId;
		
	}

	private CustomerAccountDetailsEntity createAccountLoginDetails(CustomerDetailsEntity customerAccDetails, CustomerDetailsDTO inputDTO) 
	{
		
		CustomerAccountDetailsEntity loginDetails = new CustomerAccountDetailsEntity();
		loginDetails.setCustomerDetails(customerAccDetails);
		loginDetails.setUserName(inputDTO.getUsername());
		ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		loginDetails.setPassword(passwordEncoder.encodePassword(inputDTO.getPassword(), null));
		loginDetails.setEnabled(true);
		loginDetails.setRole(1);
		return customerAccountDetailsDAO.save(loginDetails);
	}
}
