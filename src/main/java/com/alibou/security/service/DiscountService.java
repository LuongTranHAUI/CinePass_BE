package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Discount;
import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.model.request.DiscountRequest;
import com.alibou.security.model.response.DiscountResponse;
import com.alibou.security.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private static final Logger logger = Logger.getLogger(DiscountService.class.getName());
    private final DiscountRepository repository;
    private final UserService userService;
    private final DiscountApplicationService discountApplicationService;
    private final GeneralMapper generalMapper;

    public List<DiscountResponse> findAll() {
        List<Discount> discounts = repository.findAll();
        logger.info("Discount retrieved successfully");
        return discounts.stream()
                .map(discount -> generalMapper.mapToDTO(discount,DiscountResponse.class))
                .collect(Collectors.toList());
    }

    public DiscountResponse add(DiscountRequest request) {
        var existingDiscount = repository.findByCode(request.getCode());
        if (existingDiscount.isPresent()) {
            throw new IllegalArgumentException("Discount's name was exist");
        }
        var discount = generalMapper.mapToEntity(request, Discount.class);
        discount.setCreatedBy(userService.getCurrentUserId());
        discount.setCreatedAt(LocalDateTime.now());
        repository.save(discount);
        logger.info("Discount added successfully: {}");
        var discountApplication = DiscountApplication.builder()
                .discount(discount)
                .user(userService.getCurrentUser())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .createdBy(userService.getCurrentUserId())
                .build();
        discountApplicationService.createDiscountApplication(discountApplication);
        return generalMapper.mapToDTO(discount,DiscountResponse.class);
    }

    public DiscountResponse update(DiscountRequest request, Long id) {
        var existingDiscount = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        generalMapper.mapToEntity(request, Discount.class);
        existingDiscount.setUpdatedBy(userService.getCurrentUserId());
        existingDiscount.setUpdatedAt(LocalDateTime.now());
        repository.save(existingDiscount);
        logger.info("Discount updated successfully: {}");
        return generalMapper.mapToDTO(existingDiscount, DiscountResponse.class);
    }

    public void delete(Long id) {
        var existingDiscount = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        repository.deleteById(existingDiscount.getId());
        logger.info("Theater deleted successfully: {}");
    }
}
