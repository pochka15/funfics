package com.pochka15.funfics.controllers.admin.users;

import com.pochka15.funfics.dto.UserForAdminTableDto;
import com.pochka15.funfics.dto.form.UserRolesForm;
import com.pochka15.funfics.services.users.AdminService;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

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
    public List<UserForAdminTableDto> allUsers() {
        return adminService.allUsers();
    }

    @DeleteMapping("/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return okOrBadResponse(() -> adminService.deleteUserById(userId));
    }

    @GetMapping("/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<UserForAdminTableDto> fetchUser(@PathVariable Long userId) {
        var fetched = adminService.fetchUserById(userId);
        return fetched.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/block/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> blockUser(@PathVariable Long userId) {
        return okOrBadResponse(() -> adminService.blockUserById(userId));
    }

    @GetMapping("/unblock/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> unblockUser(@PathVariable Long userId) {
        return okOrBadResponse(() -> adminService.unblockUserById(userId));
    }

    @GetMapping("/make-admin/{userId}")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> makeAdmin(@PathVariable Long userId) {
        return okOrBadResponse(() -> adminService.makeAdminById(userId));
    }

    @PostMapping("/set-roles")
    @Authorization(API_KEY_NAME)
    public ResponseEntity<?> setRoles(@RequestBody UserRolesForm form) {
        return okOrBadResponse(() -> adminService.setUserRoles(form.getId(), form.getRoles()));
    }

    private ResponseEntity<?> okOrBadResponse(Supplier<Boolean> funcWithBooleanResult) {
        return funcWithBooleanResult.get()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
