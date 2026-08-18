package com.kanishkaa.purchase_order.purchase_order.dto.response_side;

import java.math.BigDecimal;

public record MonthlySummaryLabourResponse(
        long count,
        BigDecimal totalAmount
) {
    public MonthlySummaryLabourResponse {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }
}
