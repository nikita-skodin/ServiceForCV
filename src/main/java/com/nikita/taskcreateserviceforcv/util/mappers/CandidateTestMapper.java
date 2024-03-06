package com.nikita.taskcreateserviceforcv.util.mappers;

import com.nikita.taskcreateserviceforcv.DTOs.CandidateTestDTO;
import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateTestMapper implements Mappable<CandidateTest, CandidateTestDTO> {

    private final TestMapper testMapper;
    private final CandidateMapper candidateMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    public CandidateTest getEntity(CandidateTestDTO candidateTestDTO) {
        CandidateTest map = modelMapper.map(candidateTestDTO, CandidateTest.class);

        map.setCandidate(candidateMapper.getEntity(candidateTestDTO.getCandidate()));
        map.setTest(testMapper.getEntity(candidateTestDTO.getTest()));

        return map;
    }

    public CandidateTestDTO getDTO(CandidateTest candidateTest) {
        CandidateTestDTO map = modelMapper.map(candidateTest, CandidateTestDTO.class);

        map.setCandidate(candidateMapper.getDTO(candidateTest.getCandidate()));
        map.setTest(testMapper.getDTO(candidateTest.getTest()));

        return map;
    }

}
