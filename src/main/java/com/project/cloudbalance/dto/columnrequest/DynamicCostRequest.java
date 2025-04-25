package com.project.cloudbalance.dto.columnrequest;

import lombok.Data;

import java.util.Map;

@Data
public class DynamicCostRequest {
//    private String startDate;
//    private String endDate;
    private String startMonth; // format: "YYYY-MM"
    private String endMonth;
    private Map<String, Object> filters;
}
