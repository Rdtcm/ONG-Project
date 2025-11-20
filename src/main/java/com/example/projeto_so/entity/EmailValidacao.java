package com.example.projeto_so.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "email_validacoes")
public class EmailValidacao {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    private Boolean utilizado = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private Candidato candidato;

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public Boolean getUtilizado() {
        return utilizado;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public String gerarToken() {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        this.token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        this.dataEnvio = LocalDateTime.now();
        this.dataExpiracao = dataEnvio.plusHours(2);
        this.utilizado = Boolean.FALSE;
        return token;
    }

    public boolean validarToken(String token) {
        return this.token.equals(token) && Boolean.FALSE.equals(utilizado) && LocalDateTime.now().isBefore(dataExpiracao);
    }

    public void marcarComoUtilizado() {
        this.utilizado = Boolean.TRUE;
    }
}

