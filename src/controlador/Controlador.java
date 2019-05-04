/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import modelo.*;
import servicios.Conexion;
import servicios.EspectadorServicios;
import servicios.PeliculaServicios;
import vista.*;

/**
 *
 * @author kuroy
 */
public class Controlador implements ActionListener, MouseListener, Runnable{
    Principal vista;
    ArrayList<Pelicula> peliculas;
    Fecha fechaActual;
    Conexion conexion;
    Boolean iniciarCartelera;
    
    Pelicula peliculaElegida;
    Espectador espectadorActual;
    
    Silla[][][] sillas;
    Integer[] silla;
    
    /**
     * Constructor por defecto, inicializa todos los componentes
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public Controlador() throws SQLException, ClassNotFoundException {
        vista = new Principal();      
        peliculas = new ArrayList<>();
        fechaActual = new Fecha();
        conexion = new Conexion();
        iniciarCartelera = false;
        initComponents();
    }
    
    /**
     * Comienza a correr la hora y manda los listeners a la vista
     */
    private void initComponents(){      
        fechaActual.start();
        fechaActual.getHora().activar();
        fechaActual.getHora().start();

        vista.initListener(this, this);       
        vista.cambiarPanel(vista.vistaRegistro, vista.vistaInicio);
    }
   
    @Override
    public void actionPerformed(ActionEvent ae) {        
        if(ae.getSource() == vista.vistaInicio.btnAdd){
            Pelicula pelicula;
            try{
                pelicula = getPelicula();
                if(!interrumpe(pelicula)){
                    if(!fechaPasada(pelicula)){
                        PeliculaServicios.insert(conexion.getConnection(), pelicula);
                        peliculas.add(pelicula);                        
                        vista.vistaInicio.clear();
                        showMessage(2);
                    }else{
                        Pelicula.idGlobal--;
                        showMessage(8);
                    }
                }else{
                    Pelicula.idGlobal--;
                    showMessage(7);
                }
            }catch(SQLException e){
                showMessage(10);
            }catch(Exception e){
                showMessage(1);
            }
            
            if(peliculas.size()>3){
                vista.vistaInicio.btnFin.setEnabled(true);
            }
        }
        
        if(ae.getSource() == vista.vistaInicio.btnFin){
            initSillas(peliculas.size());
            vista.vistaSala.initSalas(peliculas.size(), 8, 9);
            vista.vistaCartelera.addPeliculas(peliculas.toArray());
            vista.vistaCartelera.threadMover.start();
            iniciarCartelera = true;
            vista.cambiarPanel(vista.vistaInicio, vista.vistaCartelera);
        }
        
        if(ae.getSource() == vista.vistaSala.btnCont){
            try {
                espectadorActual.setPeliculaQueVera(peliculaElegida);
                EspectadorServicios.insert(conexion.getConnection(), espectadorActual, vista.vistaSala.getButtons()[peliculaElegida.getId()][silla[0]][silla[1]].getText());
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            vista.vistaSala.getButtons()[peliculaElegida.getId()][silla[0]][silla[1]].selected();
            sillas[peliculaElegida.getId()][silla[0]][silla[1]].setEspectador(espectadorActual);
            vista.vistaRegistro.clear();
            vista.cambiarPanel(vista.vistaSala, vista.vistaCartelera);
        }
        
        if(ae.getSource() == vista.vistaRegistro.btnRegistro){
            try{
                espectadorActual = getEspectador();
                if(espectadorActual.getEdad()<peliculaElegida.getCategoria()){
                    showMessage(4); 
                }else if(espectadorActual.getDinero()<peliculaElegida.getTarifa()){
                    showMessage(3);
                }else{
                    vista.vistaSala.mostrarSala(peliculaElegida.getId());
                    vista.vistaSala.intermitente(peliculaElegida.getId(), getRandom());
                    vista.cambiarPanel(vista.vistaRegistro, vista.vistaSala);
                } 
                }catch(Exception e){
                    showMessage(1);
                }
        }            
        
        if(ae.getSource() == vista.vistaRegistro.btnVolver){
            vista.vistaRegistro.clear();
            vista.cambiarPanel(vista.vistaRegistro, vista.vistaCartelera);
        }
        
        if(ae.getSource() == vista.vistaSala.btnCamb){
            vista.vistaSala.getButtons()[peliculaElegida.getId()][silla[0]][silla[1]].desactivar();
            vista.vistaSala.intermitente(peliculaElegida.getId(), getRandom());
        }
        
        if(ae.getSource() == vista.vistaSala.btnVolver){
            vista.vistaSala.getButtons()[peliculaElegida.getId()][silla[0]][silla[1]].desactivar();
            vista.cambiarPanel(vista.vistaSala, vista.vistaRegistro);
        }
    }
    
    /**
     * Consulta si la fecha de estreno other ya fue pasada por la actual del sisterma
     * osea fechaActual>p.getFechaEstreno() si es asi returna true de lo contrario false.
     * @param other
     * @return 
     */
    private Boolean fechaPasada(Pelicula other){
        if(fechaActual.esMayor(other.getFechaEstreno())){
            return true;
        }
        return false;
    }
    
    /**
     * Consulta si el horario de estreno de other interrumpe contra otros horarios 
     * de peliculas retorna true si interrumpe de lo contrario false.
     * @param other
     * @return 
     */
    private Boolean interrumpe(Pelicula other){
        for (Pelicula p : peliculas) {
            if(p.getFechaFin().esMayor(other.getFechaEstreno()) && p.getFechaEstreno().esMenor(p.getFechaFin())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene dos numeros(Coordenadas) randoms y los retorna en un arreglo si en dicha
     * coordenada esta vacia la silla, de lo contrario seguira buscando,este metodo es
     * recomendable usarlo con existePuesto(Integer id) ya que puede llegar
     * a quedar en un blucle infinito.
     * @return 
     */
    private Integer[] getRandom(){
        Integer i;
        Integer j;
        do{
            i = (int)(Math.random()*sillas[0].length);
            j = (int)(Math.random()*sillas[0][0].length);
        }while(!sillas[peliculaElegida.getId()][i][j].isEmpty());
        silla = new Integer[]{i, j};
        return silla;
    }
    
    /**
     * Vacia la sala con la id recibida
     * @param id 
     */
    public void vaciar(Integer id){
        vista.vistaSala.clear(id);
        for (Integer i = 0; i < sillas[id].length; i++) {
            for (Integer j = 0; j < sillas[id][i].length; j++) {
                sillas[id][i][j].setEspectador(null);
            }
        }
    }
    
    /**
     * Consulta si en el id(Pelicula) existe puesto disponible retorna true si existe
     * de lo contrario false.
     * @param id
     * @return 
     */
    private Boolean existePuesto(Integer id){
        for(Integer i=0; i<sillas[id].length; i++){
            for(Integer j=0; j<sillas[id][i].length; j++){
                if(sillas[id][i][j].isEmpty()){
                    return true;
                }    
            }
        }
        return false;
    }
    
    /**
     * Obtiene la pelicula a partir de los componentes de la vistaInicio para 
     * luego retonarla
     * @return 
     */
    private Pelicula getPelicula(){
        String titulo = vista.vistaInicio.txtNom.getText();
        String director = vista.vistaInicio.txtDir.getText();
        String descripcion = vista.vistaInicio.txtDes.getText();
        ImageIcon img = vista.vistaInicio.getImagen();
        Integer categoria = Integer.parseInt(vista.vistaInicio.txtCat.getText());
        Integer tarifa = Integer.parseInt(vista.vistaInicio.txtTaf.getText());
        
        String horaDur[] = vista.vistaInicio.getHoraDur();
        String horaIni[] = vista.vistaInicio.getHoraIni();
        String fechaA[] = vista.vistaInicio.getFecha();

        Integer dia = Integer.parseInt(fechaA[0]);
        Integer mes = Integer.parseInt(fechaA[1]);
        Integer año = Integer.parseInt(fechaA[2]);

        Hora horaInicio = new Hora(Integer.parseInt(horaIni[0]), Integer.parseInt(horaIni[1]), Integer.parseInt(horaIni[2]));
        Hora horaDuracion = new Hora(Integer.parseInt(horaDur[0]), Integer.parseInt(horaDur[1]), Integer.parseInt(horaDur[2]));
        horaDuracion.start();
        Fecha fecha = new Fecha(año, mes, dia, horaInicio);
        
        return new Pelicula(titulo, descripcion, img, fecha, horaDuracion, categoria, tarifa, director);
    }
    
    /**
     * Obtiene el espectador a partir de los componentes de la vistaRegistro para
     * luego retornarlo
     * @return 
     */
    private Espectador getEspectador(){
        String nombre = vista.vistaRegistro.txtNom.getText()+" "+vista.vistaRegistro.txtApe.getText();
        Long docum = Long.parseLong(vista.vistaRegistro.txtDoc.getText());
        Integer edad = Integer.parseInt(vista.vistaRegistro.txtEdad.getText());
        Integer tarifa = Integer.parseInt(vista.vistaRegistro.txtDin.getText());
        
        return new Espectador(nombre, docum, edad, tarifa);
    }
    
    /**
     * Inicializa el arreglo tridimensional a partir de x(Numero de peliculas)
     * que sera el numero de capas que tendra el arreglo
     * @param x 
     */
    //Cambio
    public void initSillas(Integer x){
        sillas = new Silla[x][8][9];
        Character letra[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
        Integer numero = 8;
        for(int l=0; l<sillas.length; l++){
            for (int i = sillas[l].length-1; i >= 0 ; i--) {
                for(int j=0; j<sillas[l][i].length; j++){               
                    sillas[l][i][j] = new Silla(null, letra[j], numero);
                }
                numero--;
            }
        }
    }
    
    /**
     * Obtiene la pelicula elegida a traves de obj que es el componente de vistaCartelera
     * que fue clickeado a travez de este pregunta cual fue y me obtiene en el mismo
     * indice del componenteX(JLabel) el tituloX(JLabel) y este se lo manda a 
     * obtenerPeliculaElegida(String titulo) que es encarga para obtenerla para luego
     * almacenarla en la variable peliculaElegida.
     * @param obj 
     */
    private void peliculaElegida(Object obj){
        if(obj.equals(vista.vistaCartelera.lblImg1)){
            peliculaElegida = obtenerPeliculaElegida(vista.vistaCartelera.lblTitle1.getText());
        }
        
        if(obj.equals(vista.vistaCartelera.lblImg2)){
            peliculaElegida = obtenerPeliculaElegida(vista.vistaCartelera.lblTitle2.getText());
        }
        
        if(obj.equals(vista.vistaCartelera.lblImg3)){
            peliculaElegida = obtenerPeliculaElegida(vista.vistaCartelera.lblTitle3.getText());
        }
        
        if(obj.equals(vista.vistaCartelera.lblImg4)){
            peliculaElegida = obtenerPeliculaElegida(vista.vistaCartelera.lblTitle4.getText());
        }
    }
    
    /**
     * Usa el ciclo for each para preguntar si p.getTitulo().equals(titulo) si es cierto retorna p
     * de lo contrario null(Aunque este es un caso omiso ya que si o si va a existir)
     * @param titulo
     * @return 
     */
    private Pelicula obtenerPeliculaElegida(String titulo){
        for(Pelicula p : peliculas){
            if(p.getTitulo().equals(titulo)){
                return p;
            }
        }
        return null;
    }    
    
    @Override
    public void mouseClicked(MouseEvent me) {
        peliculaElegida(me.getSource()); 
        if(existePuesto(peliculaElegida.getId())){
            vista.vistaCartelera.mover = true;   
            vista.vistaCartelera.actualizar();
            if(me.getButton() == MouseEvent.BUTTON1){                  
                if(peliculaElegida.getEstado().equals("FINALIZADA")){
                    showMessage(6);
                }else if(peliculaElegida.getEstado().equals("CANCELADA")){
                    showMessage(9);
                }else{
                    vista.cambiarPanel(vista.vistaCartelera, vista.vistaRegistro);
                }
            }
        }else{
            showMessage(5);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {        
        vista.vistaCartelera.cambiarATexto((javax.swing.JLabel)me.getSource(), Boolean.TRUE);
        vista.vistaCartelera.mover = false;
    }

    @Override
    public void mouseExited(MouseEvent me) {
        vista.vistaCartelera.cambiarATexto((javax.swing.JLabel)me.getSource(), Boolean.FALSE);            
        vista.vistaCartelera.mover = true;   
        vista.vistaCartelera.actualizar();
    }
    
    /**
     * Centro de mensajes 
     * @param i 
     */
    private void showMessage(Integer i){
        switch(i){
            case 1 : JOptionPane.showMessageDialog(vista, "Existen datos mal insertados", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 2 : JOptionPane.showMessageDialog(vista, "Se ha añadido la pelicula", "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
            break;
            case 3 : JOptionPane.showMessageDialog(vista, "Dinero insuficiente para la pelicula", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 4 : JOptionPane.showMessageDialog(vista, "Edad menor a la permitida", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 5 : JOptionPane.showMessageDialog(vista, "La sala esta llena", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 6 : JOptionPane.showMessageDialog(vista, this.peliculaElegida.getTitulo()+" esta finalizada", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 7 : JOptionPane.showMessageDialog(vista, "El horario de esta pelicula cruza con otra ya agregada", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 8 : JOptionPane.showMessageDialog(vista, "El horario de esta pelicula ya paso", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 9 : JOptionPane.showMessageDialog(vista, this.peliculaElegida.getTitulo()+" esta cancelada", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
            case 10 : JOptionPane.showMessageDialog(vista, "Esta pelicula ya existe en la base de datos", "Operacion fallida", JOptionPane.ERROR_MESSAGE);
            break;
        }
    }

    @Override
    public void run() {
        while(true){
            vista.vistaInicio.actualizarHorario(fechaActual.toString());
            if(iniciarCartelera){
                vista.vistaCartelera.actualizarVista(fechaActual, sillas);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Controlador c = new Controlador();               
        Thread t1 = new Thread(c);
        t1.start();
        c.vista.setVisible(true);      
    }   
}
