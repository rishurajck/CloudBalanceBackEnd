package com.project.cloudbalance.controller.aws;

import com.project.cloudbalance.dto.aws.Ec2Metadata;
import com.project.cloudbalance.service.awsservice.Ec2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/EC2")
public class Ec2Controller {

    @Autowired
    private Ec2Service ec2Service;

    @GetMapping("/{accountId}")
    public List<Ec2Metadata> getMetadata(@PathVariable Long accountId) {
        return ec2Service.getEc2InstancesByAccountId(accountId);
    }
}
