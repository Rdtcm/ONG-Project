package com.example.projeto_so.service;

import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.entity.TermoCompromisso;
import com.example.projeto_so.repository.CandidatoRepository;
import com.example.projeto_so.repository.TermoCompromissoRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class TermoService {

    private final TermoCompromissoRepository termoRepository;
    private final CandidatoRepository candidatoRepository;

    public TermoService(TermoCompromissoRepository termoRepository, CandidatoRepository candidatoRepository) {
        this.termoRepository = termoRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public TermoCompromisso obterTermoVigente() {
        return termoRepository.findFirstByVigenteTrueOrderByIdDesc()
            .orElseGet(this::criarTermoPadrao);
    }

    public boolean registrarAceite(Long candidatoId, boolean aceite) {
        Long idSeguro = Objects.requireNonNull(candidatoId, "candidatoId é obrigatório");
        Candidato candidato = candidatoRepository.findById(idSeguro)
            .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado"));

        TermoCompromisso termo = candidato.getTermoCompromisso();
        if (termo == null) {
            termo = new TermoCompromisso();
            termo.setConteudo(obterTermoVigente().getConteudo());
            termo.setVersao(obterTermoVigente().getVersao());
            termo.setCandidato(candidato);
        }

        termo.registrarAceite(LocalDateTime.now(), aceite);
        candidato.setAceitouTermo(aceite);
        candidato.setTermoCompromisso(termo);
        termoRepository.save(termo);
        candidatoRepository.save(candidato);
        return true;
    }

    private TermoCompromisso criarTermoPadrao() {
        TermoCompromisso termo = new TermoCompromisso();
        termo.setConteudo("""
            Ao aderir à Rede Mais Social, comprometo-me a atuar com ética,
            transparência e foco em impacto social, respeitando a legislação vigente e
            as boas práticas de governança.
            """.trim());
        termo.setVersao("1.0");
        termo.setVigente(Boolean.TRUE);
        return termoRepository.save(termo);
    }
}

