package com.joaogcm.projeto.spring.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joaogcm.projeto.spring.mvc.entities.Profissao;

@Repository
@Transactional
public interface ProfissaoRepository extends CrudRepository<Profissao, Long> {

}