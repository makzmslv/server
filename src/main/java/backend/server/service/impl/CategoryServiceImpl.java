package backend.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.CategoryCreateDTO;
import backend.business.dto.CategoryDTO;
import backend.business.dto.CategoryUpdateActiveStatusDTO;
import backend.business.dto.CategoryUpdateDTO;
import backend.business.dto.CategoryUpdateDisplayOrderDTO;
import backend.business.enums.CategorySubType;
import backend.business.enums.CategoryType;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.CategoryDAO;
import backend.db.dao.MenuItemListDAO;
import backend.db.dao.SubCategoryDAO;
import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntriesEntity;
import backend.db.entity.SubCategoryEntity;

@Service
@Transactional
public class CategoryServiceImpl
{
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SubCategoryDAO subCategoryDAO;

    @Autowired
    private MenuItemListDAO menuItemListDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public CategoryDTO createCategory(CategoryCreateDTO createDTO)
    {
        validateInputForCreateDTO(createDTO);
        CategoryEntity category = new CategoryEntity();
        category.setName(createDTO.getName());
        category.setHotel(validator.getHotelFromId(createDTO.getHotelId()));
        category = categoryDAO.save(category);
        SubCategoryEntity subCategory = mapper.map(createDTO, SubCategoryEntity.class);
        subCategory.setCategory(category);
        subCategoryDAO.save(subCategory);
        CategoryDTO categoryDTO = mapper.map(subCategory, CategoryDTO.class);
        categoryDTO.setId(category.getId());
        categoryDTO.setSubCategoryId(subCategory.getId());
        return categoryDTO;
    }

    public List<CategoryDTO> findAll()
    {
        List<CategoryEntity> categories = categoryDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, categories, CategoryDTO.class);
    }

    public List<CategoryDTO> findbyActiveStatus(Boolean active)
    {
        List<CategoryEntity> categories = categoryDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, categories, CategoryDTO.class);
    }

    public CategoryDTO updateCategoryDetails(Integer categoryId, CategoryUpdateDTO updateDTO)
    {
        validateUpdateDTO(categoryId, updateDTO);
        CategoryEntity category = validator.getCategoryEntityFromId(categoryId);
        mapper.map(updateDTO, category);
        categoryDAO.save(category);
        return mapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO updateCategoryActiveStatus(Integer categoryId, CategoryUpdateActiveStatusDTO updateDTO)
    {
        SubCategoryEntity subCategoryEntity = validator.getSubCategoryEntityFromId(categoryId);
        List<MenuEntriesEntity> menuEntries = menuItemListDAO.findBySubCategory(subCategoryEntity);
        if (!menuEntries.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        subCategoryEntity.setActive(updateDTO.getActive());
        subCategoryDAO.save(subCategoryEntity);
        return mapper.map(subCategoryEntity, CategoryDTO.class);
    }

    public List<CategoryDTO> updateDisplayOrder(List<CategoryUpdateDisplayOrderDTO> updateDTOs)
    {
        List<CategoryDTO> updatedCategories = new ArrayList<CategoryDTO>();
        for (CategoryUpdateDisplayOrderDTO updateDTO : updateDTOs)
        {
            SubCategoryEntity category1 = validator.getSubCategoryEntityFromId(updateDTO.getCategoryId1());
            SubCategoryEntity category2 = validator.getSubCategoryEntityFromId(updateDTO.getCategoryId2());
            Integer displayOrderCategory1 = category1.getDisplayRank();
            Integer displayOrderCategory2 = category2.getDisplayRank();
            category1.setDisplayRank(displayOrderCategory2);
            category2.setDisplayRank(displayOrderCategory1);
            subCategoryDAO.save(category1);
            subCategoryDAO.save(category2);
            updatedCategories.add(mapper.map(category1, CategoryDTO.class));
            updatedCategories.add(mapper.map(category2, CategoryDTO.class));
        }
        return updatedCategories;
    }

    public void deleteCategory(Integer categoryId)
    {
        SubCategoryEntity category = validator.getSubCategoryEntityFromId(categoryId);
        if (category.getActive())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        List<MenuEntriesEntity> menu = menuItemListDAO.findBySubCategory(category);
        if (!menu.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        subCategoryDAO.delete(category);
    }

    private void validateInputForCreateDTO(CategoryCreateDTO createDTO)
    {
        validateType(createDTO.getType());
        validateSubType(createDTO.getType(), createDTO.getSubType());
        checkIfDuplicateEntryExists(createDTO);
        checkIfDisplayRankAlreadyUsed(createDTO);
    }

    private void validateUpdateDTO(Integer categoryId, CategoryUpdateDTO updateDTO)
    {
        CategoryEntity categoryEntity = validator.getCategoryEntityFromId(categoryId);
        if (categoryEntity.equals(mapper.map(updateDTO, CategoryEntity.class)))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }
        validateType(updateDTO.getType());
        validateSubType(updateDTO.getType(), updateDTO.getSubType());
    }

    private void validateType(Integer categoryType)
    {
        if (!CategoryType.getAllTypes().contains(categoryType))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_CATEGORY_TYPE));
        }
    }

    private void validateSubType(Integer categoryType, Integer categorySubType)
    {
        if (!CategorySubType.getAllSubTypes().contains(categorySubType))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_CATEGORY_SUB_TYPE));
        }

        if (CategoryType.FOOD.getCode().equals(categoryType))
        {
            if (!CategorySubType.getFoodSubTypes().contains(categorySubType))
            {
                throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_TYPE_SUB_TYPE_MISMATCH));
            }
        }

        if (CategoryType.DRINKS.getCode().equals(categoryType))
        {
            if (!CategorySubType.getDrinkSubTypes().contains(categorySubType))
            {
                throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_TYPE_SUB_TYPE_MISMATCH));
            }
        }
    }

    private void checkIfDuplicateEntryExists(CategoryCreateDTO createDTO)
    {
        CategoryEntity category = categoryDAO.findByName(createDTO.getName());
        List<SubCategoryEntity> subCategory = subCategoryDAO.findByCategoryAndTypeAndSubType(category, createDTO.getType(), createDTO.getSubType());
        if (!subCategory.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_ALREADY_EXISTS));
        }
    }

    private void checkIfDisplayRankAlreadyUsed(CategoryCreateDTO createDTO)
    {
        CategoryEntity category = categoryDAO.findByName(createDTO.getName());
        SubCategoryEntity subCategory = subCategoryDAO.findByCategoryAndDisplayRank(category, createDTO.getDisplayRank());
        if (subCategory != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_CATEGORY_DISPLAY_RANK));
        }
    }
}
