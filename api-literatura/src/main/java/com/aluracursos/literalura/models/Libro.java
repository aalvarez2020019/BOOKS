package com.aluracursos.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String autor;
    private String titulo;
    private String lenguaje;
    private Integer descargas;

    public Libro(){}

    public Libro(ResultadosDatos datosLibro) {

        // DATOS DEL LIBRO
        this.titulo = datosLibro.titulo();
        this.autor = datosLibro.autorList().get(0).nombreAutor();
        this.lenguaje = datosLibro.language().get(0);
        this.descargas = datosLibro.descargas();
    }

    // GETTERS AND SETTERS
    public Long getId() {
        return id;
    }
    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getDescargas() {
        return descargas;
    }


    public String getTitulo() {
        return titulo;
    }


    public String getAutor() {
        return autor;
    }

    public String getLenguaje() {
        return lenguaje;
    }


}
