package com.nikita.taskcreateserviceforcv.util.mappers;

import com.nikita.taskcreateserviceforcv.DTOs.CandidateDTO;
import com.nikita.taskcreateserviceforcv.entities.Candidate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandidateMapper implements Mappable<Candidate, CandidateDTO> {

    private final AreaMapper areaMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    public Candidate getEntity(CandidateDTO candidateDTO) {
        Candidate map = modelMapper.map(candidateDTO, Candidate.class);
        map.setPossibleAreas(candidateDTO.getPossibleAreas().stream().map(areaMapper::getEntity).collect(Collectors.toSet()));
        return map;
    }

    public CandidateDTO getDTO(Candidate candidate) {
        CandidateDTO map = modelMapper.map(candidate, CandidateDTO.class);
        map.setPossibleAreas(candidate.getPossibleAreas().stream().map(areaMapper::getDTO).collect(Collectors.toSet()));
        return map;
    }

}
