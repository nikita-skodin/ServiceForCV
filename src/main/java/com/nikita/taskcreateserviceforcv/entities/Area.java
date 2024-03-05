package com.nikita.taskcreateserviceforcv.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Entity
@Table(name = "areas")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "title must not be blank")
    @Size(min = 3, max = 50,
            message = "title`s length must be between 3 and 50 chars")
    @Column(nullable = false)
    String title;

    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    @ManyToMany(mappedBy = "applicableAreas")
    List<Test> applicableTests;

    @ManyToMany(mappedBy = "possibleAreas")
    List<Candidate> candidatesForArea;
}
