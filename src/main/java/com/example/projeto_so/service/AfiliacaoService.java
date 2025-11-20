package com.example.projeto_so.service;

import com.example.projeto_so.entity.Afiliacao;
import com.example.projeto_so.entity.AfiliacaoStatus;
import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.repository.AfiliacaoRepository;
import com.example.projeto_so.repository.CandidatoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AfiliacaoService {

    private final CandidatoRepository candidatoRepository;
    private final AfiliacaoRepository afiliacaoRepository;

    public AfiliacaoService(CandidatoRepository candidatoRepository,
                            AfiliacaoRepository afiliacaoRepository) {
        this.candidatoRepository = candidatoRepository;
        this.afiliacaoRepository = afiliacaoRepository;
    }

    public Afiliacao solicitarAfiliacao(Candidato candidato) {
        Afiliacao afiliacao = candidato.getAfiliacao();
        if (afiliacao == null) {
            afiliacao = new Afiliacao();
        }
        afiliacao.setCandidato(candidato);
        afiliacao.atualizarStatus(AfiliacaoStatus.RASCUNHO);
        candidato.setAfiliacao(afiliacao);
        candidato.setSituacao(AfiliacaoStatus.RASCUNHO.name());
        candidato.setDataSolicitacao(LocalDateTime.now());
        candidatoRepository.save(candidato);
        return afiliacaoRepository.save(afiliacao);
    }

    public boolean aprovarAfiliacao(Long candidatoId) {
        Afiliacao afiliacao = obterAfiliacaoPorCandidato(candidatoId);
        afiliacao.aprovar();
        atualizarSituacaoCandidato(afiliacao, AfiliacaoStatus.APROVADO);
        afiliacaoRepository.save(afiliacao);
        return true;
    }

    public boolean reprovarAfiliacao(Long candidatoId, String motivo) {
        Afiliacao afiliacao = obterAfiliacaoPorCandidato(candidatoId);
        afiliacao.reprovar(motivo);
        atualizarSituacaoCandidato(afiliacao, AfiliacaoStatus.REPROVADO);
        afiliacaoRepository.save(afiliacao);
        return true;
    }

    public boolean atualizarStatus(Long candidatoId, AfiliacaoStatus status) {
        Afiliacao afiliacao = obterAfiliacaoPorCandidato(candidatoId);
        afiliacao.atualizarStatus(status);
        atualizarSituacaoCandidato(afiliacao, status);
        afiliacaoRepository.save(afiliacao);
        return true;
    }

    public boolean atualizarStatus(Long candidatoId, String status) {
        return atualizarStatus(candidatoId, AfiliacaoStatus.valueOf(status));
    }

    public Optional<Candidato> buscarPorEmailOuCpf(String email, String cpfOuCnpj) {
        return candidatoRepository.buscarPorEmailCPF(email, cpfOuCnpj);
    }

    public List<Candidato> listarTodos() {
        return candidatoRepository.findAll();
    }

    public Candidato buscarPorId(Long id) {
        Long idSeguro = Objects.requireNonNull(id, "id é obrigatório");
        return candidatoRepository.findById(idSeguro)
            .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));
    }

    private Afiliacao obterAfiliacaoPorCandidato(Long candidatoId) {
        Long idSeguro = Objects.requireNonNull(candidatoId, "candidatoId é obrigatório");
        return afiliacaoRepository.findByCandidatoId(idSeguro)
            .orElseThrow(() -> new IllegalArgumentException("Afiliacao não encontrada"));
    }

    private void atualizarSituacaoCandidato(Afiliacao afiliacao, AfiliacaoStatus status) {
        Candidato candidato = afiliacao.getCandidato();
        candidato.setSituacao(status.name());
        candidatoRepository.save(candidato);
    }
}