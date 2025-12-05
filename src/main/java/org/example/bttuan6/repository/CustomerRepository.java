package org.example.bttuan6.repository;

import org.example.bttuan6.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Thêm dòng này để check khách hàng cũ
    Optional<Customer> findByEmail(String email);
}