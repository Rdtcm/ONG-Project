package com.example.projeto_so.controllers;

import com.example.projeto_so.service.CredencialService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

    private final CredencialService credencialService;

    public IndexController(CredencialService credencialService) {
        this.credencialService = credencialService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("heroMessage", "Conecte-se a uma rede de impacto e solidariedade.");
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String email,
                                 @RequestParam String senha,
                                 RedirectAttributes redirectAttributes) {
        if (!credencialService.validar(email, senha)) {
            redirectAttributes.addFlashAttribute("erroLogin", "Credenciais inválidas. Verifique e tente novamente.");
            redirectAttributes.addFlashAttribute("emailPrefill", email);
            return "redirect:/login";
        }
        return "redirect:/afiliacao/inicio";
    }

    @GetMapping("/cadastro-senha")
    public String cadastroSenha() {
        return "cadastro-senha";
    }

    @PostMapping("/cadastro-senha")
    public String processarCadastroSenha(@RequestParam String email,
                                         @RequestParam String senha,
                                         @RequestParam String confirmacao,
                                         RedirectAttributes redirectAttributes) {
        List<String> erros = new ArrayList<>();
        if (!StringUtils.hasText(email)) {
            erros.add("Informe um e-mail.");
        }
        if (!StringUtils.hasText(senha) || senha.length() < 6) {
            erros.add("A senha deve ter pelo menos 6 caracteres.");
        }
        if (!senha.equals(confirmacao)) {
            erros.add("As senhas não conferem.");
        }

        if (!erros.isEmpty()) {
            redirectAttributes.addFlashAttribute("errosCadastro", erros);
            redirectAttributes.addFlashAttribute("emailPrefill", email);
            return "redirect:/cadastro-senha";
        }

        credencialService.registrar(email, senha);
        redirectAttributes.addFlashAttribute("cadastroSucesso", "Senha criada com sucesso! Faça login para continuar.");
        redirectAttributes.addFlashAttribute("emailPrefill", email);
        return "redirect:/login";
    }
}
