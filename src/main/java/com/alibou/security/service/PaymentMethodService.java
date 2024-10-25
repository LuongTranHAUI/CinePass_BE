package com.alibou.security.service;

import com.alibou.security.entity.PaymentMethod;
import com.alibou.security.model.request.PaymentMethodRequest;
import com.alibou.security.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    private final PaymentMethodRepository repository;
    private final UserService userService;

   public void add(PaymentMethodRequest request) {
        var paymentMethod = PaymentMethod.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .createdBy(userService.getCurrentUserId())
                .build();
        repository.save(paymentMethod);
    }

    public void change(PaymentMethodRequest request, Long id) {
        var existingPaymentMethod = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found"));
        var paymentMethod = PaymentMethod.builder()
                .name(request.getName())
                .description(request.getDescription())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .updatedBy(userService.getCurrentUserId())
                .build();
        repository.save(paymentMethod);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PaymentMethod> findAll() {
        return repository.findAll();
    }
}
