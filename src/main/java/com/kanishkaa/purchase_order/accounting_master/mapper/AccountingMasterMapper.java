package com.kanishkaa.purchase_order.accounting_master.mapper;

import com.kanishkaa.purchase_order.accounting_master.dto.api_side.AccountingMasterRequest;
import com.kanishkaa.purchase_order.accounting_master.dto.response_side.AccountingMasterResponse;
import com.kanishkaa.purchase_order.accounting_master.dto.tally_json.AccountingMasterInnerDto;
import com.kanishkaa.purchase_order.accounting_master.model.AccountingMasterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountingMasterMapper {
    // request -> entity
    AccountingMasterEntity toEntity(AccountingMasterRequest request);
    // entity -> response
    AccountingMasterResponse toDto(AccountingMasterEntity entity);
    // update
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(AccountingMasterRequest request, @MappingTarget AccountingMasterEntity entity);

    // for tally: inner dto -> entity
    AccountingMasterEntity toEntity(AccountingMasterInnerDto dto);
    // update for tally
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AccountingMasterInnerDto dto, @MappingTarget AccountingMasterEntity entity);
}
