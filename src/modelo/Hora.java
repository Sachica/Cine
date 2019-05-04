/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.time.LocalDateTime;

/**
 *
 * @author kuroy
 */
public class Hora extends Thread{   
    private Integer hour;
    private Integer minute;
    private Integer second;
    private Boolean increment;
    public Boolean mover;
    public Boolean romper;

    public Hora() {
        LocalDateTime c = LocalDateTime.now();
        this.hour = c.getHour();
        this.minute = c.getMinute();
        this.second = c.getSecond();
        increment = true;
        mover = false;
        romper = false; 
    }

    public Hora(Integer hour, Integer minute, Integer second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        increment = true;
        mover=false;
        romper = false;
    }
    
    /**
     * Incrementa this.second cada vez que es llamado y actualiza la hora
     */
    public void increaseSecond(){
        second++;
        this.updateIncrease();
    }
    
    /**
     * Decrementa this.second cada vez que es llamado y actualiza la hora
     */
    public void decreasedSecond(){
        second--;
        this.updateDecreased();
    }
    
    /**
     * Actualiza el decremento que hubo de this.second respecto a la hora actual
     */
    private void updateDecreased(){
        if(second < 0){
            second = 59;
            minute--;
            if(minute < 0){
                minute = 59;
                hour--;
                if(hour < 0){
                    hour = 23;
                }
            }
        }
    }
    
    /**
     * Actualiza el incremento que hubo de this.second respecto a la hora actual
     */
    private void updateIncrease(){
        if(second > 59){
            second = 0;
            minute++;
            if(minute > 59){
                minute = 0;
                hour++;
                if(hour > 23){
                    hour = 0;
                }
            }
        }
    }
    
    /**
     * Meotod que recibe otra hora y la compara con this para comprobar si this 
     * es mayor a other, de ser asi retorna true de lo contrario false
     * @param other
     * @return 
     */
    public Boolean esMayor(Hora other){
        if(this.hour>other.getHour()){
            return true;
        }else if(this.hour==other.getHour() && this.getMinute()>other.getMinute()){
            return true;
        }else if(this.hour==other.getHour() && this.getMinute()==other.getMinute() && this.getSecond()>other.getSecond()){
            return true;
        }
        return false;
    }
    
    /**
     * Meotod que recibe otra hora y la compara con this para comprobar si this 
     * es menor a other, de ser asi retorna true de lo contrario false
     * @param other
     * @return 
     */
    public Boolean esMenor(Hora other){
        return !esMayor(other) && !equals(other);
    }
    
    /**
     * Meotod que recibe otra hora y la compara con this para comprobar si this 
     * es igual a other, de ser asi retorna true de lo contrario false
     * @param other
     * @return 
     */
    public Boolean equals(Hora other){
        return this.hour==other.getHour() && this.getMinute()==other.getMinute() && this.getSecond()==other.getSecond();
    }
    
    /**
     * Metodo que retorna true si la hora actual es 00:00:00
     * @return 
     */
    public Boolean horaCero(){
        return this.hour == 0 && this.minute == 0 && this.second == 0;
    }

    /**
     * Permite al thread ejecutar el codigo dentro del bloque if que 
     * se en encuentra en el metodo run()
     */
    public void activar(){
        mover = true;
    }
    
    /**
     * Deniega al thread ejecutar el codigo dentro del bloque if que 
     * se en encuentra en el metodo run()
     */
    public void dsactivar(){
        mover = false;
    }
    
    @Override 
    public void run(){
        while(true){
            if(mover){
                if(increment){
                    increaseSecond();
                }else{
                    decreasedSecond();
                }
            }
            if(romper){break;}
            try{
                this.sleep(1000);
                }catch(InterruptedException e){System.out.println("nel");}
            }
        }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Boolean getIncrement() {
        return increment;
    }

    public void setIncrement(Boolean increment) {
        this.increment = increment;
    }
    
    public String toString(){ 
        String hora = this.hour<10 ? "0"+this.hour : ""+this.hour;
        String minuto = this.minute<10 ? "0"+this.minute : ""+this.minute;
        String segundo = this.second<10 ? "0"+this.second : ""+this.second;
        return hora+":"+minuto+":"+segundo;
    }
}
