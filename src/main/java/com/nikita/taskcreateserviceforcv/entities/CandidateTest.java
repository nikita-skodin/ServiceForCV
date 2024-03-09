package com.nikita.taskcreateserviceforcv.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "candidates_tests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    Test test;

    @Column(name = "date_of_passing", columnDefinition = "DATE DEFAULT CURRENT_DATE", nullable = false)
    LocalDate dateOfPassing;

    @Column(nullable = false)
    Integer score;
}
