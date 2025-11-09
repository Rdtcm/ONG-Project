package com.example.projeto_so.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.repository.CandidatoRepository;

@Service
public class AfiliacaoService {
    
    @Autowired
    private CandidatoRepository candidatoRepository;
    
    public Candidato salvarCandidato(Candidato candidato) {
        // verifica se já existe cadastro
        if (candidatoRepository.existsByEmail(candidato.getEmail()) || 
            candidatoRepository.existsByCpfOuCnpj(candidato.getCpfOuCnpj())) {
            throw new RuntimeException("Já existe um cadastro com este e-mail ou CPF/CNPJ");
        }
        
        if (candidato.isAceitouTermo()) {
            candidato.setSituacao("AGUARDANDO_VALIDACAO");
        } else {
            candidato.setSituacao("BLOQUEADO");
        }
        
        return candidatoRepository.save(candidato);
    }
    
    public List<Candidato> listarTodos() {
        return candidatoRepository.findAll();
    }
    
    public Candidato buscarPorId(Long id) {
        return candidatoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
    }
}