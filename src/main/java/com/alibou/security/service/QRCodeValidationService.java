package com.alibou.security.service;

import com.alibou.security.enums.TicketStatus;
import com.alibou.security.model.request.TicketValidationRequest;
import com.alibou.security.model.response.TicketResponse;
import com.github.javafaker.Faker;
import com.google.zxing.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QRCodeValidationService {

    private final TicketService ticketService;

    public QRCodeValidationService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public String scanAndValidateTicket(String filePath, Long userId) {
        try {
            String qrCodeText = QRCodeGeneratorService.scanQRCode(filePath);
            // Assuming QR code text contains ticketId and seatNumber separated by a comma
            String[] parts = qrCodeText.split(",");
            Long ticketId = Long.parseLong(parts[0].split(":")[1].trim());
            String seatNumber = parts[1].split(":")[1].trim();

            TicketValidationRequest request = new TicketValidationRequest(ticketId, seatNumber, userId);
            TicketResponse ticket = ticketService.getTicket(request.getTicketId());
            if (ticket != null && ticket.getSeatNumber().equals(request.getSeatNumber())) {
                ticketService.CheckAndUpdateTicketStatus(ticket.getId(), TicketStatus.USED, request.getUserId());
                return "Ticket validated and status updated to USED";
            } else {
                return "Invalid ticket information";
            }
        } catch (IOException | NotFoundException e) {
            return "Error scanning QR Code: " + e.getMessage();
        } catch (Exception e) {
            return "Error validating ticket: " + e.getMessage();
        }
    }
}
