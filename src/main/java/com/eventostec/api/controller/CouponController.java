package com.eventostec.api.controller;
import java.net.ResponseCache;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.service.CouponService;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;


    @PostMapping("/event/{eventID}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventID, @RequestBody CouponRequestDTO data) {
        Coupon coupon = couponService.addCouponToEvent(eventID, data);
        return ResponseEntity.ok(coupon);
    }
}