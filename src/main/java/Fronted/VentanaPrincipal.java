/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Fronted;

import AnalizadorLexico.Lexer;
import Registros.RegistroError;
import Registros.RegistroToken;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dar333n
 */
public class VentanaPrincipal extends javax.swing.JFrame {
        
     private DefaultTableModel modelTokens;
     private DefaultTableModel modelErros;
     private Lexer lexer; 
 
    public VentanaPrincipal() {
        initComponents();
    }
    
    private void mostrarTokens(List<RegistroToken> tokens){
        modelTokens=(DefaultTableModel) TablaTokens.getModel();
        modelTokens.setRowCount(0);

        for(RegistroToken tok: tokens){
            int numeroReg = 0;
            Object[] fila= new Object[]{
                numeroReg++,
                tok.getLexema(),
                tok.getTipo(),
                tok.getFila(),
                tok.getColumna()
            };
            modelTokens.addRow(fila);
        }
    }
    
    private void mostrarErrores(List<RegistroError> errores){
        modelErros=(DefaultTableModel) TablaErrores.getModel();
        modelErros.setRowCount(0);

        for( RegistroError tok: errores){
            int numeroReg = 0;
            Object[] fila= new Object[]{
                numeroReg++,
                tok.getLexema(),
                tok.getDescripcion(),
                tok.getFila(),
                tok.getColumna()
            };
            modelErros.addRow(fila);
        }
    }
    
    
    private void accionGuardarComo() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo como...");

        // Filtro para forzar/sugerir extensión .pz
        javax.swing.filechooser.FileNameExtensionFilter filtro = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos PromptZal (*.pz)", "pz");
        fileChooser.setFileFilter(filtro);

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();

            // Agregar extensión .pz automáticamente si el usuario no la escribió
            if (!archivo.getName().toLowerCase().endsWith(".pz")) {
                archivo = new java.io.File(archivo.getAbsolutePath() + ".pz");
            }

            // Escribir el contenido del JTextArea en el archivo seleccionado
            try (java.io.FileWriter writer = new java.io.FileWriter(archivo)) {
                writer.write(Editor.getText());
                this.setTitle("PromptZal IDE - " + archivo.getName());
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Archivo guardado exitosamente.", 
                    "Guardado", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error al guardar el archivo: " + e.getMessage(), 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void accionAbrir() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo PromptZal");

        // Filtro para mostrar solo archivos .pz
        javax.swing.filechooser.FileNameExtensionFilter filtro = 
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos PromptZal (*.pz)", "pz");
        fileChooser.setFileFilter(filtro);

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = fileChooser.getSelectedFile();
            try {
                // Leer todo el contenido del archivo y pegarlo en el JTextArea
                String contenido = new String(java.nio.file.Files.readAllBytes(archivo.toPath()));
                Editor.setText(contenido);

            } catch (java.io.IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error al abrir el archivo: " + e.getMessage(), 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        BarraBtns = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        EditorTexto = new javax.swing.JScrollPane();
        Editor = new javax.swing.JTextArea();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaTokens = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaErrores = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lexer");

        jPanel1.setLayout(new java.awt.BorderLayout());

        BarraBtns.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setText("Analizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        BarraBtns.add(jButton1);

        jButton2.setText("Generar Imagen del AFD");
        BarraBtns.add(jButton2);

        jPanel1.add(BarraBtns, java.awt.BorderLayout.PAGE_START);

        Editor.setColumns(20);
        Editor.setRows(5);
        EditorTexto.setViewportView(Editor);

        jSplitPane1.setLeftComponent(EditorTexto);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tokens "));

        TablaTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "#", "Lexema", "Tipo", "Fila", "Columna"
            }
        ));
        jScrollPane1.setViewportView(TablaTokens);

        jSplitPane2.setTopComponent(jScrollPane1);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Errores"));

        TablaErrores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "#", "Lexema/Caracter", "Tipo Error", "Fila", "Columna"
            }
        ));
        jScrollPane2.setViewportView(TablaErrores);

        jSplitPane2.setRightComponent(jScrollPane2);

        jSplitPane1.setRightComponent(jSplitPane2);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Abrir...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator1);

        jMenuItem3.setText("Salir");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        accionAbrir();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String codigoFuente = Editor.getText();

        Lexer lexer = new Lexer(codigoFuente);
        lexer.AnalizarFlujo();

        mostrarTokens(lexer.getTokens());
        mostrarErrores(lexer.getErrores());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
         // TODO add your handling code here:
        System.exit(0); 
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        accionGuardarComo();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraBtns;
    private javax.swing.JTextArea Editor;
    private javax.swing.JScrollPane EditorTexto;
    private javax.swing.JTable TablaErrores;
    private javax.swing.JTable TablaTokens;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    // End of variables declaration//GEN-END:variables
}
