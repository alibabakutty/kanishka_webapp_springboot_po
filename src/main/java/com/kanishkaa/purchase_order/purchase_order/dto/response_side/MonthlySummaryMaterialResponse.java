package com.kanishkaa.purchase_order.purchase_order.dto.response_side;

import java.math.BigDecimal;

public record MonthlySummaryMaterialResponse(
        long count,
        BigDecimal totalAmount
) {
    // Optional: Compact constructor to prevent null values leaking to React
    public MonthlySummaryMaterialResponse {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }
}
