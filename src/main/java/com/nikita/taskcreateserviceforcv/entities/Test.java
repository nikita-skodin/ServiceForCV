package com.nikita.taskcreateserviceforcv.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(exclude = {"applicableAreas", "attemptsList"})
@EqualsAndHashCode(exclude = {"attemptsList", "applicableAreas"})
@Entity
@Table(name = "tests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    String description;

    @ManyToMany
    @JoinTable(
            name = "compatible_tests_and_areas",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    Set<Area> applicableAreas = new HashSet<>();

    @OneToMany(mappedBy = "test")
    List<CandidateTest> attemptsList = new ArrayList<>();

}