package com.project.cloudbalance.repository;

import com.project.cloudbalance.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findAccountByArn(String arn);
    Optional<Accounts> findAccountsByAccountId(Long accountId);

}
