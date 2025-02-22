package com.example.criteria;

import com.example.criteria.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class RequestProcessor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProcessedResponse process(Request request, List<Response> responses) {
        // Extract global statuses where key == "*" or key is null/empty
        Set<String> globalStatuses = request.getFilter().stream()
                .filter(f -> f.getKey() == null || f.getKey().isBlank() || "*".equals(f.getKey()))
                .map(Filter::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Extract specific filters (state -> status mapping)
        Map<String, String> specificFilters = request.getFilter().stream()
                .filter(f -> f.getKey() != null && !f.getKey().isBlank() && f.getValue() != null && !f.getValue().isBlank())
                .collect(Collectors.toMap(Filter::getKey, Filter::getValue, (v1, v2) -> v1));

        // Process responses to filter matching tasks
        List<List<ApplicationTask>> applications = responses.stream()
                .filter(res -> request.getApplicationId().contains(res.getApplicationId()))
                .map(res -> res.getTasks().stream()
                        .filter(task -> globalStatuses.contains(task.getStatus()) ||
                                specificFilters.getOrDefault(task.getState(), "").equals(task.getStatus()))
                        .map(task -> new ApplicationTask(res.getApplicationId(), task.getState(), task.getStatus(), task.getDate()))
                        .toList())
                .filter(tasks -> !tasks.isEmpty())
                .toList();

        return new ProcessedResponse(applications);
    }

    public static ProcessedResponse processRequests(String requestJson, String responseJson) throws Exception {
        Request request = objectMapper.readValue(requestJson, Request.class);
        List<Response> responses = objectMapper.readValue(responseJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Response.class));
        return process(request, responses);
    }

    public static void main(String[] args) throws Exception {
        String requestJson = "{\"applicationId\":[\"202545658975\",\"202545658974\",\"202545658988\",\"202545658995\"],\"filter\":[{\"key\":\"*\",\"value\":\"Completed\"},{\"key\":\"carCheck\"},{\"key\":\"autoCheck\",\"value\":\"Completed\"},{\"key\":\"healthCheck\",\"value\":\"Under Review\"},{\"key\":\"*\",\"value\":\"Completed\"},{\"value\":\"In Progress\"},{\"value\":\"Not Started\"},{\"value\":\"Failed\"}]}";
        String responseJson = "[{\"applicationId\":\"202545658975\",\"tasks\":[{\"state\":\"autoCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T08:45:10Z\"},{\"state\":\"healthCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T06:45:10Z\"},{\"state\":\"bikeCheck\",\"status\":\"In Progress\",\"date\":\"2025-02-22T02:45:10Z\"},{\"state\":\"carCheck\",\"status\":\"Under Review\",\"date\":\"2025-02-22T07:45:10Z\"},{\"state\":\"policyCheck\",\"status\":\"Completed\",\"date\":\"2025-02-22T10:45:10Z\"},{\"state\":\"localCheck\",\"status\":\"Failed\",\"date\":\"2025-02-21T09:45:10Z\"}]},{\"applicationId\":\"202545658988\",\"tasks\":[{\"state\":\"autoCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T08:45:10Z\"},{\"state\":\"healthCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T06:45:10Z\"},{\"state\":\"bikeCheck\",\"status\":\"In Progress\",\"date\":\"2025-02-22T02:45:10Z\"},{\"state\":\"carCheck\",\"status\":\"Under Review\",\"date\":\"2025-02-22T07:45:10Z\"},{\"state\":\"policyCheck\",\"status\":\"Completed\",\"date\":\"2025-02-22T10:45:10Z\"},{\"state\":\"localCheck\",\"status\":\"Failed\",\"date\":\"2025-02-21T09:45:10Z\"}]}]";
        ProcessedResponse processedResponse = processRequests(requestJson, responseJson);
        System.out.println(objectMapper.writeValueAsString(processedResponse));
    }
}

