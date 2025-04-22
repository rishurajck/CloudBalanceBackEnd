package com.project.cloudbalance.controller.usermanagement;

import com.project.cloudbalance.DTO.AccountsRequestDTO;
import com.project.cloudbalance.DTO.CustomerResponseDTO;
import com.project.cloudbalance.DTO.UserRequestDTO;
import com.project.cloudbalance.service.usermanagement.UserManagementService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserManagementController {

    private final UserManagementService userManagementService;
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/createuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userManagementDTO)
    {
        return userManagementService.createUser(userManagementDTO);
    }

    @GetMapping("/usermanagement")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<?> getUser()
    {
        return userManagementService.getUser();
    }

    @GetMapping("/editUser/{id}")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<?> getCustomer(@PathVariable Long id)
    {
        return userManagementService.getCustomer(id);
    }

    @GetMapping("accounts")
    @PreAuthorize("hasRole('READ_ONLY') or hasRole('ADMIN')")
    public ResponseEntity<?> getAccounts()
    {
        return userManagementService.getAccounts();
    }

    @PostMapping("addAccounts")
    @RolesAllowed({"hasRole('ADMIN')"})
    public ResponseEntity<?> addAccounts(@RequestBody AccountsRequestDTO accountsRequestDTO)
    {
        return userManagementService.addAccounts(accountsRequestDTO);
    }

    @PutMapping("updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO)
    {
        return userManagementService.updateUser(id, userRequestDTO);
    }

    @GetMapping("account/{username}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerAccount(@PathVariable String username) {
        return userManagementService.getCustomerAccount(username);
    }


}
