package com.alibou.security.useraccess;

import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class UserAccessController {

    private final UserRepository userRepository;
    private final UserAccessRepository userAccessRepository;

    @Transactional
    @PostMapping("/assign")
    public ResponseEntity<?> assignAccess(@RequestBody AccessAssignmentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Полностью удаляем все текущие доступы пользователя
        userAccessRepository.deleteByUserId(user.getId());

        List<UserAccess> allAccesses = new ArrayList<>();

        for (AccessAssignmentRequest.AccessEntry accessEntry : request.accesses()) {
            String refType = accessEntry.refType();
            List<Long> refIds = accessEntry.refIds();

            if (refIds == null || refIds.isEmpty()) continue;

            for (Long refId : refIds) {
                UserAccess ua = new UserAccess();
                ua.setUser(user);
                ua.setRefType(refType);
                ua.setRefId(refId);
                allAccesses.add(ua);
            }
        }

        if (!allAccesses.isEmpty()) {
            userAccessRepository.saveAll(allAccesses);
        }

        return ResponseEntity.ok("Accesses replaced successfully: " + allAccesses.size());
    }


}
