package backend.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.HotelCreateDTO;
import backend.business.dto.HotelDTO;
import backend.business.dto.MenuDTO;
import backend.server.service.impl.HotelServiceImpl;
import backend.server.service.impl.MenuServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "hotel")
@Controller
@RequestMapping(value = "/hotel")
public class HotelController
{

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private HotelServiceImpl hotelService;

    @RequestMapping(value = "/{hotelId}/menu", method = RequestMethod.GET)
    @ResponseBody
    public MenuDTO getMenuEntriesForHotel(@PathVariable Integer hotelId)
    {
        return menuService.getMenuEntriesForHotel(hotelId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public HotelDTO createHotel(@Valid @RequestBody HotelCreateDTO createDTO)
    {
        return hotelService.createHotel(createDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<HotelDTO> getAllHotels()
    {
        return hotelService.getAllHotels();
    }
}
