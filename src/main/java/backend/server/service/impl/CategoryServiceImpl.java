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
import backend.db.entity.CategoryEntity;
import backend.db.entity.MenuEntriesEntity;

@Service
@Transactional
public class CategoryServiceImpl
{
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private MenuItemListDAO menuItemListDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public CategoryDTO createCategory(CategoryCreateDTO createDTO)
    {
        validateInputForCreateDTO(createDTO);
        CategoryEntity category = mapper.map(createDTO, CategoryEntity.class);
        category = categoryDAO.save(category);
        return mapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> findAll()
    {
        List<CategoryEntity> categories = categoryDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, categories, CategoryDTO.class);
    }

    public List<CategoryDTO> findbyActiveStatus(Boolean active)
    {
        List<CategoryEntity> categories = categoryDAO.findByActive(active);
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
        CategoryEntity categoryEntity = validator.getCategoryEntityFromId(categoryId);
        List<MenuEntriesEntity> menuEntries = menuItemListDAO.findByCategory(categoryEntity);
        if (!menuEntries.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        categoryEntity.setActive(updateDTO.getActive());
        categoryDAO.save(categoryEntity);
        return mapper.map(categoryEntity, CategoryDTO.class);
    }

    public List<CategoryDTO> updateDisplayOrder(List<CategoryUpdateDisplayOrderDTO> updateDTOs)
    {
        List<CategoryDTO> updatedCategories = new ArrayList<CategoryDTO>();
        for (CategoryUpdateDisplayOrderDTO updateDTO : updateDTOs)
        {
            CategoryEntity category1 = validator.getCategoryEntityFromId(updateDTO.getCategoryId1());
            CategoryEntity category2 = validator.getCategoryEntityFromId(updateDTO.getCategoryId2());
            Integer displayOrderCategory1 = category1.getDisplayRank();
            Integer displayOrderCategory2 = category2.getDisplayRank();
            category1.setDisplayRank(displayOrderCategory2);
            category2.setDisplayRank(displayOrderCategory1);
            categoryDAO.save(category1);
            categoryDAO.save(category2);
            updatedCategories.add(mapper.map(category1, CategoryDTO.class));
            updatedCategories.add(mapper.map(category2, CategoryDTO.class));
        }
        return updatedCategories;
    }

    public void deleteCategory(Integer categoryId)
    {
        CategoryEntity category = validator.getCategoryEntityFromId(categoryId);
        if (category.getActive())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        List<MenuEntriesEntity> menu = menuItemListDAO.findByCategory(category);
        if (!menu.isEmpty())
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_IN_USE));
        }
        categoryDAO.delete(category);
    }

    private void validateInputForCreateDTO(CategoryCreateDTO createDTO)
    {
        validateType(createDTO.getType());
        validateSubType(createDTO.getType(), createDTO.getSubType());
        checkIfDuplicateEntryExists(createDTO);
        checkIfDisplayRankAlreadyUsed(createDTO.getDisplayRank());
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
        CategoryEntity category = categoryDAO.findByNameAndTypeAndSubType(createDTO.getName(), createDTO.getType(), createDTO.getSubType());
        if (category != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.CATEGORY_ALREADY_EXISTS));
        }
    }

    private void checkIfDisplayRankAlreadyUsed(Integer displayOrder)
    {
        CategoryEntity category = categoryDAO.findByDisplayRank(displayOrder);
        if (category != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.INVALID_CATEGORY_DISPLAY_RANK));
        }
    }
}
