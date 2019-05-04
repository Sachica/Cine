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
public class Silla{
    private Espectador espectador;
    private Character columna;
    private Integer fila;
	
    public Silla(){
    }

    public Silla(Espectador espectador, Character columna, Integer fila) {
        this.espectador = espectador;
        this.columna = columna;
        this.fila = fila;
    }
    
    /**
     * Averigua si this(Silla) esta vacia osea si su espectador es igual a null
     * si es asi retorna true de lo contrario false
     * @return 
     */
    public Boolean isEmpty(){
	return this.espectador==null;
    }
	
    /**
     * Quita el espectador de la silla cambiando el actual a null
     */
    public void clear(){
	this.espectador = null;
    }

    public Espectador getEspectador() {
        return espectador;
    }

    public void setEspectador(Espectador espectador) {
        this.espectador = espectador;
    }

    public Character getColumna() {
        return columna;
    }

    public void setColumna(Character columna) {
        this.columna = columna;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }
    
}
