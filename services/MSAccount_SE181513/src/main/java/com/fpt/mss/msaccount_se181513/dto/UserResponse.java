package com.fpt.mss.msaccount_se181513.dto;

public record UserResponse(
        Integer id, String username, String email, boolean activated, String role) {}
