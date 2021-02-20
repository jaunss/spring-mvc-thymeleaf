package com.joaogcm.projeto.spring.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.joaogcm.projeto.spring.mvc.entities")
@ComponentScan(basePackages = { "com.*" })
@EnableJpaRepositories(basePackages = { "com.joaogcm.projeto.spring.mvc.repositories" })
@EnableTransactionManagement
public class SpringmvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringmvcApplication.class, args);
	}
}