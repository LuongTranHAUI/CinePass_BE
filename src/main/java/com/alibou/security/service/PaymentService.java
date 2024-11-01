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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    public JsonObject processVNPayment(PaymentRequest paymentRequest) {
        String orderType = "other";
        long amount = paymentRequest.getAmount().multiply(new BigDecimal(100)).longValue();
        String bankCode = paymentRequest.getBankCode();

        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnPayConfig.getIpAddress(httpServletRequest);

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
            if (fieldValue != null && !fieldValue.isEmpty()) {
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
                .status(PaymentStatus.PROCESSING)
                .user(UserService.getCurrentUser())
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
            String fieldName = null;
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = null;
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = VnPayConfig.hashAllFields(fields);
        JsonObject response = new JsonObject();

        if (signValue.equals(vnp_SecureHash)) {
            String transactionId = request.getParameter("vnp_TxnRef");
            Payment payment = paymentRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

            boolean checkOrderId = true; // Assume transaction ID exists in the database
            boolean checkAmount = true; // Assume the amount is correct
            boolean checkOrderStatus = payment.getStatus() == PaymentStatus.PROCESSING; // Check if the payment status is pending

            if (checkOrderId) {
                if (checkAmount) {
                    if (checkOrderStatus) {
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            payment.setStatus(PaymentStatus.COMPLETED);
                            response.addProperty("message", "GD thanh cong");
                        } else {
                            payment.setStatus(PaymentStatus.FAILED);
                            response.addProperty("message", "GD khong thanh cong");
                        }
                        response.addProperty("RspCode", "00");
                        response.addProperty("Message", "Confirm Success");
                    } else {
                        response.addProperty("RspCode", "02");
                        response.addProperty("Message", "Order already confirmed");
                    }
                } else {
                    response.addProperty("RspCode", "04");
                    response.addProperty("Message", "Invalid Amount");
                }
            } else {
                response.addProperty("RspCode", "01");
                response.addProperty("Message", "Order not Found");
            }
        } else {
            response.addProperty("RspCode", "97");
            response.addProperty("Message", "Invalid Checksum");
        }

        Payment payment = paymentRepository.findByTransactionId(request.getParameter("vnp_TxnRef"))
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        paymentRepository.save(payment);
        return response;
    }

    public Payment add(PaymentRequest request) {
        var payment = Payment.builder()
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.COMPLETED)
                .paymentMethod(paymentMethodRepository.findById(request.getPaymentMethodId())
                        .orElseThrow(() -> new IllegalArgumentException("Payment Method not found")))
                .user(UserService.getCurrentUser())
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

    public List<Payment> findByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}