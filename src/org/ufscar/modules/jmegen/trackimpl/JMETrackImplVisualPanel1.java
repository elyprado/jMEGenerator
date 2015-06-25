/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ufscar.modules.jmegen.trackimpl;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.ufscar.modules.jmegen.data.FilePathObject;
import org.ufscar.modules.jmegen.data.Track;
import org.ufscar.modules.jmegen.data.TrackPart;

public final class JMETrackImplVisualPanel1 extends JPanel {
    static final String CONTENT = "content";
    static final String FILENAME = "fileName";
    
    /**
     * Creates new form JMETrackImplVisualPanel1
     */
    public JMETrackImplVisualPanel1() {
        initComponents();
    }
public JMETrackImplVisualPanel1(Project project) {
        this();
        
        
        //pesquisa pelos arquivos que xml de track
        ArrayList<FilePathObject> lista = pesquisaArquivos(project.getProjectDirectory().getChildren());
        DefaultComboBoxModel<FilePathObject> model = new DefaultComboBoxModel<FilePathObject>();
        for (FilePathObject fileObj : lista) {
            model.addElement(fileObj);
        }
        cmbTrackXMLFile.setModel(model);
        
        /*FileObject[] files = project.getProjectDirectory().getChildren();
        String lista = project.getProjectDirectory().getName();
        for (FileObject fo : files) {
            lista+= "\n " + fo.getName();
        }
        jLabel2.setText(lista);*/
    }

public ArrayList<FilePathObject> pesquisaArquivos(FileObject[] files) {
        ArrayList<FilePathObject> lista = new ArrayList<FilePathObject>();
        
        for (FileObject fo : files) {
            if (fo.getPath().indexOf("src")>=0) {
                if (fo.getExt().toLowerCase().endsWith("xml")) {
                    try {
                        if (fo.asText().indexOf("xmlns:ns2=\"trackjmegen\"")>0) {
                            //xmlns:ns2="trackjmegen"
                        //lista.add(fo.getPath() + " - " + fo.getName() + " - " + fo.getExt());
                            lista.add(new FilePathObject(fo.getName(), fo.getPath()));
                        }
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }

                FileObject[] files2 = fo.getChildren();
                if (files2!=null) {
                    if (files2.length>0) {
                        lista.addAll(pesquisaArquivos(files2));
                    }
                }
            } 
        }  
        
                
        return lista;        
    }

    @Override
    public String getName() {
        return "Step #1";
    }

     public String getContent() {
         FilePathObject file = (FilePathObject) cmbTrackXMLFile.getSelectedItem();
         Track track = readFile(file.getPath());
         
        String code = "";
        //code += "\t\tparts.add(new Part(\"Models/parts/"+ "teste" +".mesh.j3o\", " +  "x" + ", " + "y" + ", " + "z" + " ));\n";
        
        for (TrackPart part: track.getParts()) {
            //converte os tamanhos da maquete p/ o tamanhos reais
            int x = part.getX() * 36;
            int y = 0;
            int z = part.getY() * 36;
 
            code += "parts.add(new Part(\"Models/Track/"+ part.getName() +".mesh.j3o\", " +  x + ", " + y + ", " + z + " ));\n";
        }
        
        return code;

    }
     
      public Track readFile(String pathFile) {
        Track track = null;
        try {
            JAXBContext context = JAXBContext.newInstance(Track.class);
            Unmarshaller uMarshaller = context.createUnmarshaller();
 
            String fileContent = new String(Files.readAllBytes(Paths.get(pathFile)));
                    
            
            StringReader reader = new StringReader(fileContent);
            track = (Track) uMarshaller.unmarshal(reader);
        } catch (Exception ex) {
            //Exceptions.printStackTrace(ex);
        }
        
        if (track==null) {
            track = new Track();
        }
        
        return track;

    }

    public String getFileName() {
         FilePathObject file = (FilePathObject) cmbTrackXMLFile.getSelectedItem();
         return file.getName();
    }

 
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        cmbTrackXMLFile = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, "XML Track File:");

        cmbTrackXMLFile.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbTrackXMLFile, 0, 305, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbTrackXMLFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(261, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbTrackXMLFile;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
