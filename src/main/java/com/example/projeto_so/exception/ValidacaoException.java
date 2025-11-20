package com.example.projeto_so.exception;

import java.util.List;

public class ValidacaoException extends RuntimeException {

    private final List<String> erros;

    public ValidacaoException(String mensagem, List<String> erros) {
        super(mensagem);
        this.erros = erros;
    }

    public List<String> getErros() {
        return erros;
    }
}

