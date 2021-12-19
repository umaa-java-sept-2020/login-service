package io.login.server.controllers.open;

import io.login.client.models.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAuthenticationApiController {

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest)
    {
        return null;
    }

    @PostMapping("/validate-jwt/{jwt}")
    public ResponseEntity<?> validateJwt(@PathVariable String jwt)
    {
        return null;
    }

    @PostMapping("/reset-system-password")
    public ResponseEntity<?> resetPassword(@RequestBody LoginRequest loginRequest)
    {
        return null;
    }
}
