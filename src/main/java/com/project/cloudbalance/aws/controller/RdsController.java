package com.project.cloudbalance.aws.controller;

import com.project.cloudbalance.aws.dto.RdsInstanceInfo;
import com.project.cloudbalance.aws.service.RdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/RDS")
public class RdsController {

    @Autowired
    private RdsService rdsService;

    @GetMapping("/{accountId}")
    public List<RdsInstanceInfo> getRdsInstances(@PathVariable Long accountId)
    {
        return rdsService.getEc2InstancesByAccountId(accountId);
    }
}
