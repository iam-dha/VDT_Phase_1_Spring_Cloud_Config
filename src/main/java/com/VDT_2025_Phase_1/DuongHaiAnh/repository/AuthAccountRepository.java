package com.VDT_2025_Phase_1.DuongHaiAnh.repository;

import com.VDT_2025_Phase_1.DuongHaiAnh.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, UUID> {
    /**
     *
     * Select * auth_account where username ='' and password =''
     */
    List<AuthAccount> findByAccountAndPassword(String account, String password);
    AuthAccount findByAccount(String account);
    Boolean existsByAccount(String account);
    Boolean existsByEmail(String email);
    Boolean existsByAccountOrEmail(String account, String email);
}
