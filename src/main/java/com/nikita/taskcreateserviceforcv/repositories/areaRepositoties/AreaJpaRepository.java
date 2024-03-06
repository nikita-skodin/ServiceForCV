package com.nikita.taskcreateserviceforcv.repositories.areaRepositoties;

import com.nikita.taskcreateserviceforcv.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaJpaRepository extends JpaRepository<Area, Long> {

    Optional<Area> findByTitle(String title);

}
