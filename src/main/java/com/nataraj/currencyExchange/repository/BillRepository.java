package com.nataraj.currencyExchange.repository;


import com.nataraj.currencyExchange.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update Bill bill set bill.status =:status where bill.id =:billId")
    void updateStatus(@Param("billId") Long billId, @Param("status") Bill.StatusType billStatus);
}