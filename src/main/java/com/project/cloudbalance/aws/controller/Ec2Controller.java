package com.project.cloudbalance.aws.controller;

import com.project.cloudbalance.aws.dto.Ec2Metadata;
import com.project.cloudbalance.aws.service.Ec2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/EC2")
public class Ec2Controller {

    @Autowired
    private Ec2Service ec2Service;

    @GetMapping("/metadata/{accountId}")
    public List<Ec2Metadata> getMetadata(@PathVariable Long accountId) {
        return ec2Service.getEc2InstancesByAccountId(accountId);
    }
}
