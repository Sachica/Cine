/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import modelo.*;
import servicios.*;

/**
 *
 * @author kuroy
 */
public class Test {
    
    public static Silla[][][] getSillas(){
        Silla sillas[][][] = new Silla[4][8][9];
        Integer k = 0;
        while(k<4){
            sillas[k] = llenarSala(sillas[k]);
            Integer i[] = getRandom();
            sillas[k][i[0]][i[1]].setEspectador(null);
            k++;
        }
        return sillas;
    }
    
    private static Silla[][] llenarSala(Silla sillas[][]){
        Espectador espectador = new Espectador(" - ", new Long(12345678), 18, 20000);
        for (Integer i = 0; i < sillas.length; i++) {
            for (Integer j = 0; j < sillas[i].length; j++) {  
                sillas[i][j] = new Silla(espectador, 'i', i);
            }
        }       
        return sillas;
    }
    
    private static Integer[] getRandom(){
        return new Integer[]{(int)(Math.random()*8), (int)(Math.random()*9)};
    }
    
    public static Pelicula[] crearPeliculas(Connection conexion) throws SQLException{
        Pelicula peliculas[] = new Pelicula[4];
        
        /*
            Titulos y sus urls respectivas de las peliculas(Se pueden añadir mas si desea)
        */
        String titulos[] = {"John Wick", "Spiderman", "Avengers", "La Llorona"};
        String URLImagenes[] = {"Imagenes/john.jpg",
                                "Imagenes/spiderman.jpg", 
                                "Imagenes/avengers.jpg", 
                                "Imagenes/llorona.jpg"};
        /*
            Fecha con hora de estreno(Preferible una hora mayor a la actual para el simulacro)
        */
        Integer hora = 6;
        Integer minuto = 50;
        Integer segundo = 0;
        Integer dia = 23;
        Integer mes = 4;
        Integer año = 2019;
        
        /*
            Creacion de peliculas
        */
        Fecha fechaEstreno;
        Hora horaDuraciones[] = {new Hora(0, 0, 10), new Hora(0, 0, 10), new Hora(0, 0, 10), new Hora(0, 0, 10)};
        
        for (Integer i = 0;  i < peliculas.length; i++) {
            fechaEstreno =  new Fecha(año, mes, dia, new Hora(hora, minuto, segundo));
            horaDuraciones[i].start();
            peliculas[i] = new Pelicula(titulos[i], "Descripcion: "+titulos[i], new ImageIcon(URLImagenes[i]), fechaEstreno, horaDuraciones[i], 18, 20000, "DIRECTOR: "+i);
            PeliculaServicios.insert(conexion, peliculas[i]);
            segundo+=10;
        }
        
        return peliculas;
    }
    
    public static ArrayList<Pelicula> toArray(Pelicula p[]){
        ArrayList<Pelicula> peliculas = new ArrayList<>();
        for(Integer i=0; i<p.length; i++){
            peliculas.add(p[i]);
        }
        return peliculas;
    }
    
    /*
        Simulacro(Ejecutar si ya realizo los cambios correspondientes en Test.crearPeliculas())
    */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Controlador controlador = new Controlador();
        Silla sillas[][][] = Test.getSillas();
        Pelicula peliculas[] = Test.crearPeliculas(controlador.conexion.getConnection());
        controlador.peliculas = Test.toArray(peliculas);
        controlador.initSillas(peliculas.length);
        controlador.sillas = sillas;
        controlador.vista.vistaCartelera.addPeliculas(peliculas); 
        controlador.vista.vistaSala.initSalas(peliculas.length, 8, 9);
        controlador.vista.vistaSala.llenarSalas(sillas);
        controlador.vaciar(3);
        controlador.vista.vistaCartelera.threadMover.start();
        controlador.iniciarCartelera = true;
        controlador.vista.cambiarPanel(controlador.vista.vistaInicio, controlador.vista.vistaCartelera);
        
        Thread t = new Thread(controlador);
        t.start();
        controlador.vista.setVisible(true);
    }
}
