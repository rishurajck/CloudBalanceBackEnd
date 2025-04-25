package com.project.cloudbalance.service.awsservice;

import com.project.cloudbalance.dto.aws.RdsInstanceInfo;
import com.project.cloudbalance.entity.Accounts;
import com.project.cloudbalance.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.*;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.*;

import java.util.List;

@Service
public class RdsService {

    @Autowired
    private AccountsRepository accountsRepository;
    public List<RdsInstanceInfo> getEc2InstancesByAccountId(Long accountId) {
        Accounts account = accountsRepository.findAccountsByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));

        return getRdsInstancesViaAssumedRole(account.getArn());
    }
    private static final Region FIXED_REGION = Region.US_EAST_1;

    public List<RdsInstanceInfo> getRdsInstancesViaAssumedRole(String roleArn) {
        // 1. Assume Role
        StsClient stsClient = StsClient.builder()
                .region((FIXED_REGION))
                .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName("springbootRdsSession")
                .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);

        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                assumeRoleResponse.credentials().accessKeyId(),
                assumeRoleResponse.credentials().secretAccessKey(),
                assumeRoleResponse.credentials().sessionToken()
        );

        // 2. Create RDS Client with temporary credentials
        RdsClient rdsClient = RdsClient.builder()
                .region(FIXED_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .build();

        // 3. Fetch RDS instances
        DescribeDbInstancesRequest request = DescribeDbInstancesRequest.builder().build();
        DescribeDbInstancesResponse response = rdsClient.describeDBInstances(request);

        List<RdsInstanceInfo> rdsList = response.dbInstances()
                .stream()
                .map(db->
                        new RdsInstanceInfo(
                                db.dbInstanceArn(),
                                db.dbInstanceIdentifier(),
                                db.engine(), db.dbInstanceStatus(), "us-east-1" ))
                .toList();

        return rdsList;
    }
}
