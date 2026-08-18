package com.kanishkaa.purchase_order.login.repository;


import com.kanishkaa.purchase_order.login.model.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginModel, Long> {
    Optional<LoginModel> findByUsername(String username);
    Optional<LoginModel> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
