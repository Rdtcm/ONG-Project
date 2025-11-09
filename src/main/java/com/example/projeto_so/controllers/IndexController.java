package com.example.projeto_so.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/afiliacao/form";  // Redireciona para o formulário
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("mensagem", "Dashboard da Rede");
        return "dashboard";
    }
}

