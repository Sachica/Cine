/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.Color;
import javax.swing.JButton;

/**
 *
 * @author kuroy
 */
public class Button extends JButton implements Runnable{
    private Boolean activado=false;
    private Boolean cambiar=false;
    private static final Color WHITE = Color.WHITE;
    private static final Color RED = Color.RED;
    private static final Color BLUE = Color.BLUE;
    
    public Button(String string) {
        super(string);
        super.setFocusable(false);
        super.setBackground(WHITE);
        
    }
    
    /**
     * Permite al thread ejecutar el codigo dentro del bloque if en el metodo
     * run() para lograr hacer un boton intermitente
     */
    public void activar(){
        activado = true;
    }
    
    /**
     * Selecciona un button, esto es dejar de ser intermitente this y cambiar 
     * su color de fondo y su color de letra
     */
    public void selected(){
        activado = false;
        super.setBackground(BLUE);
        super.setForeground(WHITE);
    }
    
    /**
     * Deniega al thread ejecutar el codigo dentro del bloque if en el metodo
     * run() para lograr hacer un boton intermitente
     */
    public void desactivar(){
        activado = false;
        changeWhite();
        super.setForeground(Color.BLACK);
    }
    
    public void changeRed(){
        super.setBackground(RED);
    }
    
    public void changeWhite(){
        super.setBackground(WHITE);
    }
    
    /**
     * Cambia de color el fondo de this dependiendo de la boolean "cambiar"
     */
    private void changeColor(){
        if(cambiar){
            changeWhite();
        }else{
            changeRed();
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(activado){
                changeColor();
                cambiar = !cambiar;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }
    
}
