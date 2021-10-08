package com.example.demo.repositories;

import com.example.demo.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT MAX(a.bid) FROM Application a")
    Optional<Long> findMaxBid();
}
