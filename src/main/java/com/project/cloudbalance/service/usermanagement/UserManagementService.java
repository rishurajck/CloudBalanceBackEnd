package com.project.cloudbalance.service.usermanagement;

import com.project.cloudbalance.DTO.*;
import com.project.cloudbalance.entity.Accounts;
import com.project.cloudbalance.entity.Role;
import com.project.cloudbalance.entity.User;
import com.project.cloudbalance.repository.AccountsRepository;
import com.project.cloudbalance.repository.UserRepository;
import com.project.cloudbalance.util.UserEntitytoDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Service
public class UserManagementService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountsRepository accountsRepository;
    private final UserEntitytoDTO mapper = new UserEntitytoDTO();


    //create user
    public ResponseEntity<?> createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please Provide User Details Correctly");
        }
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        String password = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(password);
        User entity = mapper.map(userRequestDTO);
        if (userRequestDTO.getAccounts() != null && !userRequestDTO.getAccounts().isEmpty()) {
            List<Accounts> accounts = accountsRepository.findAllById(userRequestDTO.getAccounts());
            entity.setAccounts(accounts);
        }
        userRepository.save(entity);
        return ResponseEntity.ok().body(new ApiResponse(200, "User Created Successfully"));
    }


    // Get All User Details except accounts
    public ResponseEntity<?> getUser() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> response = users.stream()
                .map(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRole(String.valueOf(user.getRole()));
                    dto.setFirstname(user.getFirstname());
                    dto.setLastname(user.getLastname());
                    dto.setLastLogin(user.getLastLogin());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // Get Particular Customer Only
    public ResponseEntity<?> getCustomer(Long id)
    {
        Optional<User> customer = userRepository.findById(id);
        if(customer.isPresent())
        {
            CustomerResponseDTO responseDTO = new CustomerResponseDTO();
            User user = customer.get();
            responseDTO.setId(user.getId());
            responseDTO.setFirstname(user.getFirstname());
            responseDTO.setLastname(user.getLastname());
            responseDTO.setUsername(user.getUsername());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setRole(user.getRole().name());
            responseDTO.setLastLogin(user.getLastLogin());
            responseDTO.setAccounts(user.getAccounts());
            return ResponseEntity.ok().body(responseDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Get All Accounts
    public ResponseEntity<?> getAccounts()
    {
        List<Accounts> accounts = accountsRepository.findAll();
        if(!accounts.isEmpty())
        {
            return ResponseEntity.ok(accounts);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No accounts found");
        }
    }

    // add accounts
    public ResponseEntity<?> addAccounts(AccountsRequestDTO accountsRequestDTO)
    {
        if (accountsRequestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please Provide Account Details Correctly");
        }
        if (accountsRepository.findAccountByArn(accountsRequestDTO.getArn()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account already exists");
        }
        Accounts accounts = new Accounts();
        accounts.setArn(accountsRequestDTO.getArn());
        accounts.setAccountId(accountsRequestDTO.getAccountId());
        accounts.setAccountName(accountsRequestDTO.getAccountName());
        accountsRepository.save(accounts);
        return ResponseEntity.ok().body(new ApiResponse(200,"Account Created Successfully"));
    }

    // Update User
    public ResponseEntity<?> updateUser(Long id, UserRequestDTO userRequestDTO)
    {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
        {
            User existingUser = user.get();
            existingUser.setFirstname(userRequestDTO.getFirstname());
            existingUser.setLastname(userRequestDTO.getLastname());
            existingUser.setEmail(userRequestDTO.getEmail());
            existingUser.setUsername(userRequestDTO.getUsername());
            existingUser.setRole(Role.valueOf(userRequestDTO.getRole()));

            if (userRequestDTO.getAccounts() != null && !userRequestDTO.getAccounts().isEmpty()) {
                List<Accounts> accounts = accountsRepository.findAllById(userRequestDTO.getAccounts());
                existingUser.setAccounts(accounts);
            }
            userRepository.save(existingUser);
            return ResponseEntity.ok().body(new ApiResponse(200,"User Updated Successfully"));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

    }

    //get specific customer account for aws
    public ResponseEntity<?> getCustomerAccount(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
        {
            User particularUser = user.get();
            return ResponseEntity.ok().body(particularUser.getAccounts());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

    }
}
