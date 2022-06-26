package io.login.server.controllers.secured;

import io.login.client.models.*;
import io.login.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserManagementController {
    @Autowired
    private IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserAccount> getUserInfo() {
        return null;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserAccount> getMyUserInfo() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAuthContext userAuthContext = (UserAuthContext) object;
        UserAccount userRequest = userAuthContext.getUserRequest();
        return ResponseEntity.ok(userRequest);
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is ADMIN
     *
     * @return
     */
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserAccount userRequest) {
        return null;
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is USER
     *
     * @return
     */
    @PostMapping("/register-user")
    @Transactional
    public ResponseEntity<UserAccount> registerUser(@RequestBody UserAccount userRequest) {
        this.userService.createUser(userRequest);
        this.userService.saveUserRoleMapping(userRequest);
        return ResponseEntity.ok(userRequest);
    }

    @PostMapping("/update-role")
    public ResponseEntity<RoleUpdate> updateUserRole(@RequestBody RoleUpdate updateUserRole) {
        this.userService.updateUserRole(updateUserRole);
        return ResponseEntity.ok(updateUserRole);
    }

    @PostMapping("/user-profile-details")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        this.userService.createUserProfileDetails(userProfile);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/update-user-profile-details/{uuid}")
    public ResponseEntity<UserProfile> updateResourceByUUID(@RequestBody UserProfile userProfile,
                                                         @PathVariable("uuid") String uuid){
        this.userService.updateUserProfileDetails(userProfile);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/get-user-profile/{username}")
    public ResponseEntity<UserProfile> getResource(@PathVariable("username") String userName) {
        UserProfile userProfile = null;
        try {
            userProfile = this.userService.getUserProfileDetails(userName);
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setHttpStatusCode(500);
            errorModel.setApplicationErrorCode(12341);
            errorModel.setUserInterfaceMessage("User Profile details not set for the user");
            throw new LoginAppException(errorModel, e);
        }
    }

    @GetMapping("/get-role")
    public ResponseEntity<List<UserAccount>> getDetails()
    {
        List<UserAccount> userAccounts  = this.userService.getUserRole();
        return ResponseEntity.ok(userAccounts);
    }
}
