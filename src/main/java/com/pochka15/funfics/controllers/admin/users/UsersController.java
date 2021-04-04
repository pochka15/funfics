package com.pochka15.funfics.controllers.admin.users;

import com.pochka15.funfics.dto.UserForAdmin;
import com.pochka15.funfics.dto.form.UserRolesForm;
import com.pochka15.funfics.exceptions.UserNotFound;
import com.pochka15.funfics.services.users.AdminService;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pochka15.funfics.configs.SpringFoxConfig.API_KEY_NAME;

@RestController
@RequestMapping("/admin/users")
public class UsersController {
    private final AdminService adminService;

    public UsersController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @Authorization(API_KEY_NAME)
    public List<UserForAdmin> allUsers() {
        return adminService.allUsers();
    }

    @DeleteMapping("/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        adminService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @Authorization(API_KEY_NAME)
    public UserForAdmin fetchUser(@PathVariable Long userId) throws UserNotFound {
        return adminService.fetchUserById(userId);
    }

    @GetMapping("/block/{userId}")
    @Authorization(API_KEY_NAME)
    public UserForAdmin blockUser(@PathVariable Long userId) throws UserNotFound {
        return adminService.blockUserById(userId);
    }

    @GetMapping("/unblock/{userId}")
    @Authorization(API_KEY_NAME)
    public UserForAdmin unblockUser(@PathVariable Long userId) throws UserNotFound {
        return adminService.unblockUserById(userId);
    }

    @GetMapping("/make-admin/{userId}")
    @Authorization(API_KEY_NAME)
    public UserForAdmin makeAdmin(@PathVariable Long userId) throws UserNotFound {
        return adminService.makeAdminById(userId);
    }

    @PostMapping("/set-roles")
    @Authorization(API_KEY_NAME)
    public UserForAdmin setRoles(@RequestBody UserRolesForm form) throws UserNotFound {
        return adminService.setUserRoles(form.getId(), form.getRoles());
    }
}
