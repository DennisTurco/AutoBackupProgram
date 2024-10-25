package com.mycompany.autobackupprogram;

import com.formdev.flatlaf.FlatIntelliJLaf;
import static com.mycompany.autobackupprogram.BackupManagerGUI.OpenExceptionMessage;
import java.awt.Image;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TimePicker extends javax.swing.JDialog {

    private TimeInterval timeInterval;
    private boolean closeOk;
    
    public TimePicker(java.awt.Frame parent, TimeInterval timeInterval, boolean modal) {
        super(parent, modal);
        
        closeOk = false;

        initComponents();
        
        if (timeInterval != null) {
            daysSpinner.setValue(timeInterval.getDays());
            hoursSpinner.setValue(timeInterval.getHours());
            minutesSpinner.setValue(timeInterval.getMinutes());
        }
        
        // logo application
        Image icon = new ImageIcon(this.getClass().getResource(ConfigKey.LOGO_IMG.getValue())).getImage();
        this.setIconImage(icon); 
    }
    
    public TimeInterval getTimeInterval() {
        if (closeOk) return timeInterval;
        return null;
    }        
    
    private void daysIntervalSpinnerChange() {        
        Integer days = (Integer) daysSpinner.getValue();
        
        if (days == null || days < 0) {
            daysSpinner.setValue(0);
        }         
    }
    
    private void hoursIntervalSpinnerChange() {        
        Integer hours = (Integer) hoursSpinner.getValue();
        
        if (hours == null || hours < 0) {
            hoursSpinner.setValue(0);
        } else if (hours > 23) {
            hoursSpinner.setValue(23);
        }       
    }
    
    private void minutesIntervalSpinnerChange() {        
        Integer minutes = (Integer) minutesSpinner.getValue();
        
        if (minutes == null || minutes < 0) {
            minutesSpinner.setValue(0);
        }  else if (minutes > 59) {
            minutesSpinner.setValue(59);
        } 
    }
    
    private boolean checkInputCorrectness() {
        Integer days = (Integer) daysSpinner.getValue();
        Integer hours = (Integer) hoursSpinner.getValue();
        Integer minutes = (Integer) minutesSpinner.getValue();
        return (days != null && days >= 0 && hours != null && hours >= 0 && hours <= 23 && minutes != null && minutes >= 0 && minutes <= 59 && (days != 0 || hours != 0 || minutes != 0));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        daysSpinner = new javax.swing.JSpinner();
        hoursSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        minutesSpinner = new javax.swing.JSpinner();
        btnOk = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Time interval for auto backup");
        setResizable(false);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(2);
        jTextArea1.setText("Select how often to perform the automatic backup by \nchoosing the frequency in days, hours, and minutes.");
        jTextArea1.setBorder(null);
        jTextArea1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTextArea1.setFocusable(false);
        jTextArea1.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("Days");

        daysSpinner.setToolTipText("Mouse wheel to adjust the value");
        daysSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                daysSpinnerStateChanged(evt);
            }
        });
        daysSpinner.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                daysSpinnerMouseWheelMoved(evt);
            }
        });

        hoursSpinner.setToolTipText("Mouse wheel to adjust the value");
        hoursSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                hoursSpinnerStateChanged(evt);
            }
        });
        hoursSpinner.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                hoursSpinnerMouseWheelMoved(evt);
            }
        });

        jLabel2.setText("Hours");

        jLabel3.setText("Minutes");

        minutesSpinner.setToolTipText("Mouse wheel to adjust the value");
        minutesSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minutesSpinnerStateChanged(evt);
            }
        });
        minutesSpinner.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                minutesSpinnerMouseWheelMoved(evt);
            }
        });

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 95, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(daysSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(hoursSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(minutesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(100, 100, 100))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(daysSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hoursSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minutesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void daysSpinnerMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_daysSpinnerMouseWheelMoved
        mouseWeel(evt);
    }//GEN-LAST:event_daysSpinnerMouseWheelMoved

    private void hoursSpinnerMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_hoursSpinnerMouseWheelMoved
        mouseWeel(evt);
    }//GEN-LAST:event_hoursSpinnerMouseWheelMoved

    private void minutesSpinnerMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_minutesSpinnerMouseWheelMoved
        mouseWeel(evt);
    }//GEN-LAST:event_minutesSpinnerMouseWheelMoved

    private void mouseWeel(java.awt.event.MouseWheelEvent evt) {
        javax.swing.JSpinner spinner = (javax.swing.JSpinner) evt.getSource();
        int rotation = evt.getWheelRotation();

        if (rotation < 0) {
            spinner.setValue((Integer) spinner.getValue() + 1);
        } else {
            spinner.setValue((Integer) spinner.getValue() - 1);
        }
    }
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (checkInputCorrectness()) {
            timeInterval = new TimeInterval((int)daysSpinner.getValue(), (int)hoursSpinner.getValue(), (int)minutesSpinner.getValue());
            closeOk = true;
            this.dispose();
        }
        else {
            JOptionPane.showMessageDialog(null, "The time interval is not correct", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        closeOk = false;
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void daysSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_daysSpinnerStateChanged
        daysIntervalSpinnerChange();
    }//GEN-LAST:event_daysSpinnerStateChanged

    private void hoursSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_hoursSpinnerStateChanged
        hoursIntervalSpinnerChange();
    }//GEN-LAST:event_hoursSpinnerStateChanged

    private void minutesSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minutesSpinnerStateChanged
        minutesIntervalSpinnerChange();
    }//GEN-LAST:event_minutesSpinnerStateChanged

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Exception (main) --> " + ex);
            OpenExceptionMessage(ex.getMessage(), Arrays.toString(ex.getStackTrace()));
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TimePicker dialog = new TimePicker(new javax.swing.JFrame(), null, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JSpinner daysSpinner;
    private javax.swing.JSpinner hoursSpinner;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JSpinner minutesSpinner;
    // End of variables declaration//GEN-END:variables
}
