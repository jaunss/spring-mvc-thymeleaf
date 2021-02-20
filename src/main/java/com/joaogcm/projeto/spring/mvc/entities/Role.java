package com.joaogcm.projeto.spring.mvc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_role")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nomeRole;
	
	public Role() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNomeRole() {
		return nomeRole;
	}
	
	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}

	@Override /* ROLE_ADMIN, ROLE_GERENTE, ROLE_SECRETARIO */
	public String getAuthority() {
		return this.nomeRole;
	}
}