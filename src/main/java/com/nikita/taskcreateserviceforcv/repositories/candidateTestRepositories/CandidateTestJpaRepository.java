package com.nikita.taskcreateserviceforcv.repositories.candidateTestRepositories;

import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateTestJpaRepository extends JpaRepository<CandidateTest, Long> {
}
