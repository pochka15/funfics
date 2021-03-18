package com.pochka15.funfics.controller;

import com.pochka15.funfics.dto.UserForAdminTableDto;
import com.pochka15.funfics.dto.form.UserRolesForm;
import com.pochka15.funfics.service.users.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/users")
    public List<UserForAdminTableDto> allUsers() {
        return adminService.allUsers();
    }

    @GetMapping("/admin/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return okOrBadResponse(() -> adminService.deleteUserById(id));
    }

    @GetMapping("/admin/fetch-user")
    public ResponseEntity<?> fetchUser(@RequestParam Long id) {
        var fetched = adminService.fetchUserById(id);
        return fetched.isPresent()
                ? ResponseEntity.ok(fetched.get())
                : ResponseEntity.badRequest().build();
    }

    @GetMapping("/admin/block-user")
    public ResponseEntity<?> blockUser(@RequestParam Long id) {
        return okOrBadResponse(() -> adminService.blockUserById(id));
    }

    @GetMapping("/admin/make-admin")
    public ResponseEntity<?> makeAdmin(@RequestParam Long id) {
        return okOrBadResponse(() -> adminService.makeAdminById(id));
    }

    @PostMapping("/admin/set-user-roles")
    public ResponseEntity<?> setRoles(@RequestBody UserRolesForm form) {
        return okOrBadResponse(() -> adminService.setUserRoles(form.getId(), form.getRoles()));
    }

    private ResponseEntity<?> okOrBadResponse(Supplier<Boolean> funcWithBooleanResult) {
        return funcWithBooleanResult.get()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().build();
    }
}
