package com.alibou.security.useraccess;

import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access")
public class UserAccessController {

    private final UserRepository userRepository;
    private final UserAccessRepository userAccessRepository;

    @PostMapping("/assign")
    public ResponseEntity<?> assignAccess(@RequestBody AccessAssignmentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow();

        List<UserAccess> accesses = request.refIds().stream()
                .map(id -> {
                    UserAccess access = new UserAccess();
                    access.setUser(user);
                    access.setRefType(request.refType());
                    access.setRefId(id);
                    return access;
                }).toList();

        userAccessRepository.saveAll(accesses);

        return ResponseEntity.ok("Access granted");
    }
}
