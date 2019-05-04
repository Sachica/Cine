/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kuroy
 */
public class Conexion {
    private static Connection cnx = null;
    private static PreparedStatement ps  = null;

    public Conexion() throws SQLException, ClassNotFoundException{
        cnx = obtener();
    }
    
    private Connection obtener() throws SQLException, ClassNotFoundException{
        if(cnx == null){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                cnx = DriverManager.getConnection("jdbc:mysql://localhost/cine", "root", "");
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException c){
                throw new ClassCastException(c.getMessage());
            }
        }
        return cnx;
    }
    
    public ResultSet consulta(String table, String id)throws SQLException{
        ps = cnx.prepareStatement("SELECT * FROM "+table+" WHERE id="+id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs;
        }
        return null;
    }
  
    public Connection getConnection(){
        return cnx;
    }
    
    public static void cerrar(){
        if(cnx!=null){
            try{
                cnx.close();
            }catch(Exception e){}
        }
    }
}
