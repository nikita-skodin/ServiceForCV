package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.CandidateJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.CandidatePagingAndSortingRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidatePagingAndSortingRepository candidatePagingAndSortingRepository;

    public Candidate findById(Long aLong) {
        return candidateJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Candidate with id %d not found".formatted(aLong)));
    }

    public List<Candidate> findAll() {
        return candidateJpaRepository.findAll();
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

    @Transactional
    public <S extends Candidate> S save(S entity) {

        Long id = entity.getId();

        // TODO extract to validator
//        if (candidateJpaRepository.findById(id).isPresent()) {
//            throw new BadRequestException("Candidate with id %d is already exists".formatted(id));
//        }

        return candidateJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Candidate> S update(S entity) {
        return candidateJpaRepository.save(entity);
    }

}
