package br.com.fpnbr.springbootmvc.repositories;

import br.com.fpnbr.springbootmvc.models.Pessoa;
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
}
