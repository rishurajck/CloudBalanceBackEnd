package com.project.cloudbalance.aws.service;

import com.project.cloudbalance.aws.dto.Ec2Metadata;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class Ec2Service {

    private static final Region FIXED_REGION = Region.US_EAST_1;

    public List<Ec2Metadata> getEc2InstancesViaAssumedRole(String roleArn) {
        StsClient stsClient = StsClient.builder()
                .region(FIXED_REGION)
                .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(roleArn)
                .roleSessionName("springbootSession")
                .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);
        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                assumeRoleResponse.credentials().accessKeyId(),
                assumeRoleResponse.credentials().secretAccessKey(),
                assumeRoleResponse.credentials().sessionToken()
        );

        Ec2Client ec2Client = Ec2Client.builder()
                .region(FIXED_REGION)
                .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .build();

        DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
        DescribeInstancesResponse response = ec2Client.describeInstances(request);

        List<Ec2Metadata> ec2List = new ArrayList<>();

        for (Reservation reservation : response.reservations()) {
            for (Instance instance : reservation.instances()) {
                String name = instance.tags().stream()
                        .filter(t -> t.key().equalsIgnoreCase("Name"))
                        .findFirst()
                        .map(Tag::value)
                        .orElse("N/A");

                ec2List.add(new Ec2Metadata(
                        instance.instanceId(),
                        name,
                        instance.placement().availabilityZone(),
                        instance.state().nameAsString()
                ));
            }
        }

        return ec2List;
    }
}
