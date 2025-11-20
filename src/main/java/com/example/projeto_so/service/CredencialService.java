package com.example.projeto_so.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CredencialService {

    private final Map<String, String> credenciais = new ConcurrentHashMap<>();

    public void registrar(String email, String senha) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(senha)) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }
        credenciais.put(email.trim().toLowerCase(), senha);
    }

    public boolean validar(String email, String senha) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(senha)) {
            return false;
        }
        String armazenada = credenciais.get(email.trim().toLowerCase());
        return senha.equals(armazenada);
    }
}

