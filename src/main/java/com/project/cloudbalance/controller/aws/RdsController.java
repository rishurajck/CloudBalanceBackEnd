package com.project.cloudbalance.controller.aws;

import com.project.cloudbalance.dto.aws.RdsInstanceInfo;
import com.project.cloudbalance.service.awsservice.RdsService;
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
