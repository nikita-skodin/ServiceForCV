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

    @NotBlank(message = "name must not be empty")
    @Size(min = 1, max = 20,
            message = "name`s length must be between 1 and 20 chars")
    @Column(nullable = false)
    String name;

    @NotBlank(message = "lastname must not be empty")
    @Size(min = 1, max = 20,
            message = "lastname`s length must be between 1 and 20 chars")
    @Column(nullable = false)
    String lastname;

    @NotBlank(message = "patronymic must not be empty")
    @Size(min = 1, max = 20,
            message = "patronymic`s length must be between 1 and 20 chars")
    @Column(nullable = false)
    String patronymic;

    // TODO
    byte[] photo;

    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    // TODO
    byte[] cvFile;

    @ManyToMany
    @JoinTable(
            name = "possible_areas_for_candidates",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    Set<Area> possibleAreas = new HashSet<>();

    @OneToMany(mappedBy = "candidate")
    List<CandidateTest> candidateTests = new ArrayList<>();

}
