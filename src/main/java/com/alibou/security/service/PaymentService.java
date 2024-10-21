package com.alibou.security.service;

import com.alibou.security.config.VnPayConfig;
import com.alibou.security.entity.Payment;
import com.alibou.security.entity.PaymentMethod;
import com.alibou.security.enums.PaymentStatus;
import com.alibou.security.model.request.PaymentRequest;
import com.alibou.security.repository.PaymentMethodRepository;
import com.alibou.security.repository.PaymentRepository;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserService userService;

    public JsonObject processPayment(PaymentRequest paymentRequest) throws UnsupportedEncodingException {
        String orderType = "other";
        long amount = paymentRequest.getAmount().multiply(new BigDecimal(100)).longValue();
        String bankCode = paymentRequest.getBankCode();

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = paymentRequest.getIpAddress();

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", paymentRequest.getCurrency());

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = paymentRequest.getLanguage();
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentRequest.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found"));

        Payment payment = Payment.builder()
                .transactionId(vnp_TxnRef)
                .amount(paymentRequest.getAmount())
                .currency(paymentRequest.getCurrency())
                .status(PaymentStatus.valueOf("PROCESSING"))
                .user(userService.getCurrentUser())
                .paymentMethod(paymentMethod)
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        paymentRepository.save(payment);

        JsonObject response = new JsonObject();
        response.addProperty("paymentUrl", paymentUrl);
        return response;
    }

    public JsonObject handleVnPayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayConfig.hashAllFields(fields);

        JsonObject response = new JsonObject();
        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionId = request.getParameter("vnp_TxnRef");
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        switch (responseCode) {
            case "00":
                response.addProperty("message", "GD thanh cong");
                payment.setStatus(PaymentStatus.COMPLETED);
                break;
            case "24":
            case "15":
                response.addProperty("message", "GD bi huy");
                payment.setStatus(PaymentStatus.CANCELED);
                break;
            default:
                response.addProperty("message", "GD khong thanh cong");
                payment.setStatus(PaymentStatus.FAILED);
                break;
        }
        paymentRepository.save(payment);
        return response;
    }

    public Payment add(PaymentRequest request) {
        var payment = Payment.builder()
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.valueOf("COMPLETED"))
                .paymentMethod(paymentMethodRepository.findById(request.getPaymentMethodId())
                        .orElseThrow(() -> new IllegalArgumentException("Payment Method not found")))
                .user(userService.getCurrentUser())
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        paymentRepository.save(payment);
        return payment;
    }

    public Payment update(PaymentRequest request) {
        var payment = paymentRepository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found")));
        payment.setUpdatedBy(userService.getCurrentUserId());
        payment.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        paymentRepository.save(payment);
        return payment;
    }

    public Payment delete(Long id) {
        var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        paymentRepository.deleteById(payment.getId());
        return payment;
    }
}