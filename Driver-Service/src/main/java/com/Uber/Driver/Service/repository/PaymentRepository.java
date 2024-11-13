package com.Uber.Driver.Service.repository;



import com.Uber.Driver.Service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByDriverId(Long driverId);
    Payment findByTransactionDate(java.util.Date transactionDate);
}
