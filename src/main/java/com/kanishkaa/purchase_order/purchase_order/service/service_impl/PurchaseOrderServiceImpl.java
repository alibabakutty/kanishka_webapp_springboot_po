package com.kanishkaa.purchase_order.purchase_order.service.service_impl;

import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryLabourResponse;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.MonthlySummaryMaterialResponse;
import jakarta.transaction.Transactional;
import com.kanishkaa.purchase_order.purchase_order.dto.api_side.PurchaseOrderRequest;
import com.kanishkaa.purchase_order.purchase_order.dto.response_side.PurchaseOrderResponse;
import com.kanishkaa.purchase_order.purchase_order.mapper.PurchaseOrderMapper;
import com.kanishkaa.purchase_order.purchase_order.model.PurchaseOrderEntity;
import com.kanishkaa.purchase_order.purchase_order.model.PurchaseOrderSubFormEntity;
import com.kanishkaa.purchase_order.purchase_order.repository.PurchaseOrderRepository;
import com.kanishkaa.purchase_order.purchase_order.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository repository;
    private final PurchaseOrderMapper mapper;

    @Transactional
    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {

        System.out.println("🔥 CREATE METHOD ENTERED");
        System.out.println("Voucher No: " + request.voucherNumber());
        try {

            String superKey = request.companyName()
                    + " - "
                    + request.voucherType()
                    + " - "
                    + request.voucherNumber();

            return repository.findBySuperKey(superKey)
                    .map(existingEntity -> {

                        // Update parent fields
                        mapper.updateEntityFromRequest(request, existingEntity);

                        // Remove old inventory entries
                        existingEntity.getInventoryEntries().forEach(
                                item -> item.setPurchaseOrder(null)
                        );
                        existingEntity.getInventoryEntries().clear();

                        repository.flush();

                        // Create new inventory entries from request
                        PurchaseOrderEntity tempEntity = mapper.toEntity(request);

                        if (tempEntity.getInventoryEntries() != null) {
                            tempEntity.getInventoryEntries().forEach(item -> {
                                item.setPurchaseOrder(existingEntity);
                                item.setSuperKey(superKey);
                            });

                            existingEntity.getInventoryEntries()
                                    .addAll(tempEntity.getInventoryEntries());
                        }

                        existingEntity.setTotalAmount(
                                calculateTotal(existingEntity.getInventoryEntries())
                        );

                        existingEntity.setSuperKey(superKey);

                        PurchaseOrderEntity savedEntity =
                                repository.saveAndFlush(existingEntity);

                        return mapper.toDto(savedEntity);
                    })
                    .orElseGet(() -> {

                        PurchaseOrderEntity newEntity =
                                mapper.toEntity(request);

                        newEntity.setSuperKey(superKey);

                        if (newEntity.getInventoryEntries() != null) {
                            newEntity.getInventoryEntries().forEach(item -> {
                                item.setPurchaseOrder(newEntity);
                                item.setSuperKey(superKey);
                            });
                        }

                        newEntity.setTotalAmount(
                                calculateTotal(newEntity.getInventoryEntries())
                        );

                        PurchaseOrderEntity savedEntity =
                                repository.saveAndFlush(newEntity);

                        return mapper.toDto(savedEntity);
                    });

        } catch (Exception e) {

            System.err.println(
                    "❌ CRITICAL ERROR SAVING VOUCHER: "
                            + request.voucherNumber()
            );

            System.err.println("Reason: " + e.getMessage());

            e.printStackTrace();

            throw e;
        }
    }

    @Override
    public PurchaseOrderResponse getById(Long id) {
        PurchaseOrderEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        return mapper.toDto(entity);
    }



    @Override
    public List<PurchaseOrderResponse> getpo() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    public List<PurchaseOrderResponse> generalpo() {
        return repository.generalpurchaseorder()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    public List<PurchaseOrderResponse> materialpo() {
        return repository.materialpo()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<PurchaseOrderResponse> labourpo() {
        return repository.labourpo()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<PurchaseOrderResponse> getMaterialPOsByMonth(int year, int monthNumber) {
        // calculate start and end date of the month
        java.time.LocalDate startDate = java.time.LocalDate.of(year, monthNumber, 1);
        java.time.LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository.findMaterialPOsByDateRange(startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public MonthlySummaryMaterialResponse getMaterialSummaryByMonth(int year, int monthNumber) {
        java.time.LocalDate startDate = java.time.LocalDate.of(year, monthNumber, 1);
        java.time.LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository.findMaterialSummaryByDateRange(startDate, endDate);
    }


    @Override
    public List<PurchaseOrderResponse> getLabourPOsByMonth(int year, int monthNumber){
        // calculate start and end date of the month
        java.time.LocalDate startDate = java.time.LocalDate.of(year, monthNumber, 1);
        java.time.LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository.findLabourPOsByDateRange(startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    public MonthlySummaryLabourResponse getLabourSummaryByMonth(int year, int monthNumber) {
        java.time.LocalDate startDate = java.time.LocalDate.of(year, monthNumber, 1);
        java.time.LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return repository.findLabourSummaryByDateRange(startDate, endDate);
    }

    @Override
    public PurchaseOrderResponse update(Long id, PurchaseOrderRequest request) {

         PurchaseOrderEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));

        // update basic fields
        mapper.updateEntityFromRequest(request, existing);

        // handle line items manually
        existing.getInventoryEntries().clear();
        String Validkey=request.companyName()+" - "+request.voucherType()+" - " +request.voucherNumber();
        List<PurchaseOrderSubFormEntity> newItems = mapper.toEntity(request).getInventoryEntries();

        if (newItems != null) {
            newItems.forEach(item -> {
                item.setPurchaseOrder(existing);
                item.setSuperKey(Validkey);
            });
            existing.getInventoryEntries().addAll(newItems);
        }

        // recalculate total
        existing.setTotalAmount(calculateTotal(existing.getInventoryEntries()));
        existing.setSuperKey(Validkey);

        PurchaseOrderEntity updated = repository.save(existing);

        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Purchase order not found");
        }
        repository.deleteById(id);
    }


    private BigDecimal calculateTotal(List<PurchaseOrderSubFormEntity> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = items.stream()
                .map(PurchaseOrderSubFormEntity::getItemAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // return absolute which is positive value
        return total.abs();
    }
}
