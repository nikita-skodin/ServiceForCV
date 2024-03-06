package com.nikita.taskcreateserviceforcv.repositories;

import com.nikita.taskcreateserviceforcv.entities.Candidate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatePagingAndSortingRepository extends PagingAndSortingRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
}