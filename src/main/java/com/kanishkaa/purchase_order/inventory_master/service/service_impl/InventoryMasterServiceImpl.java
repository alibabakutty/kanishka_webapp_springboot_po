package com.kanishkaa.purchase_order.inventory_master.service.service_impl;

import jakarta.persistence.EntityNotFoundException;
import com.kanishkaa.purchase_order.inventory_master.dto.api_side.InventoryMasterRequest;
import com.kanishkaa.purchase_order.inventory_master.dto.response_side.InventoryMasterResponse;
import com.kanishkaa.purchase_order.inventory_master.dto.tally_json.InventoryMasterInnerDto;
import com.kanishkaa.purchase_order.inventory_master.dto.tally_json.InventoryMasterWrapper;
import com.kanishkaa.purchase_order.inventory_master.mapper.InventoryMasterMapper;
import com.kanishkaa.purchase_order.inventory_master.model.InventoryMasterEntity;
import com.kanishkaa.purchase_order.inventory_master.repository.InventoryMasterRepository;
import com.kanishkaa.purchase_order.inventory_master.service.InventoryMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryMasterServiceImpl implements InventoryMasterService {
    private final InventoryMasterRepository repository;
    private final InventoryMasterMapper mapper;

    @Transactional
    @Override
    public InventoryMasterResponse createInventoryMaster(InventoryMasterRequest request) {
        // check if an master with this item name already exists
        return  repository.findByItemName(request.itemName())
                .map(existingEntity -> {
                    // if exists
                    mapper.updateEntityFromRequest(request, existingEntity);
                    return mapper.toDto(repository.save(existingEntity));
                }).orElseGet(() -> {
                    // if does not exists create new
                    InventoryMasterEntity newEntity = mapper.toEntity(request);
                    return mapper.toDto(repository.save(newEntity));
                });
    }

    @Transactional
    @Override
    public void saveAllFromTally(InventoryMasterWrapper wrapper){
        List<InventoryMasterInnerDto> tallyData = wrapper.getInventoryMasters();

        if (tallyData.isEmpty()) return;

        for (InventoryMasterInnerDto innerDto : tallyData){
            upsertFromDto(innerDto);
        }
    }

    @Override
    public InventoryMasterResponse getByIdInventoryMaster(Long id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Inventory Master not found with this id: " + id));
    }

    @Override
    public List<InventoryMasterResponse> getAllInventoryMasters(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryMasterResponse updateInventoryMaster(Long id, InventoryMasterRequest request){
        // find by existing record
        InventoryMasterEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot update, ID not found: " + id));
        // use mapstruct to update fields
        mapper.updateEntityFromRequest(request, existingEntity);
        return mapper.toDto(repository.save(existingEntity));
    }

    @Transactional
    @Override
    public void deleteByIdInventoryMaster(Long id){
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. ID not found: " + id);
        }
        repository.deleteById(id);
    }

    // helper function
    private void upsertFromDto(InventoryMasterInnerDto dto){
        repository.findByItemName(dto.itemName())
                .ifPresentOrElse(
                        existing -> {
                            mapper.updateEntityFromDto(dto, existing);
                            repository.save(existing);
                        },
                        () -> {
                            InventoryMasterEntity entity = mapper.toEntity(dto);
                            repository.save(entity);
                        }
                );
    }
}
