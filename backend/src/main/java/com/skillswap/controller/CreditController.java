package com.skillswap.controller;

import com.skillswap.dto.CreditTransactionDto;
import com.skillswap.entity.CreditTransaction;
import com.skillswap.security.CurrentUser;
import com.skillswap.service.CreditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditService creditService;
    private final CurrentUser currentUser;

    public CreditController(CreditService creditService, CurrentUser currentUser) {
        this.creditService = creditService;
        this.currentUser = currentUser;
    }

    @GetMapping("/history")
    public ResponseEntity<List<CreditTransactionDto>> history() {
        List<CreditTransactionDto> dtos = creditService.getHistory(currentUser.get().getId())
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private CreditTransactionDto toDto(CreditTransaction t) {
        CreditTransactionDto dto = new CreditTransactionDto();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setType(t.getType().name());
        dto.setDescription(t.getDescription());
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}
