package com.nikita.taskcreateserviceforcv.repositories;

import com.nikita.taskcreateserviceforcv.entities.Area;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaPagingAndSortingRepository extends PagingAndSortingRepository<Area, Long>, JpaSpecificationExecutor<Area> {
}