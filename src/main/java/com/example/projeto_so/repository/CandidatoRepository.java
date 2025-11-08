package com.example.projeto_so.repository;

import com.example.projeto_so.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Optional<Candidato> findByEmail(String email);
    Optional<Candidato> findByCpfOuCnpj(String cpfOuCnpj);
    boolean existsByEmail(String email);
    boolean existsByCpfOuCnpj(String cpfOuCnpj);
}