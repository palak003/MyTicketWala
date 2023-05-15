package com.example.movieservice.repository;

import com.example.movieservice.entities.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHolderRepository extends JpaRepository<AccountHolder,Long> {
}
