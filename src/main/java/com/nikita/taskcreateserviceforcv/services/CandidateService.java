package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.candidateRepositories.CandidateJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.candidateRepositories.CandidatePagingAndSortingRepository;
import com.nikita.taskcreateserviceforcv.services.interfaces.JpaService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateService implements JpaService<Candidate, Long> {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidatePagingAndSortingRepository candidatePagingAndSortingRepository;

    private final AreaService areaService;

    public Candidate findById(Long aLong) {
        return candidateJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Candidate with id %d not found".formatted(aLong)));
    }

    public Page<Candidate> findAll(Integer offset, Integer limit, String keyword, Boolean isSorted) {

        Specification<Candidate> spec = Specification.where(null);

        // TODO add indexing to the database
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((Root<Candidate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                Predicate lastnameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                return criteriaBuilder.or(lastnameLike, descriptionLike);
            });
        }

        if (isSorted) {
            Sort sortByLastname = Sort.by(Sort.Direction.ASC, "lastname");
            return candidatePagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit, sortByLastname));
        }


        return candidatePagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }

    public boolean exists(Example<Candidate> of) {
        return candidateJpaRepository.exists(of);
    }

    @Transactional
    public <S extends Candidate> S save(S entity) {

        entity.setPossibleAreas(entity.getPossibleAreas().stream().map(area -> {

            Area byId;

            try {
                byId = areaService.findById(area.getId());
                byId.getCandidatesForArea().add(entity);
            } catch (Exception e) {
                throw new BadRequestException("Area with such id does not exist");
            }

            return byId;
        }).collect(Collectors.toSet()));

        return candidateJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Candidate> S update(S entity) {
        return candidateJpaRepository.save(entity);
    }
}
