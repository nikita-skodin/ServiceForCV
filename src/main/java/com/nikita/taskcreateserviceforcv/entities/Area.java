package com.nikita.taskcreateserviceforcv.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"applicableTests", "candidatesForArea"})
@Entity
@Table(name = "areas")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    String description;

    @ManyToMany(mappedBy = "applicableAreas")
    Set<Test> applicableTests = new HashSet<>();

    @ManyToMany(mappedBy = "possibleAreas")
    List<Candidate> candidatesForArea = new ArrayList<>();
}
