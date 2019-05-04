/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import modelo.Fecha;
import modelo.Pelicula;
import modelo.Silla;

/**
 *
 * @author kuroy
 */
public class formCartelera extends javax.swing.JPanel implements Runnable{
    public Boolean mover = true;
    public Thread threadMover;
    private Pelicula peliculas[];
    /**
     * Creates new form formCartelera
     */
    public formCartelera() {
        initComponents();
        threadMover = new Thread(this);
    }

    /**
     * Añade los listeners a sus respectivos componentes en los que lo necesita
     * @param e
     * @param m 
     */
    public void initListener(java.awt.event.ActionListener e, java.awt.event.MouseListener m){
        lblImg1.addMouseListener(m);
        lblImg2.addMouseListener(m);
        lblImg3.addMouseListener(m);
        lblImg4.addMouseListener(m);
    }
    
    /**
     * Añade la peliculas al arreglo de this  del parametro p[]
     * @param p 
     */
    public void addPeliculas(Object p[]){
        initPeliculas(p);
    }
    
    /**
     * Carga las primeras 4 peliculas a la interfaz con sus respectivos datos y 
     * imagenes
     */
    public void cargarPeliculas(){       
        Dimension global = lblImg1.getMaximumSize();
        Icon icon;
        /*
            Para 1
        */     
        icon = new ImageIcon(peliculas[0].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT));
        lblImg1.setText("");
        lblImg1.setIcon(icon);
        lblTitle1.setText(peliculas[0].getTitulo());
        lblHora1.setText(peliculas[0].getFechaEstreno().toString());
        lblPrecio1.setText("$ "+peliculas[0].getTarifa());
        /*
            Para 2
        */
        icon = new ImageIcon(peliculas[1].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT));
        lblImg2.setText("");
        lblImg2.setIcon(icon);
        lblTitle2.setText(peliculas[1].getTitulo());
        lblHora2.setText(peliculas[1].getFechaEstreno().toString());
        lblPrecio2.setText("$ "+peliculas[1].getTarifa());
        /*
            Para 3
        */
        icon = new ImageIcon(peliculas[2].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT));
        lblImg3.setText("");
        lblImg3.setIcon(icon);
        lblTitle3.setText(peliculas[2].getTitulo());
        lblHora3.setText(peliculas[2].getFechaEstreno().toString());
        lblPrecio3.setText("$ "+peliculas[2].getTarifa());
        /*
            Para 4
        */
        icon = new ImageIcon(peliculas[3].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT));
        lblImg4.setText("");
        lblImg4.setIcon(icon);
        lblTitle4.setText(peliculas[3].getTitulo());
        lblHora4.setText(peliculas[3].getFechaEstreno().toString());    
        lblPrecio4.setText("$ "+peliculas[3].getTarifa());
    }
    
    /**
     * Actualiza las imagenes de la interfaz
     */
    public void actualizar(){
        Dimension global = lblImg1.getMaximumSize();
        lblImg1.setIcon(new ImageIcon(peliculas[0].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT)));
        lblImg2.setIcon(new ImageIcon(peliculas[1].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT)));
        lblImg3.setIcon(new ImageIcon(peliculas[2].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT)));
        lblImg4.setIcon(new ImageIcon(peliculas[3].getImagen().getImage().getScaledInstance((int)global.getWidth(), (int)global.getHeight(), Image.SCALE_DEFAULT)));
    } 
    
    /**
     * Actualiza el estado de las peliculas en la interfaz
     */
    private void actualizarEstados(){
        lblEst1.setText(peliculas[0].getEstado());
        lblEst2.setText(peliculas[1].getEstado());
        lblEst3.setText(peliculas[2].getEstado());
        lblEst4.setText(peliculas[3].getEstado());
    }
    
    /**
     * Actualiza la fecha de la interfaz y comienza la restar la duracion de la pelicula 
     * si esta tiene estado de INICIADA o deja de restar la duracion de la 
     * pelicula si esta tiene de estado FINALIZADA
     * @param fecha
     * @param sillas 
     */
    public void actualizarVista(Fecha fecha, Silla sillas[][][]){
        lblHorario.setText(fecha.toString());
        
        for(Integer i=0; i<peliculas.length; i++){
            peliculas[i].setEstado(fecha, sillas);
            if(peliculas[i].getEstado().equals("INICIADA")){
                peliculas[i].getDuracion().mover = true;
            }
            
            if(peliculas[i].getEstado().equals("FINALIZADA")){
                peliculas[i].getDuracion().mover = false;
            }
        }
        this.actualizarEstados();
    }
    
    /**
     * Hace el casteo respectivo para inicializar peliculas de this
     * @param o 
     */
    private void initPeliculas(Object o[]){
        peliculas = new Pelicula[o.length];
        for (int i = 0; i < o.length; i++) {
            peliculas[i] = (Pelicula)o[i];
        }
    }
    
    /**
     * Mueve las peliculas haciendo que la primera pase de ultimo, yb la ultima pase
     * de primera generando asi dicho movimiento
     */
    private void moverPeliculas(){
        Pelicula tmp[] = new Pelicula[peliculas.length];
        Pelicula p = peliculas[0];
        for (int i = 1, k=0; i < peliculas.length; i++, k++) {
           tmp[k] = peliculas[i];
        }
        tmp[tmp.length-1] = p;
        
        peliculas = tmp;
    }
    
    /**
     * Cambia el label de la imagen a la descripcion de dicha pelicula
     * @param label
     * @param comand 
     */
    public void cambiarATexto(JLabel label, Boolean comand){
        Icon icon = null;
        if(label.equals(lblImg1)){
            if(comand){
                icon = lblImg1.getIcon();
                lblImg1.setIcon(null);
                lblImg1.setText("<html>"+peliculas[0].getDescripcion()+"<html>");
            }else{
                lblImg1.setText("");
                lblImg1.setIcon(icon);
            }
        }
        
        if(label.equals(lblImg2)){
            if(comand){
                icon = lblImg2.getIcon();
                lblImg2.setIcon(null);
                lblImg2.setText("<html>"+peliculas[1].getDescripcion()+"<html>");
            }else{
                lblImg2.setText("");
                lblImg2.setIcon(icon);
            }
        }
        
        if(label.equals(lblImg3)){
            if(comand){
                icon = lblImg3.getIcon();
                lblImg3.setIcon(null);
                lblImg3.setText("<html>"+peliculas[2].getDescripcion()+"<html>");
            }else{
                lblImg3.setText("");
                lblImg3.setIcon(icon);
            }
        }
        
        if(label.equals(lblImg4)){
            if(comand){
                icon = lblImg4.getIcon();
                lblImg4.setIcon(null);
                lblImg4.setText("<html>"+peliculas[3].getDescripcion()+"<html>");
            }else{
                lblImg4.setText("");
                lblImg4.setIcon(icon);
            }
        }
    }
    
    @Override
    public void run() {
        while(true){
            if(mover){
                moverPeliculas();
                cargarPeliculas();
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                mover = false;
            }
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImg1 = new javax.swing.JLabel();
        lblImg3 = new javax.swing.JLabel();
        lblImg2 = new javax.swing.JLabel();
        lblImg4 = new javax.swing.JLabel();
        lblTitle1 = new javax.swing.JLabel();
        lblHora1 = new javax.swing.JLabel();
        lblTitle2 = new javax.swing.JLabel();
        lblHora2 = new javax.swing.JLabel();
        lblHora3 = new javax.swing.JLabel();
        lblTitle3 = new javax.swing.JLabel();
        lblHora4 = new javax.swing.JLabel();
        lblTitle4 = new javax.swing.JLabel();
        lblEst1 = new javax.swing.JLabel();
        lblEst2 = new javax.swing.JLabel();
        lblEst3 = new javax.swing.JLabel();
        lblEst4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblHorario = new javax.swing.JLabel();
        lblPrecio1 = new javax.swing.JLabel();
        lblPrecio2 = new javax.swing.JLabel();
        lblPrecio3 = new javax.swing.JLabel();
        lblPrecio4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Funciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1114, 360));
        setMinimumSize(new java.awt.Dimension(1086, 301));

        lblImg1.setForeground(new java.awt.Color(255, 255, 255));
        lblImg1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImg1.setText("Imagen");
        lblImg1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImg1.setMaximumSize(new java.awt.Dimension(255, 206));

        lblImg3.setForeground(new java.awt.Color(255, 255, 255));
        lblImg3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImg3.setText("Imagen");
        lblImg3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImg3.setMaximumSize(new java.awt.Dimension(255, 206));

        lblImg2.setForeground(new java.awt.Color(255, 255, 255));
        lblImg2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImg2.setText("Imagen");
        lblImg2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImg2.setMaximumSize(new java.awt.Dimension(255, 206));

        lblImg4.setForeground(new java.awt.Color(255, 255, 255));
        lblImg4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImg4.setText("Imagen");
        lblImg4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblImg4.setMaximumSize(new java.awt.Dimension(255, 206));
        lblImg4.setName(""); // NOI18N

        lblTitle1.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle1.setText("Titulo");

        lblHora1.setForeground(new java.awt.Color(255, 255, 255));
        lblHora1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora1.setText("Hora");

        lblTitle2.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle2.setText("Titulo");

        lblHora2.setForeground(new java.awt.Color(255, 255, 255));
        lblHora2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora2.setText("Hora");

        lblHora3.setForeground(new java.awt.Color(255, 255, 255));
        lblHora3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora3.setText("Hora");

        lblTitle3.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle3.setText("Titulo");

        lblHora4.setForeground(new java.awt.Color(255, 255, 255));
        lblHora4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHora4.setText("Hora");

        lblTitle4.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle4.setText("Titulo");

        lblEst1.setForeground(new java.awt.Color(255, 255, 255));
        lblEst1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEst1.setText("Estado");

        lblEst2.setForeground(new java.awt.Color(255, 255, 255));
        lblEst2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEst2.setText("Estado");

        lblEst3.setForeground(new java.awt.Color(255, 255, 255));
        lblEst3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEst3.setText("Estado");

        lblEst4.setForeground(new java.awt.Color(255, 255, 255));
        lblEst4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEst4.setText("Estado");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Fecha");

        lblHorario.setForeground(new java.awt.Color(255, 255, 255));
        lblHorario.setText("DD:MM:YYYY  HH:MM:SS");

        lblPrecio1.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio1.setText("Tarifa");

        lblPrecio2.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio2.setText("Tarifa");

        lblPrecio3.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio3.setText("Tarifa");

        lblPrecio4.setForeground(new java.awt.Color(255, 255, 255));
        lblPrecio4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio4.setText("Tarifa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(lblHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblHora1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTitle1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblImg1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEst1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrecio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblHora2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTitle2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEst2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblImg2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrecio2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblTitle3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHora3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEst3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblImg3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrecio3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTitle4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHora4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEst4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblImg4, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrecio4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblImg1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHora1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEst1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrecio1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblImg2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblImg4, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblImg3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHora2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEst2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrecio2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitle3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHora3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEst3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrecio3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitle4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHora4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEst4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPrecio4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    public javax.swing.JLabel lblEst1;
    public javax.swing.JLabel lblEst2;
    public javax.swing.JLabel lblEst3;
    public javax.swing.JLabel lblEst4;
    public javax.swing.JLabel lblHora1;
    public javax.swing.JLabel lblHora2;
    public javax.swing.JLabel lblHora3;
    public javax.swing.JLabel lblHora4;
    public javax.swing.JLabel lblHorario;
    public javax.swing.JLabel lblImg1;
    public javax.swing.JLabel lblImg2;
    public javax.swing.JLabel lblImg3;
    public javax.swing.JLabel lblImg4;
    public javax.swing.JLabel lblPrecio1;
    public javax.swing.JLabel lblPrecio2;
    public javax.swing.JLabel lblPrecio3;
    public javax.swing.JLabel lblPrecio4;
    public javax.swing.JLabel lblTitle1;
    public javax.swing.JLabel lblTitle2;
    public javax.swing.JLabel lblTitle3;
    public javax.swing.JLabel lblTitle4;
    // End of variables declaration//GEN-END:variables
   
}
