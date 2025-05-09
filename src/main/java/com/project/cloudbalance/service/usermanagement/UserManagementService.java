package com.project.cloudbalance.service.usermanagement;

import com.project.cloudbalance.dto.account.AccountsRequestDTO;
import com.project.cloudbalance.dto.api.ApiResponse;
import com.project.cloudbalance.dto.customer.CustomerResponseDTO;
import com.project.cloudbalance.dto.login.LoginResponseDTO;
import com.project.cloudbalance.dto.switchuser.SwitchUserRequestDTO;
import com.project.cloudbalance.dto.switchuser.SwitchUserResponseDTO;
import com.project.cloudbalance.dto.user.UserRequestDTO;
import com.project.cloudbalance.dto.user.UserResponseDTO;
import com.project.cloudbalance.entity.Accounts;
import com.project.cloudbalance.entity.Role;
import com.project.cloudbalance.entity.User;
import com.project.cloudbalance.exception.customException.*;
import com.project.cloudbalance.repository.AccountsRepository;
import com.project.cloudbalance.repository.UserRepository;
import com.project.cloudbalance.security.AuthService;
import com.project.cloudbalance.security.UserDetailsImpl;
import com.project.cloudbalance.security.jwt.JwtUtils;
import com.project.cloudbalance.util.UserEntitytoDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final JwtUtils jwtUtils;
    private final AuthService authService;

    //create user
    public ApiResponse createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            throw new RequestDtoNull("Please Provide the details");
        }
        if (userRequestDTO.getAccounts() != null &&
                !userRequestDTO.getAccounts().isEmpty() &&
                ("ADMIN".equalsIgnoreCase(userRequestDTO.getRole()) || "READ_ONLY".equalsIgnoreCase(userRequestDTO.getRole()))) {
            throw new RequestDtoNull("Wrong Role! Account can only be assigned to CUSTOMER Role");
        }

        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String password = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(password);
        User entity = mapper.map(userRequestDTO);

        if ("CUSTOMER".equalsIgnoreCase(userRequestDTO.getRole())) {
            if (userRequestDTO.getAccounts() != null && !userRequestDTO.getAccounts().isEmpty()) {
                List<Accounts> accounts = accountsRepository.findAllById(userRequestDTO.getAccounts());
                entity.setAccounts(accounts);
            }
        }
        userRepository.save(entity);
        return new ApiResponse(200, "User Created Successfully");
    }

    // Get All User Details except accounts
    public List<UserResponseDTO> getUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
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
    }

    // Get Particular Customer Only
    public CustomerResponseDTO getCustomer(Long id)
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
            return responseDTO;
        }
        else {
            throw new UsernameNotFoundException("User Not found");
        }
    }


    // Get All Accounts
    public List<Accounts> getAccounts()
    {
        List<Accounts> accounts = accountsRepository.findAll();
        if(!accounts.isEmpty())
        {
            return accounts;
        }
        else {
            throw new AccountsNotFound("Not accounts Found");
        }
    }

    // add accounts
    public ApiResponse addAccounts(AccountsRequestDTO accountsRequestDTO)
    {
        if (accountsRequestDTO == null )
        {
            throw new RequestDtoNull("Please Provide the Details Properly");
        }
        if (accountsRepository.findAccountByArn(accountsRequestDTO.getArn()).isPresent())
        {
            throw new AccountsAlreadyExists("Account already Exists");
        }
        Accounts accounts = new Accounts();
        accounts.setArn(accountsRequestDTO.getArn());
        accounts.setAccountId(accountsRequestDTO.getAccountId());
        accounts.setAccountName(accountsRequestDTO.getAccountName());
        accountsRepository.save(accounts);
        return new ApiResponse(200,"Account Created Successfully");
    }

    // Update User
    public ApiResponse updateUser(Long id, UserRequestDTO userRequestDTO)
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
            existingUser.getAccounts().clear();
            if (userRequestDTO.getAccounts() != null && !userRequestDTO.getAccounts().isEmpty()) {
                List<Accounts> accounts = accountsRepository.findAllById(userRequestDTO.getAccounts());
                existingUser.setAccounts(accounts);
            }
            userRepository.save(existingUser);
            return new ApiResponse(200,"User Updated Successfully");
        }
        else {
            throw new UsernameNotFoundException("User Not Found");
        }

    }

    //get specific customer account for aws
    public List<Accounts> getCustomerAccount(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent())
        {
            User particularUser = user.get();
            List<Accounts> result = particularUser.getAccounts();
            return result;
        }
        else
        {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }

    }

    // switch-user
    public ResponseEntity<?> switchUser(SwitchUserRequestDTO switchUserRequestDTO)
    {
        String targetUsername = switchUserRequestDTO.getUsername();
        String targetRole = switchUserRequestDTO.getRole();
        Optional<User> user  = userRepository.findByUsername(targetUsername);
        User res = user.get();
        if(targetUsername.equalsIgnoreCase(user.get().getUsername()))
        {
            UserDetailsImpl targetUser = (UserDetailsImpl) authService.loadUserByUsername(targetUsername);

            if (targetUser.getAuthorities().stream()
                    .noneMatch(auth -> auth.getAuthority().equals("ROLE_" + targetRole.toUpperCase()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "User does not have the specified role."));
            }

            String impersonationToken = jwtUtils.generateToken(
                    targetUser.getUsername(),
                    targetRole.toUpperCase()
            );

            SwitchUserResponseDTO responseDTO = new SwitchUserResponseDTO();
            responseDTO.setUsername(res.getUsername());
            responseDTO.setRole(res.getRole().name());
            responseDTO.setFirstname(res.getFirstname());
            responseDTO.setToken(impersonationToken);




            // Create response with token + full user info
            Map<String, Object> response = new HashMap<>();
            response.put("data", responseDTO);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
    }

    public List<UserResponseDTO> getAllCustomers() {
        List<User> customers = userRepository.findByRoleIgnoreCase();

        return customers.stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole().name());
            dto.setLastLogin(user.getLastLogin());

            return dto;
        }).toList();
    }
}

