package com.joaogcm.projeto.spring.mvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joaogcm.projeto.spring.mvc.entities.UsuarioAutenticacao;
import com.joaogcm.projeto.spring.mvc.repositories.UsuarioAutenticacaoRepository;

@Service
@Transactional
public class ImplUsuarioAutenticacaoService implements UserDetailsService {

	@Autowired
	private UsuarioAutenticacaoRepository usuarioAutenticacaoRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UsuarioAutenticacao usuarioAutenticacao = usuarioAutenticacaoRepository.findUsuarioByLogin(username);

		if (usuarioAutenticacao == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		return new User(usuarioAutenticacao.getLogin(), usuarioAutenticacao.getSenha(), usuarioAutenticacao.isEnabled(),
				true, true, true, usuarioAutenticacao.getAuthorities());
	}
}