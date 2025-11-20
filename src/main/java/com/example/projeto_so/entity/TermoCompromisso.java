package com.example.projeto_so.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "termos_compromisso")
public class TermoCompromisso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String versao = "1.0";

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_aceite")
    private LocalDateTime dataAceite;

    private Boolean aceito = Boolean.FALSE;

    private Boolean vigente = Boolean.TRUE;

    @OneToOne
    @JoinColumn(name = "candidato_id", unique = true)
    private Candidato candidato;

    public Long getId() {
        return id;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataAceite() {
        return dataAceite;
    }

    public Boolean getAceito() {
        return aceito;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public TermoCompromisso obterTermoVigente() {
        return Boolean.TRUE.equals(vigente) ? this : null;
    }

    public void registrarAceite(LocalDateTime dataAceite, boolean aceite) {
        this.dataAceite = dataAceite;
        this.aceito = aceite;
    }
}

