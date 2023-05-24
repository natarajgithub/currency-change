package com.nataraj.currencyExchange.repository;


import com.nataraj.currencyExchange.entity.BillChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillChangeRepository extends JpaRepository<BillChange, Long> {
}