package backend.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import backend.business.dto.MenuItemCreateDTO;
import backend.business.dto.MenuItemDTO;
import backend.business.dto.MenuItemUnitDTO;
import backend.business.dto.MenuItemUpdateActiveStatusDTO;
import backend.business.dto.MenuItemUpdateDTO;
import backend.server.service.impl.MenuItemServiceImpl;
import backend.server.service.impl.MenuItemUnitServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "menuItems", description = "menuItems")
@Controller
@RequestMapping(value = "/menuItems")
public class MenuItemController
{
    @Autowired
    private MenuItemServiceImpl menuItemService;

    @Autowired
    private MenuItemUnitServiceImpl menuItemUnitService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public MenuItemDTO createMenuItem(@Valid @RequestBody MenuItemCreateDTO createDTO)
    {
        return menuItemService.createMenuItem(createDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public MenuItemDTO updateMenuItemDetails(@PathVariable Integer id, @Valid @RequestBody MenuItemUpdateDTO updateDTO)
    {
        return menuItemService.updateMenuItemDetails(id, updateDTO);
    }

    @RequestMapping(value = "/{id}/active", method = RequestMethod.PUT)
    @ResponseBody
    public MenuItemDTO updateCategoryActiveStatus(@PathVariable Integer id, @Valid @RequestBody MenuItemUpdateActiveStatusDTO updateDTO)
    {
        return menuItemService.updateMenuItemActiveStatus(id, updateDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<MenuItemDTO> getMenuItems(@RequestParam(required = false) Boolean uncategorized, @RequestParam(required = false) Boolean active)
    {
        if (active != null)
        {
            return menuItemService.findbyActiveStatus(active);
        }
        if (uncategorized != null)
        {
//            menuItemService.findAllUncategorizedItems(uncategorized);
        }
        return menuItemService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteMenuItem(@PathVariable Integer id)
    {
        menuItemService.deleteMenuItem(id);
    }

    @RequestMapping(value = "/{id}/menuitemunits", method = RequestMethod.POST)
    @ResponseBody
    public MenuItemUnitDTO createMenuItemUnit(@PathVariable Integer id, @Valid @RequestBody MenuItemUnitDTO createDTO)
    {
        return menuItemUnitService.createMenuItemUnit(id, createDTO);
    }

    @RequestMapping(value = "/{id}/menuitemunits/{unitId}", method = RequestMethod.PUT)
    @ResponseBody
    public MenuItemUnitDTO updateMenuItemUnit(@PathVariable Integer id, Integer unitId, @Valid @RequestBody MenuItemUnitDTO createDTO)
    {
        return menuItemUnitService.updateMenuItemUnit(id, unitId, createDTO);
    }
}
