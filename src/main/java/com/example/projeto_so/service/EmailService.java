package com.example.projeto_so.service;

import com.example.projeto_so.entity.AfiliacaoStatus;
import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.entity.EmailValidacao;
import com.example.projeto_so.repository.EmailValidacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final EmailValidacaoRepository emailValidacaoRepository;
    private final AfiliacaoService afiliacaoService;

    public EmailService(EmailValidacaoRepository emailValidacaoRepository,
                        AfiliacaoService afiliacaoService) {
        this.emailValidacaoRepository = emailValidacaoRepository;
        this.afiliacaoService = afiliacaoService;
    }

    public EmailValidacao prepararEmailValidacao(Candidato candidato) {
        EmailValidacao emailValidacao = new EmailValidacao();
        emailValidacao.setCandidato(candidato);
        emailValidacao.gerarToken();
        candidato.adicionarEmailValidacao(emailValidacao);
        return emailValidacaoRepository.save(emailValidacao);
    }

    public boolean enviarEmailValidacao(String email, String token) {
        LOGGER.info("Simulando envio de e-mail de validação para {} com token {}", email, token);
        return true;
    }

    public boolean enviarEmailAprovacao(String email) {
        LOGGER.info("Simulando envio de e-mail de aprovação para {}", email);
        return true;
    }

    public boolean enviarEmailReprovacao(String email, String motivo) {
        LOGGER.info("Simulando envio de e-mail de reprovação para {}. Motivo: {}", email, motivo);
        return true;
    }

    public boolean validarToken(String token) {
        EmailValidacao emailValidacao = emailValidacaoRepository.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (!emailValidacao.validarToken(token)) {
            return false;
        }

        emailValidacao.marcarComoUtilizado();
        emailValidacaoRepository.save(emailValidacao);

        afiliacaoService.atualizarStatus(emailValidacao.getCandidato().getId(), AfiliacaoStatus.AGUARDANDO_APROVACAO);
        return true;
    }
}

