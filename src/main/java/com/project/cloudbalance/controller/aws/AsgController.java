package com.project.cloudbalance.controller.aws;

import com.project.cloudbalance.dto.aws.AutoScalingGroupInfo;
import com.project.cloudbalance.service.awsservice.AsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ASG")
public class AsgController {

    @Autowired
    private AsgService asgService;

    @GetMapping("/{accountId}")
    public List<AutoScalingGroupInfo> getAsgMetadata(@PathVariable Long accountId) {
        return asgService.getAsgInstancesByAccountId(accountId);
    }
}
