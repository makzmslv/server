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

import backend.business.dto.MenuCreateDTO;
import backend.business.dto.MenuDTO;
import backend.business.dto.MenuUpdateDTO;
import backend.server.service.impl.MenuServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "menu", description = "menu")
@Controller
@RequestMapping(value = "/menu")
public class MenuController
{
    @Autowired
    private MenuServiceImpl menuService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<MenuDTO> createMenuEntry(@Valid @PathVariable Integer hotelId, @RequestBody List<MenuCreateDTO> createDTO)
    {
        return menuService.createMenuEntries(hotelId, createDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public MenuDTO updateMenuEntry(@PathVariable Integer id, @Valid @RequestBody MenuUpdateDTO updateDTO)
    {
        return menuService.updateMenuEntry(id, updateDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<MenuDTO> getMenuEntries()
    {
        return menuService.getMenuEntries();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteMenuEntry(@PathVariable Integer id)
    {
        menuService.deleteMenuEntry(id);
    }
}
