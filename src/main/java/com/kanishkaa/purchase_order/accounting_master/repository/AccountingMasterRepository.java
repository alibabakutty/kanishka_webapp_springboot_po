package com.kanishkaa.purchase_order.accounting_master.repository;

import com.kanishkaa.purchase_order.accounting_master.model.AccountingMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountingMasterRepository extends JpaRepository<AccountingMasterEntity, Long> {
    boolean existsBySundryCreditorName(String sundryCreditorName);
    Optional<AccountingMasterEntity> findBySundryCreditorName(String sundryCreditorName);
}
