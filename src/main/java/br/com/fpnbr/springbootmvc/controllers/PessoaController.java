package br.com.fpnbr.springbootmvc.controllers;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import br.com.fpnbr.springbootmvc.models.Telefone;
import br.com.fpnbr.springbootmvc.repositories.PessoaRepository;
import br.com.fpnbr.springbootmvc.repositories.TelefoneRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @GetMapping( "/cadastro-pessoa")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", new Pessoa());
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);

        return modelAndView;
    }

    @PostMapping("/salvar-pessoa")
    public ModelAndView salvarPessoa(@Valid Pessoa pessoa, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
            Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
            modelAndView.addObject("pessoas", pessoaIterable);
            modelAndView.addObject("pessoa", pessoa);

            List<String> messageErrors = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                messageErrors.add(objectError.getDefaultMessage()); // Vem das annotations @NotEmpty e @NotNull
            }

            modelAndView.addObject("messageErrors", messageErrors);
            return modelAndView;

        }else {
            pessoaRepository.save(pessoa);
            ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
            Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
            modelAndView.addObject("pessoas", pessoaIterable);
            modelAndView.addObject("pessoa", new Pessoa());

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
    public ModelAndView buscarPessoaNome(@RequestParam("buscarPessoaNome") String buscarPessoaNome) {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(buscarPessoaNome));
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
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
        telefone.setPessoa(pessoa);
        telefoneRepository.save(telefone);
        ModelAndView modelAndView = new ModelAndView("cadastro/telefone_pessoa");
        modelAndView.addObject("pessoa", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.findTelefoneByPessoa(pessoaId));

        return modelAndView;
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
}
