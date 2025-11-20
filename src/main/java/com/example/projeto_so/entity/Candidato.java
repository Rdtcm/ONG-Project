package com.example.projeto_so.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "candidatos")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "cpf_ou_cnpj", unique = true, nullable = false, length = 14)
    private String cpfOuCnpj;

    @Column(name = "tipo_pessoa", length = 2)
    private String tipoPessoa;

    private String nome;

    @Column(length = 1)
    private String sexo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private String nacionalidade;

    @Column(name = "endereco_residencial", columnDefinition = "TEXT")
    private String enderecoResidencial;

    @Column(name = "endereco_comercial", columnDefinition = "TEXT")
    private String enderecoComercial;

    private String profissao;

    @Column(name = "representante_legal")
    private String representanteLegal;

    @Column(name = "aceitou_termo", nullable = false)
    private boolean aceitouTermo = false;

    private String situacao = "INICIADO";

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @OneToOne(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Perfil perfil;

    @OneToOne(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Afiliacao afiliacao;

    @OneToOne(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private TermoCompromisso termoCompromisso;

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailValidacao> emailValidacoes = new ArrayList<>();

    @OneToMany(mappedBy = "candidato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certidao> certidoes = new ArrayList<>();

    public Candidato() {
    }

    @PrePersist
    public void prePersist() {
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
    }

    public boolean validarDadosIdentificacao() {
        return email != null && !email.isBlank()
            && cpfOuCnpj != null && !cpfOuCnpj.isBlank()
            && nome != null && !nome.isBlank()
            && (tipoPessoa == null || tipoPessoa.isBlank() || tipoPessoa.equalsIgnoreCase("PF")
                || (tipoPessoa.equalsIgnoreCase("PJ") && representanteLegal != null && !representanteLegal.isBlank()));
    }

    public void adicionarCertidoes(List<Certidao> novasCertidoes) {
        certidoes.clear();
        if (novasCertidoes == null) {
            return;
        }
        novasCertidoes.forEach(certidao -> certidao.setCandidato(this));
        certidoes.addAll(novasCertidoes);
    }

    public void definirRepresentanteLegal(String representante) {
        this.representanteLegal = representante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getEnderecoResidencial() {
        return enderecoResidencial;
    }

    public void setEnderecoResidencial(String enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }

    public String getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(String enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public boolean isAceitouTermo() {
        return aceitouTermo;
    }

    public void setAceitouTermo(boolean aceitouTermo) {
        this.aceitouTermo = aceitouTermo;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil != null) {
            perfil.setCandidato(this);
        }
    }

    public Afiliacao getAfiliacao() {
        return afiliacao;
    }

    public void setAfiliacao(Afiliacao afiliacao) {
        this.afiliacao = afiliacao;
        if (afiliacao != null) {
            afiliacao.setCandidato(this);
        }
    }

    public TermoCompromisso getTermoCompromisso() {
        return termoCompromisso;
    }

    public void setTermoCompromisso(TermoCompromisso termoCompromisso) {
        this.termoCompromisso = termoCompromisso;
        if (termoCompromisso != null) {
            termoCompromisso.setCandidato(this);
        }
    }

    public List<EmailValidacao> getEmailValidacoes() {
        return Collections.unmodifiableList(emailValidacoes);
    }

    public void adicionarEmailValidacao(EmailValidacao emailValidacao) {
        emailValidacao.setCandidato(this);
        emailValidacoes.add(emailValidacao);
    }

    public List<Certidao> getCertidoes() {
        return Collections.unmodifiableList(certidoes);
    }
}
