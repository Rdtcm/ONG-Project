package com.example.projeto_so.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "afiliacoes")
public class Afiliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AfiliacaoStatus status = AfiliacaoStatus.RASCUNHO;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "motivo_rejeicao", columnDefinition = "TEXT")
    private String motivoRejeicao;

    @OneToOne
    @JoinColumn(name = "candidato_id", nullable = false, unique = true)
    private Candidato candidato;

    public Long getId() {
        return id;
    }

    public AfiliacaoStatus getStatus() {
        return status;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public void aprovar() {
        atualizarStatus(AfiliacaoStatus.APROVADO);
        this.motivoRejeicao = null;
    }

    public void reprovar(String motivo) {
        atualizarStatus(AfiliacaoStatus.REPROVADO);
        this.motivoRejeicao = motivo;
    }

    public void atualizarStatus(AfiliacaoStatus novoStatus) {
        this.status = novoStatus;
        if (novoStatus == AfiliacaoStatus.APROVADO || novoStatus == AfiliacaoStatus.REPROVADO) {
            this.dataAprovacao = LocalDateTime.now();
        }
    }
}

