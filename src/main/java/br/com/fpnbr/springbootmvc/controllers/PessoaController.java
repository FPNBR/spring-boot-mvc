package br.com.fpnbr.springbootmvc.controllers;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import br.com.fpnbr.springbootmvc.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/cadastro-pessoa")
    public String inicio() {
        return "cadastro/cadastro_pessoa";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/salvar-pessoa")
    public ModelAndView salvarPessoa(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/listar-pessoas")
    public ModelAndView pessoas() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastro_pessoa");
        Iterable<Pessoa> pessoaIterable = pessoaRepository.findAll();
        modelAndView.addObject("pessoas", pessoaIterable);

        return modelAndView;
    }
}
