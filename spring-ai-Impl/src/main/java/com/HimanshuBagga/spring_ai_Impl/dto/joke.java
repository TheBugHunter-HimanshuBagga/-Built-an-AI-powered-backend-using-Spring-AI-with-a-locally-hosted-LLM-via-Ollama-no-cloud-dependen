package com.HimanshuBagga.spring_ai_Impl.dto;

public record joke(
        String text,
        String category,
        Double laughScore,
        Boolean isNSFW
) {
}
