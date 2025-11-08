package com.example.projeto_so.controllers;

import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.service.AfiliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AfiliacaoController {
    
    @Autowired
    private AfiliacaoService afiliacaoService;
    
    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("candidato", new Candidato());
        return "formulario";
    }
    
    @PostMapping("/solicitar-afiliacao")
    public String processarFormulario(@ModelAttribute Candidato candidato, 
                                    RedirectAttributes redirectAttributes) {
        try {
            Candidato candidatoSalvo = afiliacaoService.salvarCandidato(candidato);
            redirectAttributes.addFlashAttribute("sucesso", 
                "Solicitação enviada com sucesso! ID: " + candidatoSalvo.getId());
            return "redirect:/confirmacao";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/";
        }
    }
    
    @GetMapping("/confirmacao")
    public String mostrarConfirmacao() {
        return "confirmacao";
    }
    
    @GetMapping("/candidatos")
    public String listarCandidatos(Model model) {
        model.addAttribute("candidatos", afiliacaoService.listarTodos());
        return "candidatos";
    }
}