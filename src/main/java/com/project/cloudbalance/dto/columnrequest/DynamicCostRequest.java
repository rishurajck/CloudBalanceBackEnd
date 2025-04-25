package com.project.cloudbalance.dto.columnrequest;

import lombok.Data;

import java.util.Map;

@Data
public class DynamicCostRequest {
    private String startDate;
    private String endDate;
    private Map<String, Object> filters;
}
