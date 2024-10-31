package com.alibou.security.service;

import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.repository.DiscountApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiscountApplicationService {

    @Autowired
    private DiscountApplicationRepository discountApplicationRepository;

    public List<DiscountApplication> getAllDiscountApplications() {
        return discountApplicationRepository.findAll();
    }

    public Optional<DiscountApplication> getDiscountApplicationById(Long id) {
        return discountApplicationRepository.findById(id);
    }

    public DiscountApplication createDiscountApplication(DiscountApplication discountApplication) {
        return discountApplicationRepository.save(discountApplication);
    }

    public DiscountApplication updateDiscountApplication(Long id, DiscountApplication discountApplicationDetails) {
        DiscountApplication discountApplication = discountApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DiscountApplication not found"));
        discountApplication.setDiscount(discountApplicationDetails.getDiscount());
        discountApplication.setUser(discountApplicationDetails.getUser());
        discountApplication.setCreatedAt(discountApplicationDetails.getCreatedAt());
        discountApplication.setUpdatedAt(discountApplicationDetails.getUpdatedAt());
        discountApplication.setCreatedBy(discountApplicationDetails.getCreatedBy());
        discountApplication.setUpdatedBy(discountApplicationDetails.getUpdatedBy());
        return discountApplicationRepository.save(discountApplication);
    }

    public void deleteDiscountApplication(Long id) {
        discountApplicationRepository.deleteById(id);
    }
}
