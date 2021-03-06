package io.login.server.controllers.secured;

import io.login.client.models.RoleUpdate;
import io.login.client.models.UserAuthContext;
import io.login.client.models.UserAccount;
import io.login.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {
    @Autowired
    private IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserAccount> getUserInfo()
    {
        return null;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserAccount> getMyUserInfo(){
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAuthContext userAuthContext = (UserAuthContext) object;
        UserAccount userRequest = userAuthContext.getUserRequest();
        return ResponseEntity.ok(userRequest);
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is ADMIN
     * @return
     */
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserAccount userRequest)
    {
        return null;
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is USER
     * @return
     */
    @PostMapping("/register-user")
    public ResponseEntity<UserAccount> registerUserToDB(@RequestBody UserAccount userRequest)
    {
        this.userService.addUserIntoDB(userRequest);
        this.userService.saveUserRoleMapping(userRequest);
        return ResponseEntity.ok(userRequest);
    }

    @PostMapping("/update-role")
    public ResponseEntity<RoleUpdate> updateUserRole(@RequestBody RoleUpdate updateUserRole){
        this.userService.updateUserRole(updateUserRole);
        return ResponseEntity.ok(updateUserRole);
    }
}
