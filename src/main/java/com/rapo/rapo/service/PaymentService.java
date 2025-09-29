package com.rapo.rapo.service;

import com.rapo.rapo.dto.PaymentRequestDTO;
import com.rapo.rapo.dto.PaymentResponseDTO;
import com.rapo.rapo.model.*;
import com.rapo.rapo.repository.RideRepository;
import com.rapo.rapo.repository.TransactionRepository;
import com.rapo.rapo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PaymentResponseDTO processPayment(Long passengerId, PaymentRequestDTO paymentRequest) {
        Ride ride = rideRepository.findById(paymentRequest.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + paymentRequest.getRideId()));

        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));

        if (ride.getRideStatus() == RideStatus.PAID) {
             return new PaymentResponseDTO(null, "FAILED", "This ride has already been paid for.");
        }

        // --- SIMULATE SUCCESSFUL PAYMENT ---
        // In a real app, you would integrate with a payment gateway here.
        // For now, we assume the payment is always successful.

        Transaction transaction = new Transaction();
        transaction.setRide(ride);
        transaction.setPassenger(passenger);
        transaction.setAmount(ride.getTotalFare());
        transaction.setPaymentMethod(paymentRequest.getPaymentMethod());
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        
        // --- SECURITY: Store only masked data ---
        if (paymentRequest.getPaymentMethod() == PaymentMethod.CREDIT_CARD || paymentRequest.getPaymentMethod() == PaymentMethod.DEBIT_CARD) {
            String cardNumber = paymentRequest.getCardNumber();
            if (cardNumber != null && cardNumber.length() > 4) {
                String maskedCard = "************" + cardNumber.substring(cardNumber.length() - 4);
                transaction.setPaymentDetails(maskedCard);
            }
        } else if (paymentRequest.getPaymentMethod() == PaymentMethod.UPI) {
            transaction.setPaymentDetails(paymentRequest.getUpiId());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Update the ride status to PAID
        ride.setRideStatus(RideStatus.PAID);
        rideRepository.save(ride);

        return new PaymentResponseDTO(savedTransaction.getId(), "SUCCESS", "Payment processed successfully.");
    }
}