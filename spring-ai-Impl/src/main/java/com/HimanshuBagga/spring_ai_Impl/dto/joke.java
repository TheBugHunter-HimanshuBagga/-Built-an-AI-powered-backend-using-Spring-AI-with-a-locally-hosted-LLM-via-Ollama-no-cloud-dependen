package com.HimanshuBagga.spring_ai_Impl.dto;

public record joke(
        String text,
        String category,
        Double laughScore,
        Boolean isNSFW
) {
}

/*
{
  "text": "Why did the cat sit on the computer? Because it wanted to keep an eye on the mouse!",
  "category": "sarcastic",
  "laughScore": 8.5,
  "isNSFW": false
}
 */
