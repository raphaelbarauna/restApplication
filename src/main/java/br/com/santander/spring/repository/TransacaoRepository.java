package br.com.santander.spring.repository;

import br.com.santander.spring.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {

    Page<Transacao> findAllByDataTransacao(@Param("data") Date data, Pageable pageable);
}
