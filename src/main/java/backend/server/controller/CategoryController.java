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

import backend.business.dto.CategoryCreateDTO;
import backend.business.dto.CategoryDTO;
import backend.business.dto.CategoryUpdateActiveStatusDTO;
import backend.business.dto.CategoryUpdateDTO;
import backend.business.dto.CategoryUpdateDisplayOrderDTO;
import backend.server.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;

@Api(value = "categories", description = "categories")
@Controller
@RequestMapping(value = "/categories")
public class CategoryController
{
    @Autowired
    private CategoryServiceImpl categoryService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public CategoryDTO createCategory(@Valid @RequestBody CategoryCreateDTO createDTO)
    {
        return categoryService.createCategory(createDTO);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<CategoryDTO> getCategories(@RequestParam(required = false) Boolean active)
    {
        if (active == null)
        {
            return categoryService.findAll();
        }
        return categoryService.findbyActiveStatus(active);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CategoryDTO updateCategoryDetails(@PathVariable Integer id, @Valid @RequestBody CategoryUpdateDTO updateDTO)
    {
        return categoryService.updateCategoryDetails(id, updateDTO);
    }

    @RequestMapping(value = "/{id}/active", method = RequestMethod.PUT)
    @ResponseBody
    public CategoryDTO updateCategoryActiveStatus(@PathVariable Integer id, @Valid @RequestBody CategoryUpdateActiveStatusDTO updateDTO)
    {
        return categoryService.updateCategoryActiveStatus(id, updateDTO);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public List<CategoryDTO> updateDisplayOrder(@RequestBody List<CategoryUpdateDisplayOrderDTO> updateDTOs)
    {
        return categoryService.updateDisplayOrder(updateDTOs);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteCategory(@PathVariable Integer id)
    {
        categoryService.deleteCategory(id);
    }
}
