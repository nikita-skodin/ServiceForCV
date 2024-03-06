package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.CandidateTestJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.CandidateTestPagingAndSortingRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
public class CandidateTestService {

    private final CandidateTestJpaRepository candidateTestJpaRepository;
    private final CandidateTestPagingAndSortingRepository candidateTestPagingAndSortingRepository;

    public CandidateTest findById(Long aLong) {
        return candidateTestJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("CandidateTest with id %d not found".formatted(aLong)));
    }

    public List<CandidateTest> findAll() {
        return candidateTestJpaRepository.findAll();
    }

    public Page<CandidateTest> findAll(Integer offset, Integer limit, Long testId, Long candidateId, Boolean isSortedByDate, Boolean isSortedByScore) {
        Specification<CandidateTest> spec = Specification.where(null);

        // Фильтрация по testId и candidateId
        if (testId != null && candidateId != null) {
            spec = spec.and((Root<CandidateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("test").get("id"), testId),
                            criteriaBuilder.equal(root.get("candidate").get("id"), candidateId)
                    )
            );
        } else if (testId != null) {
            spec = spec.and((Root<CandidateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("test").get("id"), testId)
            );
        } else if (candidateId != null) {
            spec = spec.and((Root<CandidateTest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("candidate").get("id"), candidateId)
            );
        }

        // Сортировка по date и score
        if (isSortedByDate && isSortedByScore) {
            Sort sortByDateAndScore = Sort.by(Sort.Order.asc("dateOfPassing"), Sort.Order.asc("score"));
            return candidateTestPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit, sortByDateAndScore));
        } else if (isSortedByDate) {
            Sort sortByDate = Sort.by(Sort.Order.asc("dateOfPassing"));
            return candidateTestPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit, sortByDate));
        } else if (isSortedByScore) {
            Sort sortByScore = Sort.by(Sort.Order.asc("score"));
            return candidateTestPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit, sortByScore));
        }

        // Возвращаем результат без сортировки, если ничего не указано
        return candidateTestPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }


    @Transactional
    public <S extends CandidateTest> S save(S entity) {

        Long id = entity.getId();

        // TODO extract to validator
//        if (candidateTestJpaRepository.findById(id).isPresent()) {
//            throw new BadRequestException("CandidateTest with id %d is already exists".formatted(id));
//        }

        return candidateTestJpaRepository.save(entity);
    }

    @Transactional
    public <S extends CandidateTest> S update(S entity) {
        return candidateTestJpaRepository.save(entity);
    }

}
