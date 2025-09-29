package com.rapo.rapo.dto;

import com.rapo.rapo.model.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long rideId;
    private PaymentMethod paymentMethod;
    // For card
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    // For UPI
    private String upiId;
}