package com.joaogcm.projeto.spring.mvc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joaogcm.projeto.spring.mvc.entities.Detalhe;

@Repository
@Transactional
public interface DetalheRepository extends CrudRepository<Detalhe, Long> {
	
	@Query("SELECT d FROM Detalhe d WHERE d.usuario.id = ?1")
	public List<Detalhe> detalhes(Long idUsuario);
}