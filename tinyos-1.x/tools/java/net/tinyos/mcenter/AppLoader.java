/*
 * Copyright (c) 2003, Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

/**
 * @author Andras Nadas
 * @last modified 12/04/2003
 */

package net.tinyos.mcenter;

public class AppLoader extends MessageCenterInternalFrame {

    private java.util.prefs.Preferences prefs  = java.util.prefs.Preferences.userNodeForPackage(this.getClass());
    private java.util.prefs.Preferences permanentStorage;
        
    javax.swing.DefaultListModel moduleListModel = new javax.swing.DefaultListModel();

    
    /** Creates new form AppLoader */
    public AppLoader() {
        super("App Loader");
        initComponents();
        permanentStorage = prefs.node(prefs.absolutePath()+"/AppLoader");
        loadModulList();
        this.moduleList.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount() == 2){
                    startModule(((ModuleListItem)moduleList.getSelectedValue()).className);
                }
            }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        moduleList = new javax.swing.JList(moduleListModel);
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(false);
        setTitle("App Loader");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        moduleList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(moduleList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jTextField1.setText("net.tinyos.mcenter.");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        jButton1.setText("LoadApp");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButton1, gridBagConstraints);

        jButton2.setText("Remove Form List");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        setSize(new java.awt.Dimension(320, 299));
    }//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        jButton1ActionPerformed(evt);
    }//GEN-LAST:event_jTextField1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int index = moduleList.getSelectedIndex();
        ModuleListItem delItem = (ModuleListItem) moduleListModel.get(index);
        moduleListModel.remove(index);
        permanentStorage.remove(delItem.name);
        //saveModulList();
        
        int size = moduleListModel.getSize();
        
        
        if (size == 0) {
            jButton2.setEnabled(false);
            
            //Adjust the selection
        } else {
            //removed item in last position
            if (index == moduleListModel.getSize())
                index--;
            //otherwise select same index
            moduleList.setSelectedIndex(index);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Class module;
        String className = this.jTextField1.getText();
        String name;
        
        if(isModulInList(className))
            return;
        
        try{
            System.out.println("Loading Module...");
            module = Class.forName(className);
            module.newInstance();
            System.out.println("\""+ module.getName()+ "\" has been Succefully loaded!" );
            name = module.getName().substring(module.getName().lastIndexOf('.')+1);
            
            
            int index = moduleList.getSelectedIndex();
            int size = moduleListModel.getSize();
            
            //If no selection or if item in last position is selected,
            //add the line to end of list, and select it
            if (index == -1 || (index+1 == size)) {
                moduleListModel.addElement(new ModuleListItem(name,className));
                moduleList.setSelectedIndex(size);
                
                //Otherwise insert the new line after the current selection,
                //and select it
            } else {
                moduleListModel.addElement(new ModuleListItem(name,className));
                moduleList.setSelectedIndex(index+1);
            }
            jButton2.setEnabled(true);
            permanentStorage.put(name,className.trim());
            //saveModulList();
            
        }catch(LinkageError le){
            System.err.println("Could not Link module: " + le.toString());
        }catch(ClassNotFoundException cnfe){
            System.err.println("Could not Found module: " + cnfe.toString());
        }catch(IllegalAccessException iae){
            System.err.println("Illegal Access in module: " + iae.toString());
        }catch(InstantiationException ie){
            System.err.println("Could not Instatiate module: " + ie.toString());
        }catch(SecurityException se){
            System.err.println("Security error in module: " + se.toString());
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList moduleList;
    // End of variables declaration//GEN-END:variables
    
    protected void startModule(String className){
        try{
            System.out.println("Loading Module...");
            Class module = Class.forName(className);
            module.newInstance();
            System.out.println("\""+ module.getName()+ "\" has been Succefully loaded!" );
        }catch(LinkageError le){
            System.err.println("Could not Link module: " + le.toString());
        }catch(ClassNotFoundException cnfe){
            System.err.println("Could not Found module: " + cnfe.toString());
        }catch(IllegalAccessException iae){
            System.err.println("Illegal Access in module: " + iae.toString());
        }catch(InstantiationException ie){
            System.err.println("Could not Instatiate module: " + ie.toString());
        }catch(SecurityException se){
            System.err.println("Security error in module: " + se.toString());
        }
    }
    
    /*************************Persistency handling*********************************/
    
//   static final String saveFile = "modules.list";
    
    protected void loadModulList(){
         try{
            java.util.HashSet keySet = new java.util.HashSet();
            keySet.addAll(java.util.Arrays.asList(permanentStorage.keys()));
            
            if(!keySet.isEmpty()){
                jButton2.setEnabled(true);
                java.util.Iterator keyIterator = keySet.iterator();
                while(keyIterator.hasNext()){
                        String key = (String)keyIterator.next();
                        String value = permanentStorage.get(key,"");
                        moduleListModel.addElement(new ModuleListItem(key,value));
                    }

            }
                
        }catch(Exception e){}
         sortModuleList();
    }
        
       /* try{
            java.io.File storage = new java.io.File(saveFile);
            if(storage.exists()){
                permanentStorage.load(new java.io.FileInputStream(storage));
                if(!permanentStorage.isEmpty()){
                    jButton2.setEnabled(true);
                    java.util.Iterator keyIterator = permanentStorage.keySet().iterator();
                    while(keyIterator.hasNext()){
                        String key = (String)keyIterator.next();
                        String value = permanentStorage.getProperty(key);
                        moduleListModel.addElement(new ModuleListItem(key,value));
                    }
                    
                }
            }
            
        }catch(java.io.FileNotFoundException fnfe){
            
        }catch(java.io.IOException ioe){
        }
    }*/
    
/*    protected void saveModulList(){
        try{
            java.io.File storage = new java.io.File(saveFile);
            if(storage.exists())
                storage.delete();
            
            java.io.FileOutputStream out = new java.io.FileOutputStream(storage);
            permanentStorage.store(new java.io.FileOutputStream(storage),"This File contains the modules which are in use by the message center");
        }catch(java.io.FileNotFoundException fnfe){
            
        }catch(java.io.IOException ioe){
        }
    }*/
    
    protected boolean isModulInList(String moduleClassName){
        boolean retval = false;
        try{
            java.util.HashSet keySet = new java.util.HashSet();
            keySet.addAll(java.util.Arrays.asList(permanentStorage.keys()));
            retval = keySet.contains(moduleClassName.substring(moduleClassName.lastIndexOf('.')+1));
        }catch(Exception e){}
        return retval;
    }
    
    /***************************Sorting the module list****************************/
    
    private void sortModuleList(){
    // The Module List is not too long so a Bubble sort will do fine.
    
    for(int i =0; i<moduleListModel.size()-1; i++)
        for(int j=0; j < moduleListModel.size()-(i+1); j++){
            ModuleListItem first = (ModuleListItem)moduleListModel.get(j);
            ModuleListItem second= (ModuleListItem)moduleListModel.get(j+1);
            
            if(first.name.compareTo(second.name) > 0){
                moduleListModel.set(j,second);
                moduleListModel.set(j+1,first);
            }
            
        }
        
    }
    
    
    /******************Inner classes to handle the module list*********************/
    
    private class ModuleListItem{
        public String name;
        public String className;
        
        ModuleListItem(String name, String className){
            this.name = name;
            this. className = className;
        }
        
        public String toString(){
            return  this.name;
        }
    }
    
    
    
    
}

