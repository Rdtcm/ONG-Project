package com.example.projeto_so.entity;

// ✅ CORRETO: Spring Boot 3+ usa jakarta.persistence
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatos")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "cpf_ou_cnpj", unique = true, nullable = false)
    private String cpfOuCnpj;
    
    private String nome;
    private String sexo;
    
    // ✅ CORRIGIDO: Removido import errado do Apache POI
    @Column(name = "data_nascimento")
    private String dataNascimento;
    
    private String nacionalidade;
    
    // ✅ CORRIGIDO: Removido unique=false (não necessário)
    @Column(name = "endereco_residencial")
    private String enderecoResidencial;
    
    @Column(name = "endereco_comercial")
    private String enderecoComercial;
    
    private String profissao;
    
    @Column(name = "habilidades_interesses", length = 1000)
    private String habilidadesInteresses;
    
    private boolean aceitouTermo;
    
    // ✅ CORRIGIDO: Usando String simples (mais fácil)
    private String situacao = "AGUARDANDO_VALIDACAO";
    
    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    
    // Construtores
    public Candidato() {}
    
    // Getters e Setters (mantenha todos que você tinha)
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
    
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
}
