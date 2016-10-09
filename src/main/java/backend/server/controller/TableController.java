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

import backend.business.dto.OrderDTO;
import backend.business.dto.TableCreateDTO;
import backend.business.dto.TableDTO;
import backend.business.dto.TableUpdateDTO;
import backend.server.service.impl.TableServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "tables", description = "tables")
@Controller
@RequestMapping(value = "/tables")
public class TableController
{
    @Autowired
    private TableServiceImpl tableService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public TableDTO createTable(@Valid @RequestBody TableCreateDTO createDTO)
    {
        return tableService.createTable(createDTO);
    }

    @RequestMapping(value = "/{tableNo}", method = RequestMethod.PUT)
    @ResponseBody
    public TableDTO updateTable(@PathVariable Integer tableNo, @Valid @RequestBody TableUpdateDTO updateDTO)
    {
        return tableService.updateTable(tableNo, updateDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<TableDTO> getTables(@RequestParam(required = false) Boolean active)
    {
        if (active == null)
        {
            return tableService.findAll();
        }
        return tableService.findbyActiveStatus(active);
    }

    @RequestMapping(value = "/{tableNo}/orders", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderDTO> getAllOrdersForTable(@PathVariable Integer tableNo)
    {
        return tableService.getAllOrdersForTable(tableNo);
    }
}
