package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmRequest(
    @NotBlank
    String token,

    @NotBlank
    @Size(min = 8, max = 64)
    String newPassword
) {}

