package com.kanishkaa.purchase_order.accounting_master.service.service_impl;

import jakarta.persistence.EntityNotFoundException;
import com.kanishkaa.purchase_order.accounting_master.dto.api_side.AccountingMasterRequest;
import com.kanishkaa.purchase_order.accounting_master.dto.response_side.AccountingMasterResponse;
import com.kanishkaa.purchase_order.accounting_master.dto.tally_json.AccountingMasterInnerDto;
import com.kanishkaa.purchase_order.accounting_master.dto.tally_json.AccountingMasterWrapper;
import com.kanishkaa.purchase_order.accounting_master.mapper.AccountingMasterMapper;
import com.kanishkaa.purchase_order.accounting_master.model.AccountingMasterEntity;
import com.kanishkaa.purchase_order.accounting_master.repository.AccountingMasterRepository;
import com.kanishkaa.purchase_order.accounting_master.service.AccountingMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountingMasterServiceImpl implements AccountingMasterService {
    private final AccountingMasterRepository repository;
    private final AccountingMasterMapper mapper;


    @Transactional
    @Override
    public AccountingMasterResponse createAccountingMaster(AccountingMasterRequest request){
        // check if an master with this sundry creditor name already exists
        return repository.findBySundryCreditorName(request.sundryCreditorName())
                .map(existingEntity -> {
                    // if exists
                    mapper.updateEntityFromRequest(request, existingEntity);
                    return mapper.toDto(repository.save(existingEntity));
                }).orElseGet(() -> {
                    // if does not exists create new
                    AccountingMasterEntity newEntity = mapper.toEntity(request);
                    return mapper.toDto(repository.save(newEntity));
                });
    }

    @Transactional
    @Override
    public void saveAllFromTally(AccountingMasterWrapper wrapper) {
        List<AccountingMasterInnerDto> tallyData = wrapper.getAccountingMasters();

        if (tallyData.isEmpty()) return;

        for(AccountingMasterInnerDto innerDto : tallyData){
            upsertFromDto(innerDto);
        }
    }

    @Override
    public AccountingMasterResponse getByIdAccountingMaster(Long id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Accounting master not found with this id: " + id));
    }

    @Override
    public List<AccountingMasterResponse> getAllAccountingMaster(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountingMasterResponse updateAccountingMaster(Long id, AccountingMasterRequest request){
        // find by existing record
        AccountingMasterEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot update, ID not found: " + id));
        // use mapstruct to update fields
        mapper.updateEntityFromRequest(request, existingEntity);

        return mapper.toDto(repository.save(existingEntity));
    }

    @Transactional
    @Override
    public void  deleteByIdAccountingMaster(Long id){
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. ID not found: " + id);
        }
        repository.deleteById(id);
    }

    // helper function
    private void upsertFromDto(AccountingMasterInnerDto dto) {

        repository.findBySundryCreditorName(dto.sundryCreditorName())
                .ifPresentOrElse(

                        existing -> {
                            mapper.updateEntityFromDto(dto, existing);
                            repository.save(existing);
                        },

                        () -> {
                            AccountingMasterEntity entity = mapper.toEntity(dto);
                            repository.save(entity);
                        }
                );
    }


    private boolean isStrictSundryDebtor(AccountingMasterInnerDto dto){
        // Null protection combined with case-insensitive, trimmed string matching
        boolean matchesParent = dto.parentName() != null && dto.parentName().trim().equalsIgnoreCase("Sundry Debtors");

        boolean matchesGrandParent = dto.grandParentName() != null && dto.grandParentName().trim().equalsIgnoreCase("Current Assets");

        return  matchesParent && matchesGrandParent;
    }
}
