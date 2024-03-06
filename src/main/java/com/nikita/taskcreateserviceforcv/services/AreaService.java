package com.nikita.taskcreateserviceforcv.services;

import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.exceptions.NotFoundException;
import com.nikita.taskcreateserviceforcv.repositories.areaRepositoties.AreaPagingAndSortingRepository;
import com.nikita.taskcreateserviceforcv.repositories.areaRepositoties.AreaJpaRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaService implements JpaService<Area, Long> {
    private final AreaJpaRepository areaJpaRepository;
    private final AreaPagingAndSortingRepository areaPagingAndSortingRepository;

    public Area findById(Long aLong) {
        return areaJpaRepository.findById(aLong).orElseThrow(() ->
                new NotFoundException("Area with id %d not found".formatted(aLong)));
    }

    public Page<Area> findAll(Integer offset, Integer limit, String keyword) {

        Specification<Area> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                return criteriaBuilder.or(titleLike, descriptionLike);
            });
        }

        return areaPagingAndSortingRepository.findAll(spec, PageRequest.of(offset, limit));
    }

    public Optional<Area> findByTitle(String title) {
        return areaJpaRepository.findByTitle(title);
    }

    @Transactional
    public <S extends Area> S save(S entity) {
        return areaJpaRepository.save(entity);
    }

    @Transactional
    public <S extends Area> S update(S entity) {
        return areaJpaRepository.save(entity);
    }
}
