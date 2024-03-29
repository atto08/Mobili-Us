package com.project.MobiliUs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.MobiliUs.security.MemberDetailsImpl;
import com.project.MobiliUs.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/auth/google/callback")
    public ResponseEntity<?> getGoogleCallback(@RequestParam String code) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/loading.html");
        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }


    @GetMapping(value = "/members/google/login")
    public ResponseEntity<?> googleLogin(@RequestParam String code) throws JsonProcessingException {
        return memberService.googleLogin(code);
    }


    @GetMapping(value = "/members/admin")
    public ResponseEntity<?> getAdmin(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @RequestParam String adminToken) {
        return memberService.getAdmin(memberDetails.getMember().getEmail(), memberDetails.getMember().getGoogleId(), adminToken);
    }
}
