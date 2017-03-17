package backend.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.HotelCreateDTO;
import backend.business.dto.OrderDTO;
import backend.business.dto.HotelDTO;
import backend.business.dto.MenuCreateDTO;
import backend.business.dto.MenuDTO;
import backend.server.service.impl.HotelServiceImpl;
import backend.server.service.impl.MenuServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "hotel")
@Controller
@RequestMapping(value = "/hotels")
public class HotelController
{

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private HotelServiceImpl hotelService;

    @PreAuthorize("hasRole('READ_ONLY')")
    @RequestMapping(value = "/{hotelId}/menus", method = RequestMethod.GET)
    @ResponseBody
    public MenuDTO getMenuEntriesForHotel(@PathVariable Integer hotelId)
    {
        return menuService.getMenuEntriesForHotel(hotelId);
    }

    @PreAuthorize("hasRole('READ_ONLY')")
    @RequestMapping(value = "/{hotelId}/orders", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderDTO> getOrdersForHotel(@PathVariable Integer hotelId)
    {
        return hotelService.getOrdersForHotel(hotelId);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(value = "/{hotelId}/menus", method = RequestMethod.POST)
    @ResponseBody
    public List<MenuDTO> createMenuEntry(@PathVariable Integer hotelId, @Valid @RequestBody List<MenuCreateDTO> createDTO)
    {
        return menuService.createMenuEntries(hotelId, createDTO);
    }

    @PreAuthorize("hasRole('ACCESS_ALL')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public HotelDTO createHotel(@Valid @RequestBody HotelCreateDTO createDTO)
    {
        return hotelService.createHotel(createDTO);
    }
    
    @PreAuthorize("hasRole('READ_ONLY')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<HotelDTO> getAllHotels(@RequestParam(required = false) String area, @RequestParam(required = false) String name)
    {
        return hotelService.getAllHotels(area, name);
    }
}
