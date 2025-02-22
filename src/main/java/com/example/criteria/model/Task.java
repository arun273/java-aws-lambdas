package com.example.criteria.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @JsonProperty("state")
    private String state;
    @JsonProperty("status")
    private String status;
    @JsonProperty("date")
    private String date;
}
