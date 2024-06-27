package com.js.moneytransfer.repository;

import com.js.moneytransfer.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.ownerId = :ownerId")
    Optional<Account> findByOwnerIdForUpdate(@Param("ownerId") Long ownerId);

    Optional<Account> findByOwnerId(Long ownerId);
}
