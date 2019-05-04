/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.Pelicula;

/**
 *
 * @author kuroy
 */
public class PeliculaServicios {
    /**
     * Prepara la consulta con conection para luego insertar la respectiva 
     * pelicula en la base de datos
     * @param conection
     * @param pelicula
     * @throws SQLException 
     */
    public static void insert(Connection conection, Pelicula pelicula) throws SQLException{
        PreparedStatement p = conection.prepareStatement("insert into pelicula (titulo, duracion, categoria, director) VALUES(?,?,?,?)");
        p.setString(1, pelicula.getTitulo());
        p.setString(2, pelicula.getDuracion().toString());
        p.setInt(3, pelicula.getCategoria());
        p.setString(4, pelicula.getDirector());
        p.executeUpdate();
    }
}
