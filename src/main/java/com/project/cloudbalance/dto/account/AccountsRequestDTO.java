package com.project.cloudbalance.dto.account;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountsRequestDTO {
    @NotBlank(message = "Arn is Mandatory")
    @Pattern(
            regexp = "^arn:aws:iam::\\d{12}:role\\/\\w+$",
            message = "Check Your Arn Format"
    )
    private String arn;

    @NotNull(message = "Account Id is Mandatory")
    @Min(value = 100000000000L, message = "Account ID must be 12 digits")
    @Max(value = 999999999999L, message = "Account ID must be exactly 12 digits")
    private Long accountId;

    @NotBlank(message = "Account Name is Mandatory")
    private String accountName;
}
