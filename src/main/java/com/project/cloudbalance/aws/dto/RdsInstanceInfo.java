package com.project.cloudbalance.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RdsInstanceInfo {
    private String resourceId;
    private String resourceName;
    private String engine;
    private String region;
    private String status;


}
