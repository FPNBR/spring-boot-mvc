package br.com.fpnbr.springbootmvc.controllers;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import br.com.fpnbr.springbootmvc.models.Telefone;
import br.com.fpnbr.springbootmvc.repositories.PessoaRepository;
import br.com.fpnbr.springbootmvc.repositories.ProfissaoRepository;
import br.com.fpnbr.springbootmvc.repositories.TelefoneRepository;
import br.com.fpnbr.springbootmvc.util.ReportUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @Autowired
    private ReportUtil reportUtil;

    @GetMapping( "/cadastro-pessoa")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", new Pessoa());
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);
        modelAndView.addObject("profissoes", profissaoRepository.findAll());

        return modelAndView;
    }

    @PostMapping(value = "/salvar-pessoa", consumes = {"multipart/form-data"})
    public ModelAndView salvarPessoa(@Valid Pessoa pessoa, BindingResult bindingResult, final MultipartFile file) throws IOException {

        pessoa.setTelefones(telefoneRepository.findTelefoneByPessoa(pessoa.getId()));

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
            Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
            modelAndView.addObject("pessoas", pessoaIterable);
            modelAndView.addObject("pessoa", pessoa);

            List<String> messageErrors = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                messageErrors.add(objectError.getDefaultMessage()); // Vem das annotations @NotEmpty e @NotNull
            }

            modelAndView.addObject("messageErrors", messageErrors);
            modelAndView.addObject("profissoes", profissaoRepository.findAll());
            return modelAndView;

        }else {
            if (file.getSize() > 0) { // Cadastrando um currículo
                pessoa.setCurriculo(file.getBytes());
                pessoa.setTipoArquivoCurriculo(file.getContentType());
                pessoa.setNomeArquivoCurriculo(file.getOriginalFilename());

            }else {
                if (pessoa.getId() != null && pessoa.getId() > 0) { // Não perder o currículo quando estiver editando o usuário existente
                    Pessoa pessoaTemp = pessoaRepository.findById(pessoa.getId()).get();
                    pessoa.setCurriculo(pessoaTemp.getCurriculo());
                    pessoa.setTipoArquivoCurriculo(pessoaTemp.getTipoArquivoCurriculo());
                    pessoa.setNomeArquivoCurriculo(pessoaTemp.getNomeArquivoCurriculo());
                }
            }

            pessoaRepository.save(pessoa);
            ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
            Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
            modelAndView.addObject("pessoas", pessoaIterable);
            modelAndView.addObject("pessoa", new Pessoa());
            modelAndView.addObject("profissoes", profissaoRepository.findAll());

            return modelAndView;
        }
    }

    @GetMapping( "/listar-pessoas")
    public ModelAndView listarPessoas() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
    }

    @GetMapping("/editar-pessoa/{pessoaId}")
    public ModelAndView editarPessoa(@PathVariable("pessoaId") Long pessoaId) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", pessoa.get());
        modelAndView.addObject("profissoes", profissaoRepository.findAll());

        return modelAndView;
    }

    @GetMapping("/excluir-pessoa/{pessoaId}")
    public ModelAndView excluirPessoa(@PathVariable("pessoaId") Long pessoaId) {
        pessoaRepository.deleteById(pessoaId);
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", new Pessoa());
        modelAndView.addObject("pessoas", pessoaRepository.findAll());

        return modelAndView;
    }

    @PostMapping("/buscar-pessoa-nome")
    public ModelAndView buscarPessoaNome(@RequestParam("buscarPessoaNome") String buscarPessoaNome, @RequestParam("buscarPessoaSexo") String buscarPessoaSexo) {
        List<Pessoa> pessoas;

        if (buscarPessoaSexo != null && !buscarPessoaSexo.isEmpty()) {
            pessoas = pessoaRepository.findPessoaByNameAndSexo(buscarPessoaNome, buscarPessoaSexo);

        }else {
            pessoas = pessoaRepository.findPessoaByName(buscarPessoaNome);
        }

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoas", pessoas);
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
    }

    @GetMapping("/buscar-pessoa-nome")
    public void gerarRelatorio(@RequestParam("buscarPessoaNome") String buscarPessoaNome, @RequestParam("buscarPessoaSexo") String buscarPessoaSexo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();

        if (buscarPessoaNome != null && !buscarPessoaNome.isEmpty() && buscarPessoaSexo != null && !buscarPessoaSexo.isEmpty()) {
            pessoas = pessoaRepository.findPessoaByNameAndSexo(buscarPessoaNome, buscarPessoaSexo);

        }else if (buscarPessoaNome != null && !buscarPessoaNome.isEmpty()) {
            pessoas = pessoaRepository.findPessoaByName(buscarPessoaNome);

        }else if (buscarPessoaSexo != null && !buscarPessoaSexo.isEmpty()) {
            pessoas = pessoaRepository.findPessoaBySexo(buscarPessoaSexo);

        }else {
            Iterable<Pessoa> iterable = pessoaRepository.findAll();
            for (Pessoa pessoa : iterable) {
                pessoas.add(pessoa);
            }
        }

        // Chamar o método para gerar o relatório
        byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa", request.getServletContext());

        // Tamanho da resposta
        response.setContentLength(pdf.length);

        // Definir na resposta o tipo de arquivo
        response.setContentType("application/octet-stream");

        // Definir o cabeçalho da resposta
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", "relatorio.pdf");
        response.setHeader(headerKey, headerValue);

        // Finaliza a resposta
        response.getOutputStream().write(pdf);
    }

    @GetMapping("/telefone-pessoa/{pessoaId}")
    public ModelAndView telefonesPessoa(@PathVariable("pessoaId") Long pessoaId) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
        ModelAndView modelAndView = new ModelAndView("cadastro/telefone_pessoa");
        modelAndView.addObject("telefones", telefoneRepository.findTelefoneByPessoa(pessoaId));
        modelAndView.addObject("pessoa", pessoa.get());

        return modelAndView;
    }

    @PostMapping("/adicionar-telefone-pessoa/{pessoaId}")
    public ModelAndView adicionarTelefonePessoa(Telefone telefone, @PathVariable("pessoaId") Long pessoaId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId).get();

        if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {

            ModelAndView modelAndView = new ModelAndView("cadastro/telefone_pessoa");
            modelAndView.addObject("pessoa", pessoa);
            modelAndView.addObject("telefones", telefoneRepository.findTelefoneByPessoa(pessoaId));

            List<String> messageErrors = new ArrayList<>();

            if (telefone.getNumero().isEmpty()) {
                messageErrors.add("O número do telefone deve ser informado!");
            }
            if (telefone.getTipo().isEmpty()) {
                messageErrors.add("O tipo do telefone deve ser informado!");
            }

            modelAndView.addObject("messageErrors", messageErrors);

            return modelAndView;

        } else {
            telefone.setPessoa(pessoa);
            telefoneRepository.save(telefone);
            ModelAndView modelAndView = new ModelAndView("cadastro/telefone_pessoa");
            modelAndView.addObject("pessoa", pessoa);
            modelAndView.addObject("telefones", telefoneRepository.findTelefoneByPessoa(pessoaId));

            return modelAndView;
        }
    }

    @GetMapping("/excluir-telefone/{telefoneId}")
    public ModelAndView excluirTelefone(@PathVariable("telefoneId") Long telefoneId) {
        Pessoa pessoa = telefoneRepository.findById(telefoneId).get().getPessoa();
        telefoneRepository.deleteById(telefoneId);
        ModelAndView modelAndView = new ModelAndView("cadastro/telefone_pessoa");
        modelAndView.addObject("pessoa", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.findTelefoneByPessoa(pessoa.getId()));

        return modelAndView;
    }

    @GetMapping("/baixar-curriculo/{pessoaId}")
    public void baixarCurriculo(@PathVariable("pessoaId") Long pessoaId, HttpServletResponse response) throws IOException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId).get();

        if (pessoa.getCurriculo() != null) {
            // Tamanho da resposta
            response.setContentLength(pessoa.getCurriculo().length);

            // Tipo do arquivo para download, ou pode ser genérica usando application/octet-stream
            response.setContentType(pessoa.getTipoArquivoCurriculo());

            //Definir o cabeçalho da resposta
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", pessoa.getNomeArquivoCurriculo());
            response.setHeader(headerKey, headerValue);

            //Finalizar a resposta passando o arquivo
            response.getOutputStream().write(pessoa.getCurriculo());
        }
    }
}
