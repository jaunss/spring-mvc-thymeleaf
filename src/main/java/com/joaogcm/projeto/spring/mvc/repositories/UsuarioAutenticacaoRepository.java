package com.joaogcm.projeto.spring.mvc.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joaogcm.projeto.spring.mvc.entities.UsuarioAutenticacao;

@Repository
@Transactional
public interface UsuarioAutenticacaoRepository extends CrudRepository<UsuarioAutenticacao, Long> {
	
	@Query("SELECT u FROM UsuarioAutenticacao u WHERE u.login = ?1")
	UsuarioAutenticacao findUsuarioByLogin(String login);
}