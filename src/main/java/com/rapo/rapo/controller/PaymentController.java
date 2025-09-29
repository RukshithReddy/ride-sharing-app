package com.rapo.rapo.controller;

import com.rapo.rapo.dto.PaymentRequestDTO;
import com.rapo.rapo.dto.PaymentResponseDTO;
import com.rapo.rapo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.rapo.rapo.model.User;
import com.rapo.rapo.repository.UserRepository;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO paymentRequest, @AuthenticationPrincipal UserDetails userDetails) {
        // Get the authenticated user's ID
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        PaymentResponseDTO response = paymentService.processPayment(user.getId(), paymentRequest);
        return ResponseEntity.ok(response);
    }
}