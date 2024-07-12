package com.aluracursos.literalura;

import com.aluracursos.literalura.main.Main;
import com.aluracursos.literalura.repository.RepositorioAutor;
import com.aluracursos.literalura.repository.RepositorioLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class aplicacionLiteratura implements CommandLineRunner
{

	// iniciar aplicacion
	public static void main(String[] args)
	{
		SpringApplication.run(aplicacionLiteratura.class, args);
	}

	@Autowired
	private RepositorioLibro repository;
	@Autowired
	private RepositorioAutor authorRepository;
	public void run(String[] args) {
		Main principal = new Main(repository,authorRepository);
		principal.mostrarMenu();
	}
}
