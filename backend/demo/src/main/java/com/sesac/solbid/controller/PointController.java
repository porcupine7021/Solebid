package com.sesac.solbid.controller;

import com.sesac.solbid.domain.User;
import com.sesac.solbid.dto.payment.response.PointSummaryResponse;
import com.sesac.solbid.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class PointController {

    private final PointService pointService;

    /*
    * 관리자용 유저 아이디 기반 포인트 단건 조회*/
    /*
    @GetMapping("/{userId}/points")
    public ResponseEntity<PointSummaryResponse> getCurrentPoint(@PathVariable Long userId) {
        PointSummaryResponse body = pointService.getCurrentPoint(userId);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(body);
    }*/

    /*사용자용 유저 아이디 기반 포인트 단건 조회*/
    @GetMapping("/me/points")
    public ResponseEntity<PointSummaryResponse> getMyPoint(
            @AuthenticationPrincipal User authUser) {

        PointSummaryResponse body = pointService.getCurrentPoint(authUser.getUserId());
        return ResponseEntity.ok()
                .header("Cache-Control", "no-store")
                .body(body);
    }
}