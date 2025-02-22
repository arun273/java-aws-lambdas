package com.example.criteria;

import com.example.criteria.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RequestProcessorV2 {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProcessedResponse process(Request request, List<Response> responses) {
        Set<String> globalStatuses = extractGlobalStatuses(request);
        Map<String, String> specificFilters = extractSpecificFilters(request);
        extractGlobalKeys(request);

        List<List<ApplicationTask>> applications = responses.stream()
                .filter(res -> request.getApplicationId().contains(res.getApplicationId()))
                .map(res -> processResponses(res, globalStatuses, specificFilters))
                .filter(tasks -> !tasks.isEmpty())
                .toList();

        return new ProcessedResponse(applications);
    }

    private static void extractGlobalKeys(Request request) {
        request.getFilter().stream()
                .filter(f -> f.getKey() != null && !f.getKey().isBlank() && (f.getValue() == null || f.getValue().isBlank()))
                .forEach(f -> log.warn("Skipping filter '{}' as it has no value.", f.getKey()));
    }

    private static Set<String> extractGlobalStatuses(Request request) {
        return request.getFilter().stream()
                .filter(f -> f.getKey() == null || f.getKey().isBlank() || "*".equals(f.getKey()))
                .map(Filter::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static Map<String, String> extractSpecificFilters(Request request) {
        return request.getFilter().stream()
                .filter(f -> f.getKey() != null && !f.getKey().isBlank() && f.getValue() != null && !f.getValue().isBlank())
                .collect(Collectors.toMap(Filter::getKey, Filter::getValue, (v1, v2) -> v1));
    }

    private static List<ApplicationTask> processResponses(Response response, Set<String> globalStatuses, Map<String, String> specificFilters) {
        return response.getTasks().stream()
                .filter(task -> globalStatuses.contains(task.getStatus()) ||
                        specificFilters.getOrDefault(task.getState(), "").equals(task.getStatus()))
                .map(task -> new ApplicationTask(response.getApplicationId(), task.getState(), task.getStatus(), task.getDate()))
                .toList();
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
        log.info(objectMapper.writeValueAsString(processedResponse));
    }
}

