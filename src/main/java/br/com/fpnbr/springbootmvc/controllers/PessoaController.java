package br.com.fpnbr.springbootmvc.controllers;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import br.com.fpnbr.springbootmvc.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping( "/cadastro-pessoa")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
    }

    @PostMapping("/salvar-pessoa")
    public ModelAndView salvarPessoa(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
    }

    @GetMapping( "/listar-pessoas")
    public ModelAndView listarPessoas() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);
        modelAndView.addObject("pessoa", new Pessoa());

        return modelAndView;
    }

    @GetMapping("/editar-pessoa/{idPessoa}")
    public ModelAndView editarPessoa(@PathVariable("idPessoa") Long idPessoa) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        modelAndView.addObject("pessoa", pessoa.get());

        return modelAndView;
    }

    @GetMapping("/excluir-pessoa/{idPessoa}")
    public ModelAndView excluirPessoa(@PathVariable("idPessoa") Long idPessoa) {
        pessoaRepository.deleteById(idPessoa);
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
}
