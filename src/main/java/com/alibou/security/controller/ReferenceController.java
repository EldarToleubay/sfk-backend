package com.alibou.security.controller;

import com.alibou.security.entity.Inn;
import com.alibou.security.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reference")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/inn")
    public List<Inn> inn() {
        return referenceService.getAll();
    }
}
