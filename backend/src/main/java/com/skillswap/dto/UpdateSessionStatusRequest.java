package com.skillswap.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateSessionStatusRequest {
    @NotBlank
    private String status; // CONFIRMED, REJECTED, COMPLETED, CANCELLED

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
