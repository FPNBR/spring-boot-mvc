package br.com.fpnbr.springbootmvc.controllers;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import br.com.fpnbr.springbootmvc.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/cadastro-pessoa")
    public String inicio() {
        return "cadastro/cadastro_pessoa";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/salvar-pessoa")
    public String salvarPessoa(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
        return "cadastro/cadastro_pessoa";
    }
}
