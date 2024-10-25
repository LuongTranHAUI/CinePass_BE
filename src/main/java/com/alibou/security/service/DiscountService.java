package com.alibou.security.service;

import com.alibou.security.entity.Discount;
import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.model.request.DiscountRequest;
import com.alibou.security.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private static final Logger logger = Logger.getLogger(DiscountService.class.getName());
    private final DiscountRepository repository;
    private final UserService userService;
    private final DiscountApplicationService discountApplicationService;

    public List<Discount> findAll() {
        return repository.findAll();
    }

    public void add(DiscountRequest request) {
        var discount = Discount.builder()
                .code(request.getCode())
                .discountPercent(request.getDiscountPercent())
                .description(request.getDescription())
                .discountPercent(request.getDiscountPercent())
                .expirationDate(request.getExpirationDate())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .createdBy(userService.getCurrentUserId())
                .build();
        repository.save(discount);

        var discountApplication = DiscountApplication.builder()
                .discount(discount)
                .user(userService.getCurrentUser())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .createdBy(userService.getCurrentUserId())
                .build();
        discountApplicationService.createDiscountApplication(discountApplication);
    }

    public void update(DiscountRequest request, Long id) {
        var existingDiscount = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        var discount = Discount.builder()
                .code(request.getCode())
                .discountPercent(request.getDiscountPercent())
                .description(request.getDescription())
                .discountPercent(request.getDiscountPercent())
                .expirationDate(request.getExpirationDate())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .updatedBy(userService.getCurrentUserId())
                .build();
        repository.save(discount);
    }

    public void delete(Long id) {
        var existingDiscount = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        repository.deleteById(existingDiscount.getId());
    }
}
