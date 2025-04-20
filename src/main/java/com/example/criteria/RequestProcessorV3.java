package com.example.criteria;

import com.example.criteria.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RequestProcessorV3 {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProcessedResponse process(Request request, List<Response> responses) {
        FilterData filterData = processFilters(request);

        List<List<ApplicationTask>> applications = responses.stream()
                .filter(res -> request.getApplicationId().contains(res.getApplicationId()))
                .map(res -> processResponses(res, filterData))
                .filter(tasks -> !tasks.isEmpty())
                .toList();

        return new ProcessedResponse(applications);
    }

    /**
     * Extracts global statuses, specific filters, and logs invalid filters.
     */
    private static FilterData processFilters(Request request) {
        // Partition filters into global and specific in one pass
        Map<Boolean, List<Filter>> partitionedFilters = request.getFilter().stream()
                .collect(Collectors.partitioningBy(f -> f.getKey() == null || f.getKey().isBlank() || "*".equals(f.getKey())));

        // Extract Global Statuses (Non-null, non-blank values)
        Set<String> globalStatuses = partitionedFilters.get(true).stream()
                .map(Filter::getValue)
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.toSet());

        // Extract Specific Filters (Valid key-value pairs)
        Map<String, String> specificFilters = partitionedFilters.get(false).stream()
                .filter(f -> f.getValue() != null && !f.getValue().isBlank())
                .collect(Collectors.toMap(Filter::getKey, Filter::getValue, (v1, v2) -> v1)); // Avoids duplicate keys

        // Log Invalid Filters (Keys without a valid value)
        partitionedFilters.get(false).stream()
                .filter(f -> f.getValue() == null || f.getValue().isBlank())
                .forEach(f -> log.warn("Skipping filter '{}' as it has no value.", f.getKey()));

        return new FilterData(globalStatuses, specificFilters);
    }


    /**
     * Processes response tasks and filters relevant ones.
     */
    private static List<ApplicationTask> processResponses(Response response, FilterData filterData) {
        return response.getTasks().stream()
                .filter(task -> filterData.globalStatuses.contains(task.getStatus()) ||
                        filterData.specificFilters.getOrDefault(task.getState(), "").equals(task.getStatus()))
                .map(task -> new ApplicationTask(response.getApplicationId(), task.getState(), task.getStatus(), task.getDate()))
                .toList();
    }

    public static ProcessedResponse processRequests(String requestJson, String responseJson) throws Exception {
        Request request = objectMapper.readValue(requestJson, Request.class);
        List<Response> responses = objectMapper.readValue(responseJson, new TypeReference<>() {});
        return process(request, responses);
    }

    public static void main(String[] args) throws Exception {
        String requestJson = "{\"applicationId\":[\"202545658975\",\"202545658974\",\"202545658988\",\"202545658995\"],\"filter\":[{\"key\":\"*\",\"value\":\"Completed\"},{\"key\":\"carCheck\"},{\"key\":\"autoCheck\",\"value\":\"Completed\"},{\"key\":\"healthCheck\",\"value\":\"Under Review\"},{\"key\":\"*\",\"value\":\"Completed\"},{\"value\":\"In Progress\"},{\"value\":\"Not Started\"},{\"value\":\"Failed\"}]}";
        String responseJson = "[{\"applicationId\":\"202545658975\",\"tasks\":[{\"state\":\"autoCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T08:45:10Z\"},{\"state\":\"healthCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T06:45:10Z\"},{\"state\":\"bikeCheck\",\"status\":\"In Progress\",\"date\":\"2025-02-22T02:45:10Z\"},{\"state\":\"carCheck\",\"status\":\"Under Review\",\"date\":\"2025-02-22T07:45:10Z\"},{\"state\":\"policyCheck\",\"status\":\"Completed\",\"date\":\"2025-02-22T10:45:10Z\"},{\"state\":\"localCheck\",\"status\":\"Failed\",\"date\":\"2025-02-21T09:45:10Z\"}]},{\"applicationId\":\"202545658988\",\"tasks\":[{\"state\":\"autoCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T08:45:10Z\"},{\"state\":\"healthCheck\",\"status\":\"Not Started\",\"date\":\"2025-02-22T06:45:10Z\"},{\"state\":\"bikeCheck\",\"status\":\"In Progress\",\"date\":\"2025-02-22T02:45:10Z\"},{\"state\":\"carCheck\",\"status\":\"Under Review\",\"date\":\"2025-02-22T07:45:10Z\"},{\"state\":\"policyCheck\",\"status\":\"Completed\",\"date\":\"2025-02-22T10:45:10Z\"},{\"state\":\"localCheck\",\"status\":\"Failed\",\"date\":\"2025-02-21T09:45:10Z\"}]}]";
        ProcessedResponse processedResponse = processRequests(requestJson, responseJson);
        log.info(objectMapper.writeValueAsString(processedResponse));
    }

    /**
     * Encapsulates extracted filter data for better organization.
     */
    private record FilterData(Set<String> globalStatuses, Map<String, String> specificFilters){

    }
}
