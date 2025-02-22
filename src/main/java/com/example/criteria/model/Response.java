package com.example.criteria.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @JsonProperty("applicationId")
    private String applicationId;
    @JsonProperty("tasks")
    private List<Task> tasks;
}
