package com.nikita.taskcreateserviceforcv.repositories;

import com.nikita.taskcreateserviceforcv.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestJpaRepository extends JpaRepository<Test, Long> {
}
