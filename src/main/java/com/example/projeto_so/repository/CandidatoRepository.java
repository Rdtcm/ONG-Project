package com.example.projeto_so.repository;

import com.example.projeto_so.entity.AfiliacaoStatus;
import com.example.projeto_so.entity.Candidato;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Optional<Candidato> findByEmail(String email);
    Optional<Candidato> findByCpfOuCnpj(String cpfOuCnpj);
    boolean existsByEmail(String email);
    boolean existsByCpfOuCnpj(String cpfOuCnpj);

    @Query("SELECT c FROM Candidato c WHERE c.email = :email OR c.cpfOuCnpj = :cpfOuCnpj")
    Optional<Candidato> buscarPorEmailCPF(@Param("email") String email, @Param("cpfOuCnpj") String cpfOuCnpj);

    @Query("SELECT c FROM Candidato c JOIN c.afiliacao a WHERE a.status = :status")
    List<Candidato> buscarPorStatus(@Param("status") AfiliacaoStatus status);
}