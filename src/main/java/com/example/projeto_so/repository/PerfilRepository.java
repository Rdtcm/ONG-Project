package com.example.projeto_so.repository;

import com.example.projeto_so.entity.Perfil;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByCandidatoId(Long candidatoId);
}

