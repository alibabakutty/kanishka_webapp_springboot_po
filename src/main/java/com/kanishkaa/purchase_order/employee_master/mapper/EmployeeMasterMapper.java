package com.kanishkaa.purchase_order.employee_master.mapper;

import com.kanishkaa.purchase_order.employee_master.dto.api_side.EmployeeMasterRequest;
import com.kanishkaa.purchase_order.employee_master.dto.response_side.EmployeeMasterResponse;
import com.kanishkaa.purchase_order.employee_master.dto.tally_json.EmployeeMasterInnerDto;
import com.kanishkaa.purchase_order.employee_master.model.EmployeeMasterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMasterMapper {

    // request ->  entity
    EmployeeMasterEntity toEntity(EmployeeMasterRequest request);
    // entity ->  response
    EmployeeMasterResponse toDto(EmployeeMasterEntity entity);
    // update for rest api side
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(EmployeeMasterRequest request, @MappingTarget EmployeeMasterEntity entity);

    // for tally : inner dto -> entity
    EmployeeMasterEntity toEntity(EmployeeMasterInnerDto dto);
    // update for tally
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(EmployeeMasterInnerDto dto, @MappingTarget EmployeeMasterEntity entity);
}
