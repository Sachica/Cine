/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kuroy
 */
public class Fecha extends Thread{
    private Integer año;
    private Integer mes;
    private Integer dia;
    private Hora hora;
    
    public Fecha() {
        hora = new Hora();
        LocalDateTime l = LocalDateTime.now();
        this.año = l.getYear();
        this.mes = l.getMonthValue()<10 ? l.getMonthValue() : l.getMonthValue();
        this.dia = l.getDayOfMonth()<10 ? l.getDayOfMonth(): l.getDayOfMonth();
    }
    
    public Fecha(Integer año, Integer mes, Integer dia) {
        this.año = año;
        this.mes = mes;
        this.dia = dia;
        this.hora = new Hora();
    }

    public Fecha(Integer año, Integer mes, Integer dia, Hora hora) {
        this.año = año;
        this.mes = mes;
        this.dia = dia;
        this.hora = hora;
    }

    /**
     * Actualiza la fecha actual en caso de que pase de dia, solo se ejecutara si
     * la hora es igual a 23, minuto igual a 59 y segundo igual a 59
     */
    public void actualizarFecha(){
        if(this.hora.getHour()==23 && this.hora.getMinute()==59 && this.hora.getSecond()==59){
            LocalDateTime l = LocalDateTime.now();
            this.año = l.getYear();
            this.mes = l.getMonthValue()<10 ? l.getMonthValue() : l.getMonthValue();
            this.dia = l.getDayOfMonth()<10 ? l.getDayOfMonth(): l.getDayOfMonth();
        }
    }
    
    /**
     * Metodo que recibe otra fecha y la compara con this para comprobar si this
     * es mayor a other, si es mayor retorna true en caso contrario retorna false
     * @param other
     * @return 
     */
    public Boolean esMayor(Fecha other){
        if(this.año>other.getAño()){
            return true;
        }else if(this.año.equals(other.getAño()) && this.getMes()>other.getMes()){
            return true;
        }else if(this.año.equals(other.getAño()) && this.getMes().equals(other.getMes()) && this.getDia()>other.getDia()){
            return true;
        }else if(this.año.equals(other.getAño()) && this.getMes().equals(other.getMes()) && this.getDia().equals(other.getDia()) && this.hora.esMayor(other.getHora())){
            return true;
        }
        return false;
    }
    
    /**
     * Metodo que recibe otra fecha y la compara con this para comprobar si this 
     * es menor a other, si es asi retorna true en caso contrario retorna false
     * @param other
     * @return 
     */
    public Boolean esMenor(Fecha other){
        return !esMayor(other) && !equals(other);
    }
    
    /**
     * Metodo que recibe otra fecha y la compara con this para comprobar si this 
     * es igual a other, si es asi retorna true en caso contrario retorna false
     * @param other
     * @return 
     */
    public Boolean equals(Fecha other){
        return this.año.equals(other.getAño()) && this.getMes().equals(other.getMes()) && this.getDia().equals(other.getDia()) && this.hora.equals(other.getHora());
    }
    
    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Hora getHora() {
        return hora;
    }

    public void setHora(Hora hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        String diaS = this.dia<10 ? "0"+this.dia : ""+this.dia;
        String mesS = this.mes<10 ? "0"+this.mes : ""+this.mes;
        return diaS+"/"+mesS+"/"+año+" - "+this.hora.toString();
    }

    @Override
    public void run() {
        while(true){
            try {
                actualizarFecha();
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Fecha.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
