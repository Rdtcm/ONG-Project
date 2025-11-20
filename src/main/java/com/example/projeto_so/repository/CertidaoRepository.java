package com.example.projeto_so.repository;

import com.example.projeto_so.entity.Certidao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertidaoRepository extends JpaRepository<Certidao, Long> {
}

