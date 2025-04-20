package com.project.cloudbalance.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ec2Metadata {
    private String instanceId;
    private String name;
    private String region;
    private String state;
}
