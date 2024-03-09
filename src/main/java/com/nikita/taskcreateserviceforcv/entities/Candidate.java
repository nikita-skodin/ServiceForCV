package com.nikita.taskcreateserviceforcv.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(exclude = {"possibleAreas", "candidateTests"})
@Entity
@Table(name = "candidates")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String lastname;

    @Column(nullable = false)
    String patronymic;

    String photo;

    String description;

    String cvFile;

    @ManyToMany
    @JoinTable(
            name = "possible_areas_for_candidates",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    Set<Area> possibleAreas = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    List<CandidateTest> candidateTests = new ArrayList<>();

}
