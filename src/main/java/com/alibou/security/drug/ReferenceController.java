package com.alibou.security.drug;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/inn")
    public List<Inn> inn() {
        return referenceService.getAll();
    }
}
