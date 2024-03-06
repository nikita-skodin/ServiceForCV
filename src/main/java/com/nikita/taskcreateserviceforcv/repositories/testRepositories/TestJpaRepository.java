package com.nikita.taskcreateserviceforcv.repositories.testRepositories;

import com.nikita.taskcreateserviceforcv.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestJpaRepository extends JpaRepository<Test, Long> {
    Optional<Test> findByTitle(String title);
}
