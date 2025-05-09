package com.project.cloudbalance.controller.usermanagement;

import com.project.cloudbalance.dto.account.AccountsRequestDTO;
import com.project.cloudbalance.dto.api.ApiResponse;
import com.project.cloudbalance.dto.customer.CustomerResponseDTO;
import com.project.cloudbalance.dto.switchuser.SwitchUserRequestDTO;
import com.project.cloudbalance.dto.user.UserRequestDTO;
import com.project.cloudbalance.dto.user.UserResponseDTO;
import com.project.cloudbalance.entity.Accounts;
import com.project.cloudbalance.service.usermanagement.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserManagementController {

    private final UserManagementService userManagementService;
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/createuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid UserRequestDTO userManagementDTO)
    {
        ApiResponse response = userManagementService.createUser(userManagementDTO);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/usermanagement")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUser()
    {
        List<UserResponseDTO> response = userManagementService.getUser();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/editUser/{id}")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id)
    {
        CustomerResponseDTO response = userManagementService.getCustomer(id);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("accounts")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<List<Accounts>> getAccounts()
    {
          List<Accounts> response = userManagementService.getAccounts();
          return ResponseEntity.ok().body(response);
    }

    @PostMapping("addAccounts")
    @RolesAllowed({"hasRole('ADMIN')"})
    public ResponseEntity<ApiResponse> addAccounts(@RequestBody @Valid AccountsRequestDTO accountsRequestDTO)
    {
        ApiResponse response = userManagementService.addAccounts(accountsRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO)
    {
        ApiResponse response = userManagementService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/account/{username}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<Accounts>> getCustomerAccount(@PathVariable String username) {
        List<Accounts> response = userManagementService.getCustomerAccount(username);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("switch-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> switchUser(@RequestBody SwitchUserRequestDTO switchUserRequestDTO)
    {
        return userManagementService.switchUser(switchUserRequestDTO);
    }

    @GetMapping("/admin/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCustomers() {
        List<UserResponseDTO> customers = userManagementService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }


}
