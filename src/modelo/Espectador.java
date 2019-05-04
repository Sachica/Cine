/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author kuroy
 */
public class Espectador{
    private String nombre;
    private Long documento;
    private Integer edad;
    private Integer dinero;
    private Pelicula peliculaQueVera;
	
    public Espectador(){
    }

    public Espectador(String nombre, Long documento, Integer edad, Integer dinero) {
        this.nombre = nombre;
        this.documento = documento;
        this.edad = edad;
        this.dinero = dinero;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Integer getDinero() {
        return dinero;
    }

    public void setDinero(Integer dinero) {
        this.dinero = dinero;
    }

    public Pelicula getPeliculaQueVera() {
        return peliculaQueVera;
    }

    public void setPeliculaQueVera(Pelicula peliculaQueVera) {
        this.peliculaQueVera = peliculaQueVera;
    }
    
}
