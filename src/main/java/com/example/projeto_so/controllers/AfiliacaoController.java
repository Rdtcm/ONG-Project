package com.example.projeto_so.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.projeto_so.entity.Afiliacao;
import com.example.projeto_so.entity.AfiliacaoStatus;
import com.example.projeto_so.entity.Candidato;
import com.example.projeto_so.entity.Certidao;
import com.example.projeto_so.entity.EmailValidacao;
import com.example.projeto_so.entity.Perfil;
import com.example.projeto_so.exception.ValidacaoException;
import com.example.projeto_so.repository.CandidatoRepository;
import com.example.projeto_so.service.AfiliacaoService;
import com.example.projeto_so.service.EmailService;
import com.example.projeto_so.service.TermoService;
import com.example.projeto_so.service.ValidacaoService;

@Controller
@RequestMapping("/afiliacao")
@SessionAttributes("candidatoEmProcesso")
public class AfiliacaoController {

    private final CandidatoRepository candidatoRepository;
    private final AfiliacaoService afiliacaoService;
    private final EmailService emailService;
    private final ValidacaoService validacaoService;
    private final TermoService termoService;

    public AfiliacaoController(CandidatoRepository candidatoRepository,
                               AfiliacaoService afiliacaoService,
                               EmailService emailService,
                               ValidacaoService validacaoService,
                               TermoService termoService) {
        this.candidatoRepository = candidatoRepository;
        this.afiliacaoService = afiliacaoService;
        this.emailService = emailService;
        this.validacaoService = validacaoService;
        this.termoService = termoService;
    }

    @ModelAttribute("candidatoEmProcesso")
    public Candidato candidatoEmProcesso() {
        return new Candidato();
    }

    @GetMapping("/inicio")
    public String exibirInicio(Model model) {
        model.addAttribute("etapa", "inicio");
        return "afiliacao/inicio";
    }

    @GetMapping("/form")
    public String legado() {
        return "redirect:/afiliacao/inicio";
    }

    @PostMapping("/iniciar")
    public String iniciarProcessoAfiliacao(@RequestParam String email,
                                           @RequestParam String cpfOuCnpj,
                                           @RequestParam String tipoPessoa,
                                           Model model,
                                           @ModelAttribute("candidatoEmProcesso") Candidato candidato) {
        if (afiliacaoService.buscarPorEmailOuCpf(email, cpfOuCnpj).isPresent()) {
            model.addAttribute("erro", "Já existe uma solicitação com este e-mail ou CPF/CNPJ.");
            return "afiliacao/inicio";
        }

        candidato.setEmail(email);
        candidato.setCpfOuCnpj(cpfOuCnpj);
        candidato.setTipoPessoa(tipoPessoa.toUpperCase());
        model.addAttribute("candidato", candidato);

        return "PF".equalsIgnoreCase(tipoPessoa)
            ? "afiliacao/identificacao-pf"
            : "afiliacao/identificacao-pj";
    }

    @PostMapping("/identificacao")
    public String processarFormularioIdentificacao(@RequestParam Map<String, String> dados,
                                                   Model model,
                                                   @ModelAttribute("candidatoEmProcesso") Candidato candidato) {
        Map<String, Object> dadosValidacao = new HashMap<>(dados);
        if (dados.containsKey("dataNascimento") && StringUtils.hasText(dados.get("dataNascimento"))) {
            dadosValidacao.put("dataNascimento", LocalDate.parse(dados.get("dataNascimento")));
        }
        dadosValidacao.put("email", candidato.getEmail());
        dadosValidacao.put("cpfOuCnpj", candidato.getCpfOuCnpj());
        dadosValidacao.put("tipoPessoa", candidato.getTipoPessoa());
        if ("PJ".equalsIgnoreCase(candidato.getTipoPessoa())) {
            dadosValidacao.put("nome", dados.get("razaoSocial"));
        }

        try {
            validacaoService.validarDadosIdentificacao(dadosValidacao);
            if ("PJ".equalsIgnoreCase(candidato.getTipoPessoa())) {
                Map<String, Object> documentos = new HashMap<>();
                documentos.put("cnpj", candidato.getCpfOuCnpj());
                documentos.put("razaoSocial", dados.get("razaoSocial"));
                documentos.put("representanteLegal", dados.get("representanteLegal"));
                validacaoService.validarDocumentosPJ(documentos);
                candidato.setNome(dados.get("razaoSocial"));
                candidato.setEnderecoComercial(dados.get("enderecoComercial"));
                candidato.definirRepresentanteLegal(dados.get("representanteLegal"));
            } else {
                candidato.setNome(dados.get("nome"));
                candidato.setSexo(dados.get("sexo"));
                if (dadosValidacao.get("dataNascimento") instanceof LocalDate nascimento) {
                    candidato.setDataNascimento(nascimento);
                }
                candidato.setNacionalidade(dados.get("nacionalidade"));
                candidato.setEnderecoResidencial(dados.get("enderecoResidencial"));
                candidato.setProfissao(dados.get("profissao"));
            }

            Afiliacao afiliacao = afiliacaoService.solicitarAfiliacao(candidato);
            model.addAttribute("afiliacao", afiliacao);

            if ("PJ".equalsIgnoreCase(candidato.getTipoPessoa())) {
                return "afiliacao/certidoes";
            }
            return "afiliacao/perfil";

        } catch (ValidacaoException e) {
            model.addAttribute("erros", e.getErros());
            return "PJ".equalsIgnoreCase(candidato.getTipoPessoa())
                ? "afiliacao/identificacao-pj"
                : "afiliacao/identificacao-pf";
        }
    }

    @PostMapping("/certidoes")
    public String processarCertidoesPJ(@RequestParam(name = "tipo") List<String> tipos,
                                       @RequestParam(name = "numero") List<String> numeros,
                                       @RequestParam(name = "dataEmissao") List<String> datasEmissao,
                                       @RequestParam(name = "dataValidade") List<String> datasValidade,
                                       @RequestParam(name = "arquivoPath") List<String> arquivos,
                                       Model model,
                                       @ModelAttribute("candidatoEmProcesso") Candidato candidato) {
        List<Certidao> certidoes = new ArrayList<>();
        IntStream.range(0, tipos.size()).forEach(i -> {
            if (!StringUtils.hasText(tipos.get(i))) {
                return;
            }
            Certidao certidao = new Certidao();
            certidao.setTipo(tipos.get(i));
            certidao.setNumero(numeros.get(i));
            certidao.setDataEmissao(LocalDate.parse(datasEmissao.get(i)));
            certidao.setDataValidade(LocalDate.parse(datasValidade.get(i)));
            certidao.setArquivoPath(arquivos.get(i));
            certidoes.add(certidao);
        });

        try {
            validacaoService.validarCertidoes(certidoes);
            candidato.adicionarCertidoes(certidoes);
            candidatoRepository.save(candidato);
            return "afiliacao/perfil";
        } catch (ValidacaoException e) {
            model.addAttribute("erros", e.getErros());
            return "afiliacao/certidoes";
        }
    }

    @PostMapping("/perfil")
    public String processarFormularioPerfil(@RequestParam String habilidades,
                                            @RequestParam String interesses,
                                            @RequestParam String tipoPerfil,
                                            Model model,
                                            @ModelAttribute("candidatoEmProcesso") Candidato candidato) {
        Map<String, Object> dados = Map.of(
            "habilidades", habilidades,
            "interesses", interesses,
            "tipoPerfil", tipoPerfil
        );

        try {
            validacaoService.validarDadosPerfil(dados);

            List<String> habilidadesList = parseLista(habilidades);
            List<String> interessesList = parseLista(interesses);

            Perfil perfil = candidato.getPerfil();
            if (perfil == null) {
                perfil = new Perfil();
                perfil.setCandidato(candidato);
            }
            perfil.setTipoPerfil(tipoPerfil);
            perfil.adicionarHabilidades(habilidadesList);
            perfil.adicionarInteresses(interessesList);
            candidato.setPerfil(perfil);
            candidatoRepository.save(candidato);

            model.addAttribute("termo", termoService.obterTermoVigente());
            return "afiliacao/termo";
        } catch (ValidacaoException e) {
            model.addAttribute("erros", e.getErros());
            return "afiliacao/perfil";
        }
    }

    @PostMapping("/termo")
    public String processarAceiteTermo(@RequestParam(defaultValue = "false") boolean aceite,
                                       Model model,
                                       @ModelAttribute("candidatoEmProcesso") Candidato candidato,
                                       SessionStatus sessionStatus) {
        candidato.setAceitouTermo(aceite);
        termoService.registrarAceite(candidato.getId(), aceite);

        if (!aceite) {
            afiliacaoService.atualizarStatus(candidato.getId(), AfiliacaoStatus.BLOQUEADO);
            sessionStatus.setComplete();
            model.addAttribute("mensagem", "Processo encerrado. Você pode iniciar novamente quando desejar.");
            return "afiliacao/processo-encerrado";
        }

        afiliacaoService.atualizarStatus(candidato.getId(), AfiliacaoStatus.AGUARDANDO_VALIDACAO);
        EmailValidacao emailValidacao = emailService.prepararEmailValidacao(candidato);
        emailService.enviarEmailValidacao(candidato.getEmail(), emailValidacao.getToken());

        model.addAttribute("tokenGerado", emailValidacao.getToken());
        return "afiliacao/email-validacao";
    }

    @GetMapping("/validar-email")
    public String validarEmail(@RequestParam String token,
                               Model model,
                               SessionStatus sessionStatus) {
        try {
            boolean valido = emailService.validarToken(token);
            if (valido) {
                model.addAttribute("mensagem", "E-mail validado com sucesso! Aguarde a aprovação.");
                sessionStatus.setComplete();
            } else {
                model.addAttribute("mensagem", "Token inválido ou expirado.");
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("mensagem", ex.getMessage());
        }
        return "afiliacao/email-validacao";
    }

    @GetMapping("/candidatos")
    public String listarCandidatos(Model model) {
        model.addAttribute("candidatos", afiliacaoService.listarTodos());
        return "candidatos";
    }

    private List<String> parseLista(String texto) {
        String[] valores = texto.split(",");
        List<String> resposta = new ArrayList<>();
        for (String valor : valores) {
            if (StringUtils.hasText(valor)) {
                resposta.add(valor.trim());
            }
        }
        return resposta;
    }
}