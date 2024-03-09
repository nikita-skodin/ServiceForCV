package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import com.nikita.taskcreateserviceforcv.entities.Test;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.candidateTestRepositories.CandidateTestJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.candidateTestRepositories.CandidateTestPagingAndSortingRepository;
import com.nikita.taskcreateserviceforcv.services.interfaces.JpaService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateTestService implements JpaService<CandidateTest, Long> {

    private final CandidateTestJpaRepository candidateTestJpaRepository;
    private final CandidateTestPagingAndSortingRepository candidateTestPagingAndSortingRepository;

    private final CandidateService candidateService;
    private final TestService testService;

    public CandidateTest findById(Long aLong) {
        return candidateTestJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("CandidateTest with id %d not found".formatted(aLong)));
    }

    public Page<CandidateTest> findAll(Integer offset, Integer limit, Long testId, Long candidateId, Boolean isSortedByDate, Boolean isSortedByScore) {
        Specification<CandidateTest> spec = Specification.where(null);

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

        return candidateTestPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }

    public <S extends CandidateTest> boolean exists(Example<S> example) {
        return candidateTestJpaRepository.exists(example);
    }

    @Transactional
    public <S extends CandidateTest> S save(S entity) {

        Test test = testService.findById(entity.getTest().getId());
        Candidate candidate = candidateService.findById(entity.getCandidate().getId());

        entity.setTest(test);
        entity.setCandidate(candidate);

        test.getAttemptsList().add(entity);
        candidate.getCandidateTests().add(entity);

        log.info("saving {}", entity);
        return candidateTestJpaRepository.save(entity);
    }

    @Transactional
    public <S extends CandidateTest> S update(S entity) {

        Test test = testService.findById(entity.getTest().getId());
        Candidate candidate = candidateService.findById(entity.getCandidate().getId());

        test.getAttemptsList().add(entity);
        candidate.getCandidateTests().add(entity);

        log.info("updating {}", entity);
        return candidateTestJpaRepository.save(entity);
    }
}
