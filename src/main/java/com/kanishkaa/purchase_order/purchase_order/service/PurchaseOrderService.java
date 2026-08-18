package com.kanishkaa.purchase_order.purchase_order.service;

import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryLabourResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryMaterialResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse create(PurchaseOrderRequest request);

    PurchaseOrderResponse getById(Long id);

    List<PurchaseOrderResponse> getpo();

    List<PurchaseOrderResponse> generalpo();

    List<PurchaseOrderResponse> materialpo();

    List<PurchaseOrderResponse> labourpo();

    List<PurchaseOrderResponse> getMaterialPOsByMonth(int year, int monthNumber);

    MonthlySummaryMaterialResponse getMaterialSummaryByMonth(int year, int monthNumber);

    List<PurchaseOrderResponse> getLabourPOsByMonth(int year, int monthNumber);

    MonthlySummaryLabourResponse getLabourSummaryByMonth(int year, int monthNumber);

    PurchaseOrderResponse update(Long id, PurchaseOrderRequest request);

    void delete(Long id);
}
