package com.example.projeto_so.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String home() {
        // o retorno é o nome do arquivo HTML (sem extensão)
        // ex: src/main/resources/templates/index.html
        return "formulario";
    }
}

