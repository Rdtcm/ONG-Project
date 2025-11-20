package com.example.projeto_so.repository;

import com.example.projeto_so.entity.EmailValidacao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidacaoRepository extends JpaRepository<EmailValidacao, Long> {
    Optional<EmailValidacao> findByToken(String token);
}

