package com.example.projeto_so.repository;

import com.example.projeto_so.entity.TermoCompromisso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermoCompromissoRepository extends JpaRepository<TermoCompromisso, Long> {
    Optional<TermoCompromisso> findFirstByVigenteTrueOrderByIdDesc();

    Optional<TermoCompromisso> findByCandidatoId(Long candidatoId);
}

