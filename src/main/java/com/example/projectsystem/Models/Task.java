package com.example.projectsystem.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks", schema = "public")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "startdate")
    private LocalDate startDate;
    @Column(name = "enddate")
    private LocalDate endDate;
    @Column(name = "adddate")
    private LocalDate addDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
