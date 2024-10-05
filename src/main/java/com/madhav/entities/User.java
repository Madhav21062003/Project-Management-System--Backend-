package com.madhav.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    private String email;

    private String password;


    // This property helps to specify how many projects can user make
    // for free 3 only
    // With subscription have the additional quality
    private int projectSize;

    // One user can have multiple issues
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Issue> assignedIssues = new ArrayList<>();

    private String jwtToken;

    private String message;


}
