package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.PaymentMethod;
import com.alibou.security.model.request.PaymentMethodRequest;
import com.alibou.security.model.response.PaymentMethodResponse;
import com.alibou.security.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private final PaymentMethodRepository repository;
    private final UserService userService;
    private final GeneralMapper generalMapper;

    public PaymentMethodResponse add(PaymentMethodRequest request) {
        var existingPaymentMethod = repository.findByName(request.getName());
        if (existingPaymentMethod.isPresent()) {
            throw new IllegalArgumentException("Theater's name was exist");
        }
        var paymentMethod = generalMapper.mapToEntity(request, PaymentMethod.class);
        paymentMethod.setCreatedBy(userService.getCurrentUserId());
        paymentMethod.setCreatedAt(LocalDateTime.now());
        repository.save(paymentMethod);
        logger.info("Payment method added successfully: {}");
        return generalMapper.mapToDTO(paymentMethod, PaymentMethodResponse.class);
    }

    public PaymentMethodResponse change(PaymentMethodRequest request, Long id) {
        var existingPaymentMethod = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found"));
        generalMapper.mapToEntity(request,existingPaymentMethod);
        existingPaymentMethod.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        existingPaymentMethod.setUpdatedBy(userService.getCurrentUserId());
        logger.info("Payment method updated successfully: {}");
        repository.save(existingPaymentMethod);
        return generalMapper.mapToDTO(existingPaymentMethod, PaymentMethodResponse.class);
    }

    public void delete(Long id) {
        var existingPaymentMethod = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        repository.deleteById(existingPaymentMethod.getId());
        logger.info("Theater deleted successfully: {}");
    }

    public List<PaymentMethodResponse> findAll() {
        List<PaymentMethod> paymentMethods = repository.findAll();
        logger.info("Theaters retrieved successfully");
        return paymentMethods.stream()
                .map(paymentMethod -> generalMapper.mapToDTO(paymentMethod, PaymentMethodResponse.class))
                .collect(Collectors.toList());
    }
}
