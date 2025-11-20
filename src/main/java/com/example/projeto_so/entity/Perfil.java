package com.example.projeto_so.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perfis")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "perfil_habilidades", joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "habilidade")
    private List<String> habilidades = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "perfil_interesses", joinColumns = @JoinColumn(name = "perfil_id"))
    @Column(name = "interesse")
    private List<String> interesses = new ArrayList<>();

    @Column(name = "tipo_perfil")
    private String tipoPerfil;

    @OneToOne
    @JoinColumn(name = "candidato_id", nullable = false, unique = true)
    private Candidato candidato;

    public Long getId() {
        return id;
    }

    public List<String> getHabilidades() {
        return habilidades;
    }

    public List<String> getInteresses() {
        return interesses;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public void adicionarHabilidades(List<String> novasHabilidades) {
        if (novasHabilidades != null) {
            habilidades.addAll(novasHabilidades.stream().map(String::trim).filter(s -> !s.isEmpty()).toList());
        }
    }

    public void adicionarInteresses(List<String> novosInteresses) {
        if (novosInteresses != null) {
            interesses.addAll(novosInteresses.stream().map(String::trim).filter(s -> !s.isEmpty()).toList());
        }
    }

    public boolean validarPerfil() {
        return !habilidades.isEmpty() && !interesses.isEmpty() && tipoPerfil != null && !tipoPerfil.isBlank();
    }
}

