package br.com.fpnbr.springbootmvc.repositories;

import br.com.fpnbr.springbootmvc.models.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    @Query("select t from Telefone t where t.pessoa.id = ?1")
    List<Telefone> findTelefoneByPessoa(Long pessoaId);
}
