package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.entities.Test;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.testRepositories.TestJpaRepository;
import com.nikita.taskcreateserviceforcv.repositories.testRepositories.TestPagingAndSortingRepository;
import com.nikita.taskcreateserviceforcv.services.interfaces.JpaService;
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

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestService implements JpaService<Test, Long> {

    private final AreaService areaService;

    private final TestJpaRepository testJpaRepository;
    private final TestPagingAndSortingRepository testPagingAndSortingRepository;


    public Test findById(Long aLong) {
        return testJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Test with id %d not found".formatted(aLong)));
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

    public Optional<Test> findByTitle(String title) {
        return testJpaRepository.findByTitle(title);
    }

    @Transactional
    public <S extends Test> S save(S entity) {

        entity.setApplicableAreas(entity.getApplicableAreas().stream().map(area -> {

            Area byId;

            try {
                byId = areaService.findById(area.getId());
                byId.getApplicableTests().add(entity);
            } catch (Exception e) {
                throw new BadRequestException("Area with such id does not exist");
            }

            return byId;
        }).collect(Collectors.toSet()));

        return testJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Test> S update(S entity) {
        return save(entity);
    }
}
