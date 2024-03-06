package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Test;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.TestJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.TestPagingAndSortingRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestService {

    private final TestJpaRepository testJpaRepository;
    private final TestPagingAndSortingRepository testPagingAndSortingRepository;

    public Test findById(Long aLong) {
        return testJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Test with id %d not found".formatted(aLong)));
    }

    public List<Test> findAll() {
        return testJpaRepository.findAll();
    }

    public Page<Test> findAll(Integer offset, Integer limit, String keyword) {

        Specification<Test> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((Root<Test> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                return criteriaBuilder.or(titleLike, descriptionLike);
            });
        }

        return testPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }

    @Transactional
    public <S extends Test> S save(S entity) {

        Long id = entity.getId();

        // TODO extract to validator
//        if (testJpaRepository.findById(id).isPresent()) {
//            throw new BadRequestException("Test with id %d is already exists".formatted(id));
//        }

        return testJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Test> S update(S entity) {
        return testJpaRepository.save(entity);
    }

}
