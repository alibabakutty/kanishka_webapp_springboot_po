package com.kanishkaa.purchase_order.employee_master.repository;

import com.kanishkaa.purchase_order.employee_master.model.EmployeeMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeMasterRepository extends JpaRepository<EmployeeMasterEntity, Long> {
    boolean existsByEmployeeName(String employeeName);
    Optional<EmployeeMasterEntity> findByEmployeeName(String employeeName);
    Optional<EmployeeMasterEntity> findByMobileNumber(String mobileNumber);
}
