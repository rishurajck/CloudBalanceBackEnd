package com.project.cloudbalance.service.awsservice;

import com.project.cloudbalance.dto.aws.AutoScalingGroupInfo;
import com.project.cloudbalance.entity.Accounts;
import com.project.cloudbalance.repository.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsResponse;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AsgService {

    @Autowired
    private AccountsRepository accountsRepository;

    public List<AutoScalingGroupInfo> getAsgInstancesByAccountId(Long accountId) {
        Accounts account = accountsRepository.findAccountsByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        return getAutoScalingGroupsViaAssumedRole(account.getArn());
    }
    private static final Region FIXED_REGION = Region.US_EAST_1;

    public List<AutoScalingGroupInfo> getAutoScalingGroupsViaAssumedRole(String roleArn) {
        // 1. Assume role
        StsClient stsClient = StsClient.builder()
                .region(FIXED_REGION)
                .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName("springbootAsgSession")
                .build());

        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                assumeRoleResponse.credentials().accessKeyId(),
                assumeRoleResponse.credentials().secretAccessKey(),
                assumeRoleResponse.credentials().sessionToken()
        );

        // 2. Build ASG Client
        AutoScalingClient asgClient = AutoScalingClient.builder()
                .region(FIXED_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .build();

        // 3. Fetch Auto Scaling Groups
        DescribeAutoScalingGroupsResponse response = asgClient.describeAutoScalingGroups();

        List<AutoScalingGroupInfo> asgList = new ArrayList<>();

        for (var asg : response.autoScalingGroups()) {
            asgList.add(new AutoScalingGroupInfo(
                    asg.autoScalingGroupARN(),
                    asg.autoScalingGroupName(),
                    FIXED_REGION.toString(),
                    asg.desiredCapacity(),
                    asg.minSize(),
                    asg.maxSize(),
                    asg.status()
            ));

        }
        System.out.println(asgList);
        return asgList;
    }

}
