/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.Espectador;

/**
 *
 * @author kuroy
 */
public class EspectadorServicios {
    /**
     * Prepara la consulta con conection para luego inserta el espectador en la 
     * base de datos con su respectiva silla
     * @param conection
     * @param espectador
     * @param silla
     * @throws SQLException 
     */
    public static void insert(Connection conection, Espectador espectador, String silla) throws SQLException{
        PreparedStatement p = conection.prepareStatement("insert into espectador (nombre, edad, dinero, peliculavista, sillaOcupada) VALUES(?,?,?,?,?)");
        p.setString(1, espectador.getNombre());
        p.setInt(2, espectador.getEdad());
        p.setInt(3, espectador.getDinero());
        p.setString(4, espectador.getPeliculaQueVera().getTitulo());
        p.setString(5, silla);
        p.executeUpdate();
    }
}
