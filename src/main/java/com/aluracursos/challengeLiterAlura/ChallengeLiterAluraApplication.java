package com.aluracursos.challengeLiterAlura;

import com.aluracursos.challengeLiterAlura.principal.Principal;

import com.aluracursos.challengeLiterAlura.repository.AutorRepository;
import com.aluracursos.challengeLiterAlura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiterAluraApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiterAluraApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroRepository, autorRepository);
		principal.menuAlUsuario();

	}
}
