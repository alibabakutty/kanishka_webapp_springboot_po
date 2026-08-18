package com.kanishkaa.purchase_order.inventory_master.mapper;

import com.kanishkaa.purchase_order.inventory_master.dto.api_side.InventoryMasterRequest;
import com.kanishkaa.purchase_order.inventory_master.dto.response_side.InventoryMasterResponse;
import com.kanishkaa.purchase_order.inventory_master.dto.tally_json.InventoryMasterInnerDto;
import com.kanishkaa.purchase_order.inventory_master.model.InventoryMasterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMasterMapper {

    // request -> entity
    InventoryMasterEntity toEntity(InventoryMasterRequest request);
    // entity ->  response
    InventoryMasterResponse toDto(InventoryMasterEntity entity);
    // update for rest api side
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(InventoryMasterRequest request, @MappingTarget InventoryMasterEntity entity);

    // for tally: inner dto ->  entity
    InventoryMasterEntity toEntity(InventoryMasterInnerDto dto);
    // update for tally
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(InventoryMasterInnerDto dto, @MappingTarget InventoryMasterEntity entity);
}
