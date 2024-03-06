package com.nikita.taskcreateserviceforcv.util.mappers;

import com.nikita.taskcreateserviceforcv.DTOs.TestDTO;
import com.nikita.taskcreateserviceforcv.entities.Test;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestMapper implements Mappable<Test, TestDTO> {

    private final ModelMapper modelMapper = new ModelMapper();
    private final AreaMapper areaMapper;

    public Test getEntity(TestDTO testDTO) {
        Test map = modelMapper.map(testDTO, Test.class);
        map.setApplicableAreas(testDTO.getApplicableAreas().stream().map(areaMapper::getEntity).collect(Collectors.toSet()));
        return map;
    }

    public TestDTO getDTO(Test test) {
        TestDTO map = modelMapper.map(test, TestDTO.class);
        map.setApplicableAreas(test.getApplicableAreas().stream().map(areaMapper::getDTO).collect(Collectors.toSet()));
        return map;
    }

}
