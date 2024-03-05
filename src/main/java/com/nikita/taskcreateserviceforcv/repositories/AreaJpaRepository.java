package com.nikita.taskcreateserviceforcv.repositories;

import com.nikita.taskcreateserviceforcv.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaJpaRepository extends JpaRepository<Area, Long> {
}
