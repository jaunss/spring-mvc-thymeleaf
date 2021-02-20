package com.joaogcm.projeto.spring.mvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplUsuarioAutenticacaoService implUsuarioAutenticacaoService;

	@Override /* Configura as solicitações de acesso por HTTP */
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() /* Desativa as configurações padrão de memória */
				.authorizeRequests() /* Permite restringir acessos */
				.antMatchers(HttpMethod.GET, "/").permitAll() /* Qualquer usuário acessa a pagina inicial */
				.antMatchers(HttpMethod.GET, "/cadastroUsuario").hasAnyRole("ADMIN").anyRequest().authenticated().and()
				.formLogin().permitAll() /* Permite qualquer usuário */
				.and().logout() /* Mapeia URL de logout e invalida usuário autenticado */
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	@Override /* Cria autenticação do usuário com banco de dados ou em memória */
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implUsuarioAutenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override /* Ignora URLs específicas */
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
}