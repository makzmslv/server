package backend.server.service.impl;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.business.dto.OrderDTO;
import backend.business.dto.TableCreateDTO;
import backend.business.dto.TableDTO;
import backend.business.dto.TableUpdateDTO;
import backend.business.enums.ErrorCodes;
import backend.business.error.ErrorMessage;
import backend.business.error.ServerException;
import backend.business.library.UtilHelper;
import backend.db.dao.OrderDAO;
import backend.db.dao.TableDAO;
import backend.db.entity.OrderEntity;
import backend.db.entity.TableEntity;

@Service
@Transactional
public class TableServiceImpl
{
    @Autowired
    private TableDAO tableDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private EntryExistingValidator validator;

    @Autowired
    private Mapper mapper;

    public TableDTO createTable(TableCreateDTO createDTO)
    {
        validateCreateTableInput(createDTO);
        TableEntity tableEntity = mapper.map(createDTO, TableEntity.class);
        tableEntity = tableDAO.save(tableEntity);
        return mapper.map(tableEntity, TableDTO.class);
    }

    public TableDTO updateTable(Integer tableNo, TableUpdateDTO updateDTO)
    {
        validateUpdateTableInput(tableNo, updateDTO);
        TableEntity tableEntity = tableDAO.findByTableNo(tableNo);
        tableEntity.setActive(updateDTO.getActive());
        tableEntity = tableDAO.save(tableEntity);
        return mapper.map(tableEntity, TableDTO.class);
    }

    public List<TableDTO> findAll()
    {
        List<TableEntity> tables = tableDAO.findAll();
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, tables, TableDTO.class);
    }

    public List<TableDTO> findbyActiveStatus(Boolean active)
    {
        List<TableEntity> tables = tableDAO.findByActive(active);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, tables, TableDTO.class);
    }

    public List<OrderDTO> getAllOrdersForTable(Integer tableNo)
    {
        TableEntity tableEntity = validator.getTableEntityFromTableNo(tableNo);
        return null;
    }

    private void validateCreateTableInput(TableCreateDTO createDTO)
    {
        TableEntity tableEntity = tableDAO.findByTableNo(createDTO.getTableNo());
        if (tableEntity != null)
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.TABLE_ALREADY_EXISTS));
        }

    }

    private void validateUpdateTableInput(Integer tableNo, TableUpdateDTO updateDTO)
    {
        TableEntity tableEntity = validator.getTableEntityFromTableNo(tableNo);

        if (tableEntity.getActive().equals(updateDTO.getActive()))
        {
            throw new ServerException(new ErrorMessage(ErrorCodes.NO_FIELDS_UPDATED));
        }

    }
}
