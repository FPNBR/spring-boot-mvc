package br.com.fpnbr.springbootmvc.repositories;

import br.com.fpnbr.springbootmvc.models.Pessoa;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    @Query("select p from Pessoa p where lower(concat(p.nome, ' ', p.sobrenome)) like lower(concat('%', ?1, '%'))")
    List<Pessoa> findPessoaByName(String nome);

    @Query("select p from Pessoa p where p.sexopessoa = ?1")
    List<Pessoa> findPessoaBySexo(String sexo);

    @Query("select p from Pessoa p where lower(concat(p.nome, ' ', p.sobrenome)) like lower(concat('%', ?1, '%')) and p.sexopessoa = ?2")
    List<Pessoa> findPessoaByNameAndSexo(String nome, String sexo);

    default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);

        // Configurando a pesquisa para consultar por partes do nome no banco, tipo o like
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        // Une o objeto com o valor e a configuração para consultar
        Example<Pessoa> pessoaExample = Example.of(pessoa, exampleMatcher);

        Page<Pessoa> pessoas = findAll(pessoaExample, pageable);

        return pessoas;
    }
    default Page<Pessoa> findPessoaBySexoPage(String sexo, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setSexopessoa(sexo);

        // Configurando a pesquisa para consultar por partes do nome no banco, tipo o like
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        // Une o objeto com o valor e a configuração para consultar
        Example<Pessoa> pessoaExample = Example.of(pessoa, exampleMatcher);

        Page<Pessoa> pessoas = findAll(pessoaExample, pageable);

        return pessoas;
    }

    default Page<Pessoa> findPessoaByNameAndSexoPage(String nome, String sexo, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSexopessoa(sexo);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Pessoa> pessoaExample = Example.of(pessoa, exampleMatcher);

        Page<Pessoa> pessoas = findAll(pessoaExample, pageable);

        return pessoas;
    }
}
