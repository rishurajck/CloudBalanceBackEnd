package com.project.cloudbalance.dto.aws;

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

    private String status;
    private String region;


}
