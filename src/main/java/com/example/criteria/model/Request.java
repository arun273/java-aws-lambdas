package com.example.criteria.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @JsonProperty("applicationId")
    private List<String> applicationId;
    @JsonProperty("filter")
    private List<Filter> filter;
}
