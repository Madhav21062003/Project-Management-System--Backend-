package com.madhav.dto;

import com.madhav.entities.Project;
import com.madhav.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDTO {

    private Long id;

    private String title;

    private String description;

    private String status;

    private Long projectID;

    private String priority;

    private LocalDate dueDate;

    public List<String> tags = new ArrayList<>();

    private Project project;

    private User assignee;



}
