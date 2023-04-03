package br.com.fpnbr.springbootmvc.repositories;

import br.com.fpnbr.springbootmvc.models.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {
}
