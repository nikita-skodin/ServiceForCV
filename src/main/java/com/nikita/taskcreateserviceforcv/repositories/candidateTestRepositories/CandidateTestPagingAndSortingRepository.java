package com.nikita.taskcreateserviceforcv.repositories.candidateTestRepositories;

import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import com.nikita.taskcreateserviceforcv.entities.Test;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateTestPagingAndSortingRepository extends PagingAndSortingRepository<CandidateTest, Long>, JpaSpecificationExecutor<CandidateTest> {
}
