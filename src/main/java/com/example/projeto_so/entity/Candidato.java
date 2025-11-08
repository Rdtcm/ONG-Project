package com.example.projeto_so.entity;

import javax.persistence.*;

import org.apache.poi.ss.formula.functions.Columns;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidatos")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, name = "")
    private String email;
    
    @Column(name = "cpf_ou_cnpj", unique = true, nullable = false)
    private String cpfOuCnpj;
    
    private String nome;
    private String sexo;
    
    Columns(name 
    = "data_nascimento")
    private String dataNascimento;
    
    private String nacionalidade;
    
    @Column(name = "endereco_residencial", nullable = false, unique = false)
    private String enderecoResidencial;
    
    @Column(name = "endereco_comercial", nullable = false, unique = false)
    private String enderecoComercial;
    
    private String profissao;
    
    @Column(name = "habilidades_interesses", length = 1000, nullable = false, unique = false)
    private String habilidadesInteresses;
    
    private boolean aceitouTermo;
    
    @Enumerated(EnumType.STRING)
    private SituacaoCandidato situacao = SituacaoCandidato.AGUARDANDO_VALIDACAO;
    
    @Column(name = "data_solicitacao", nullable = false, unique = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    
    // Construtores
    public Candidato() {}
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCpfOuCnpj() { return cpfOuCnpj; }
    public void setCpfOuCnpj(String cpfOuCnpj) { this.cpfOuCnpj = cpfOuCnpj; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
    
    public String getEnderecoResidencial() { return enderecoResidencial; }
    public void setEnderecoResidencial(String enderecoResidencial) { this.enderecoResidencial = enderecoResidencial; }
    
    public String getEnderecoComercial() { return enderecoComercial; }
    public void setEnderecoComercial(String enderecoComercial) { this.enderecoComercial = enderecoComercial; }
    
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    
    public String getHabilidadesInteresses() { return habilidadesInteresses; }
    public void setHabilidadesInteresses(String habilidadesInteresses) { this.habilidadesInteresses = habilidadesInteresses; }
    
    public boolean isAceitouTermo() { return aceitouTermo; }
    public void setAceitouTermo(boolean aceitouTermo) { this.aceitouTermo = aceitouTermo; }
    
    public SituacaoCandidato getSituacao() { return situacao; }
    public void setSituacao(SituacaoCandidato situacao) { this.situacao = situacao; }
    
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
}

enum SituacaoCandidato {
    AGUARDANDO_VALIDACAO,
    AGUARDANDO_APROVACAO,
    APROVADO,
    REJEITADO,
    BLOQUEADO
}
