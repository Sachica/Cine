/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.time.LocalDateTime;
import javax.swing.ImageIcon;

/**
 *
 * @author kuroy
 */
public class Pelicula{
    public static Integer idGlobal = -1;
    private Integer id;
    private String titulo;
    private ImageIcon imagen;
    private String descripcion;
    private Fecha fechaEstreno;  
    private Hora duracion;
    private Fecha fechaFin;
    private Hora duracionCopia;
    private Integer categoria;
    private Integer tarifa; 
    private String director;
    private String estado;
	
    public Pelicula(){
        idGlobal++;
        this.id = idGlobal;
    }

    public Pelicula(String titulo, String descripcion, ImageIcon imagen, Fecha fechaEstreno, Hora duracion, Integer categoria, Integer tarifa, String director) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fechaEstreno = fechaEstreno;
        this.duracion = duracion;
        this.duracionCopia = duracion;
        this.categoria = categoria;
        this.tarifa = tarifa;
        this.director = director;
        this.estado = "POR COMENZAR";
        idGlobal++;
        this.id = idGlobal;
        this.obtenerFinalizacion();
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }
    
    public Hora getDuracion() {
        return duracion;
    }

    public void setDuracion(Hora duracion) {
        this.duracion = duracion;
    }

    public Fecha getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(Fecha fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }
    
    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Obtiene la hora de finalizacion de la pelicula a travez de su hora de
     * su hora de estreno y su duracion
     */
    private void obtenerFinalizacion(){
        Integer horaIni = fechaEstreno.getHora().getHour();
        Integer minIni = fechaEstreno.getHora().getMinute();
        Integer secIni = fechaEstreno.getHora().getSecond();
        
        Integer horaFin = 0;
        Integer minFin = 0;
        Integer secFin = 0;
        if(secIni+duracion.getSecond() > 59){
            secFin= secIni+duracion.getSecond() - 60;
            minFin+= 1;
        }else{
            secFin= secIni+duracion.getSecond();
        }
        
        if(minIni+duracion.getMinute() > 59 || minFin+minIni+duracion.getMinute() > 59){
            minFin+= minIni+duracion.getMinute() - 60;
            horaFin+= 1;
        }else{
            minFin+= minIni+duracion.getMinute();
        }
        
        if(horaIni+duracion.getHour() > 23 || horaFin+horaIni+duracion.getHour() > 23){
            horaFin+= horaIni+duracion.getHour() - 24;    
            LocalDateTime l = LocalDateTime.now().plusDays(1);
            fechaFin = new Fecha(l.getYear(), l.getMonthValue(), l.getDayOfMonth(), new Hora(horaFin, minFin, secFin));
        }else{
            horaFin+= horaIni+duracion.getHour();
            fechaFin = new Fecha(fechaEstreno.getAño(), fechaEstreno.getMes(), fechaEstreno.getDia(), new Hora(horaFin, minFin, secFin));
        }
    }
    
    /**
     * Metodo que me retorna si hay persona en la sala de this(Pelicula) si es 
     * asi retorna true, de lo contrario false
     * @param sillas
     * @return 
     */
    public Boolean hayPersonas(Silla sillas[][][]){
        for (int i = 0; i < sillas[this.id].length; i++) {
            for (int j = 0; j < sillas[this.id][i].length; j++) {
                if(!sillas[this.id][i][j].isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Actualiza el estado de la pelicula con la hora recibida, si esta es igual
     * a la hora de estreno cambia el estado a comenzada pero no sin antes comprobar
     * si hay personas en la sala de this, si no hay cambia el estado a cancelada
     * y por ultimo cambia el estado a finalizada si la duracion de dicha pelicula es igual a cero
     * @param f
     * @param sillas 
     */
    public void setEstado(Fecha f, Silla sillas[][][]) {
        if(f.equals(this.getFechaEstreno()) && hayPersonas(sillas)){
            this.duracion.setIncrement(false);
            estado = "INICIADA";
        }
        
        if(f.equals(this.getFechaEstreno()) && !hayPersonas(sillas)){
            estado = "CANCELADA";
        }
        
        if(this.duracion.horaCero()){
            this.duracion.romper = true;
            this.duracion = duracionCopia;
            estado = "FINALIZADA";
        }
    }

    public String getEstado() {
        return estado;
    }
    
    @Override
    public String toString() {
        return "Pelicula{" + "titulo=" + titulo + ", imagen=" + imagen + ", descripcion=" + descripcion + ", fecha=" + fechaEstreno.toString() + ", duracion=" + duracion.toString() + ", categoria=" + categoria + ", tarifa=" + tarifa + ", director=" + director + '}';
    }
}
