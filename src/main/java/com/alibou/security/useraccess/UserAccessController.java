package com.alibou.security.useraccess;

import com.alibou.security.user.User;
import com.alibou.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access")
public class UserAccessController {

    private final UserRepository userRepository;
    private final UserAccessRepository userAccessRepository;

    @PostMapping("/assign")
    public ResponseEntity<?> assignAccess(@RequestBody AccessAssignmentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserAccess> allAccesses = new ArrayList<>();

        for (AccessAssignmentRequest.AccessEntry accessEntry : request.accesses()) {
            String refType = accessEntry.refType();
            List<Long> refIds = accessEntry.refIds();

            if (refIds == null || refIds.isEmpty()) continue;

            // Можно убрать дубли перед сохранением (по желанию)
            Set<Long> existingRefIds = userAccessRepository
                    .findByUserIdAndRefType(user.getId(), refType)
                    .stream()
                    .map(UserAccess::getRefId)
                    .collect(Collectors.toSet());

            List<UserAccess> newAccesses = refIds.stream()
                    .filter(id -> !existingRefIds.contains(id))
                    .map(id -> {
                        UserAccess ua = new UserAccess();
                        ua.setUser(user);
                        ua.setRefType(refType);
                        ua.setRefId(id);
                        return ua;
                    }).toList();

            allAccesses.addAll(newAccesses);
        }

        if (!allAccesses.isEmpty()) {
            userAccessRepository.saveAll(allAccesses);
        }

        return ResponseEntity.ok("Accesses assigned successfully: " + allAccesses.size());
    }

}
