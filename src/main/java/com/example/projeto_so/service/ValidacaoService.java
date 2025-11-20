package com.example.projeto_so.service;

import com.example.projeto_so.entity.Certidao;
import com.example.projeto_so.exception.ValidacaoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidacaoService {

    public boolean validarDadosIdentificacao(Map<String, Object> dados) {
        List<String> erros = new ArrayList<>();
        String tipoPessoa = (String) dados.getOrDefault("tipoPessoa", "PF");
        boolean pessoaFisica = !"PJ".equalsIgnoreCase(tipoPessoa);

        validarCampoObrigatorio(dados, "nome", "Nome é obrigatório", erros);
        validarCampoObrigatorio(dados, "email", "E-mail é obrigatório", erros);
        validarCampoObrigatorio(dados, "cpfOuCnpj", "CPF/CNPJ é obrigatório", erros);
        if (pessoaFisica) {
            validarCampoObrigatorio(dados, "nacionalidade", "Nacionalidade é obrigatória", erros);
        }

        if (dados.containsKey("dataNascimento")) {
            Object data = dados.get("dataNascimento");
            if (data instanceof LocalDate localDate && localDate.isAfter(LocalDate.now())) {
                erros.add("Data de nascimento não pode ser futura.");
            }
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Erros ao validar identificação", erros);
        }

        return true;
    }

    public boolean validarDadosPerfil(Map<String, Object> dados) {
        List<String> erros = new ArrayList<>();
        validarCampoObrigatorio(dados, "habilidades", "Informe ao menos uma habilidade", erros);
        validarCampoObrigatorio(dados, "interesses", "Informe ao menos um interesse", erros);
        validarCampoObrigatorio(dados, "tipoPerfil", "Selecione um tipo de perfil", erros);

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Erros ao validar perfil", erros);
        }

        return true;
    }

    public boolean validarCertidoes(List<Certidao> certidoes) {
        List<String> erros = new ArrayList<>();

        if (certidoes == null || certidoes.isEmpty()) {
            erros.add("Adicione pelo menos uma certidão.");
        } else {
            certidoes.forEach(certidao -> {
                if (!certidao.validarCertidao()) {
                    erros.add("Certidão inválida: " + certidao.getTipo());
                }
            });
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Erros ao validar certidões", erros);
        }

        return true;
    }

    public boolean validarDocumentosPJ(Map<String, Object> dados) {
        List<String> erros = new ArrayList<>();
        validarCampoObrigatorio(dados, "cnpj", "CNPJ é obrigatório", erros);
        validarCampoObrigatorio(dados, "razaoSocial", "Razão social é obrigatória", erros);
        validarCampoObrigatorio(dados, "representanteLegal", "Representante legal é obrigatório", erros);

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Erros nos documentos PJ", erros);
        }

        return true;
    }

    private void validarCampoObrigatorio(Map<String, Object> dados, String campo, String mensagem, List<String> erros) {
        Object valor = dados.get(campo);
        if (valor == null) {
            erros.add(mensagem);
            return;
        }
        if (valor instanceof String texto && !StringUtils.hasText(texto)) {
            erros.add(mensagem);
        }
        if (valor instanceof List<?> lista && lista.isEmpty()) {
            erros.add(mensagem);
        }
    }
}

