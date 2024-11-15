package com.mycompany.autobackupprogram.GUI;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.mycompany.autobackupprogram.BackupOperations;
import com.mycompany.autobackupprogram.Enums.ConfigKey;

import static com.mycompany.autobackupprogram.GUI.BackupManagerGUI.OpenExceptionMessage;

import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class BackupProgressGUI extends javax.swing.JFrame {

    private BackupProgressGUI() {
        initComponents();
        
        // logo application
        Image icon = new ImageIcon(this.getClass().getResource(ConfigKey.LOGO_IMG.getValue())).getImage();
        this.setIconImage(icon);
        
        this.setAlwaysOnTop(false);

        setTranslations();
    }
    
    public BackupProgressGUI(String initialPath, String destinationPath) {
        initComponents();
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // logo application
        Image icon = new ImageIcon(this.getClass().getResource(ConfigKey.LOGO_IMG.getValue())).getImage();
        this.setIconImage(icon);
        
        initialPathLabel.setText(initialPath);
        destinationPathLabel.setText(destinationPath);
        
        closeButton.setEnabled(false);
   }
    
    public void UpdateProgressBar(int value) {
        progressBar.setValue(value);
        percentageLabel.setText(value + " %");
        
        if (value == 100) {
            loadingMessageLabel.setText("backup completed!");
            closeButton.setEnabled(true);
            CancelButton.setEnabled(false);
            this.setAlwaysOnTop(true);
        } 
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        loadingMessageLabel = new javax.swing.JLabel();
        percentageLabel = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        initialPathLabel = new javax.swing.JLabel();
        destinationPathLabel = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Backup in progress");
        setMaximumSize(new java.awt.Dimension(430, 175));
        setMinimumSize(new java.awt.Dimension(430, 175));
        setResizable(false);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        loadingMessageLabel.setText("loading...");

        percentageLabel.setText("100%");

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        initialPathLabel.setText("path1");
        initialPathLabel.setMaximumSize(new java.awt.Dimension(415, 16));
        initialPathLabel.setMinimumSize(new java.awt.Dimension(415, 16));
        initialPathLabel.setPreferredSize(new java.awt.Dimension(415, 16));

        destinationPathLabel.setText("path2");
        destinationPathLabel.setMaximumSize(new java.awt.Dimension(415, 16));
        destinationPathLabel.setMinimumSize(new java.awt.Dimension(415, 16));
        destinationPathLabel.setPreferredSize(new java.awt.Dimension(415, 16));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 260, Short.MAX_VALUE)
                        .addComponent(CancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(loadingMessageLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(percentageLabel))
                            .addComponent(initialPathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(destinationPathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(initialPathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(destinationPathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(loadingMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(percentageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeButton)
                    .addComponent(CancelButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop this backup?", "Confimation required", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            BackupOperations.StopCopyFiles();
            this.dispose();
        }
    }//GEN-LAST:event_CancelButtonActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }

        java.awt.EventQueue.invokeLater(() -> {
            new BackupProgressGUI().setVisible(true);
        });
    }

    private void setTranslations() {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel destinationPathLabel;
    private javax.swing.JLabel initialPathLabel;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel loadingMessageLabel;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
