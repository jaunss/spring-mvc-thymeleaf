package com.joaogcm.projeto.spring.mvc.repositories;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joaogcm.projeto.spring.mvc.entities.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT p FROM Usuario p WHERE p.nome like %?1%")
	List<Usuario> findUsuarioByName(String nome);

	@Query("SELECT p FROM Usuario p WHERE p.sexo = ?1")
	List<Usuario> findUsuarioBySexo(String sexo);

	@Query("SELECT p FROM Usuario p WHERE p.nome like %?1% AND p.sexo = ?2")
	List<Usuario> findUsuarioByNameAndSexo(String nome, String sexo);

	default Page<Usuario> findUsuarioByNamePage(String nome, Pageable paginacao) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);

		/*
		 * Estamos configurando a pesquisa para consultar por partes do nome no banco de
		 * dados, igual a like com SQL
		 */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		/* Une o objeto com o valor e a configuração para consultar */
		Example<Usuario> example = Example.of(usuario, exampleMatcher);

		Page<Usuario> usuarios = findAll(example, paginacao);

		return usuarios;
	}

	default Page<Usuario> findUsuarioByNomeAndSexoPage(String nome, String sexo, Pageable paginacao) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setSexo(sexo);

		/*
		 * Estamos configurando a pesquisa para consultar por partes do nome no banco de
		 * dados, igual a like com SQL
		 */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		/* Une o objeto com o valor e a configuração para consultar */
		Example<Usuario> example = Example.of(usuario, exampleMatcher);
		Page<Usuario> usuarios = findAll(example, paginacao);

		return usuarios;
	}
}