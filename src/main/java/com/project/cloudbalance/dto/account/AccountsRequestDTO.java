package com.project.cloudbalance.dto.account;

import lombok.Data;

@Data
public class AccountsRequestDTO {
    private String arn;
    private Long accountId;
    private String accountName;
}
