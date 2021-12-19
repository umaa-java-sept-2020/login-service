package io.login.server.controllers.secured;

import io.login.client.models.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserRequest> getUserInfo()
    {
        return null;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserRequest> getMyUserInfo(){
        return null;
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is ADMIN
     * @return
     */
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequest userRequest)
    {
        return null;
    }

    /**
     * only an ACTIVE admin can register another admin.
     * The new user state must be DRAFT and ROLE is USER
     * @return
     */
    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest)
    {
        return null;
    }
}
