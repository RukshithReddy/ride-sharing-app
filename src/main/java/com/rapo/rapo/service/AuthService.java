package com.rapo.rapo.service;

import com.rapo.rapo.dto.RegistrationRequest;
import com.rapo.rapo.model.Role;
import com.rapo.rapo.model.User;
import com.rapo.rapo.model.Vehicle;
import com.rapo.rapo.repository.UserRepository;
import com.rapo.rapo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == Role.ROLE_DRIVER) {
            if (request.getModel() == null || request.getLicensePlate() == null || request.getCapacity() == null) {
                throw new IllegalArgumentException("Vehicle details are required for drivers.");
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setUser(savedUser);
            vehicle.setModel(request.getModel());
            vehicle.setLicensePlate(request.getLicensePlate());
            vehicle.setCapacity(request.getCapacity());
            vehicleRepository.save(vehicle);
            savedUser.setVehicle(vehicle);
            userRepository.save(savedUser);
        }

        return savedUser;
    }
}