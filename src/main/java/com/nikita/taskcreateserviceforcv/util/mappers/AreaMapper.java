package com.nikita.taskcreateserviceforcv.util.mappers;

import com.nikita.taskcreateserviceforcv.DTOs.AreaDTO;
import com.nikita.taskcreateserviceforcv.entities.Area;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public Area getEntity(AreaDTO areaDTO) {
        return modelMapper.map(areaDTO, Area.class);
    }

    public AreaDTO getDTO(Area area) {
        return modelMapper.map(area, AreaDTO.class);
    }

}
