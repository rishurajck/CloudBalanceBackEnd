package com.project.cloudbalance.aws.controller;

import com.project.cloudbalance.aws.dto.Ec2Metadata;
import com.project.cloudbalance.aws.service.Ec2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ec2")
public class Ec2Controller {

    @Autowired
    private Ec2Service ec2Service;

    @GetMapping("/metadata")
    public List<Ec2Metadata> getMetadata(@RequestParam String roleArn) {
        return ec2Service.getEc2InstancesViaAssumedRole(roleArn);
    }
}
