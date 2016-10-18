package backend.server.service.impl;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import backend.business.dto.HotelCreateDTO;
import backend.business.dto.HotelDTO;
import backend.business.dto.HotelMenuDTO;
import backend.business.library.UtilHelper;
import backend.db.dao.HotelDAO;
import backend.db.dao.HotelMenuDAO;
import backend.db.entity.HotelEntity;
import backend.db.entity.HotelMenuEntity;

@Service
public class HotelServiceImpl
{
    @Autowired
    private HotelDAO hotelDAO;

    @Autowired
    private HotelMenuDAO hotelMenuDAO;

    @Autowired
    private Mapper mapper;

    public HotelDTO createHotel(HotelCreateDTO createDTO)
    {
        HotelEntity hotel = mapper.map(createDTO, HotelEntity.class);
        hotel = hotelDAO.save(hotel);
        createHotelMenuEntry(hotel);
        return mapper.map(hotel, HotelDTO.class);
    }

    public List<HotelDTO> getAllHotels()
    {
        Sort sort = new Sort(new Order(Direction.ASC,"name"));
        List<HotelEntity> hotels = hotelDAO.findAll(sort);
        return UtilHelper.mapListOfEnitiesToDTOs(mapper, hotels, HotelDTO.class);
    }

    private HotelMenuDTO createHotelMenuEntry(HotelEntity hotel)
    {
        HotelMenuEntity hotelMenu = new HotelMenuEntity();
        hotelMenu.setHotel(hotel);
        hotelMenu = hotelMenuDAO.save(hotelMenu);
        return mapper.map(hotelMenu, HotelMenuDTO.class);
    }
}
