package com.nikita.taskcreateserviceforcv.repositories;

import com.nikita.taskcreateserviceforcv.entities.Test;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPagingAndSortingRepository extends PagingAndSortingRepository<Test, Long>, JpaSpecificationExecutor<Test> {
}
