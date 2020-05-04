package project;

/*
Simple Total Commander copy, based on JTree Components.

In this program available are functions like:

-adding new folder,
-deleting files and folders,
-coping files and folders using drag and drop method (simple drag an interesing you file to second JTree)
-sorting: alfabetic, reverse alfabetic, by modified date and reverse modified date

Author: Piotr Wygocki
 */





import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;
import java.awt.BorderLayout;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.*;

class TotalCommander {
    public int how_show_childrens;
    public FileSystemView fileSystemView;
    public JPanel gui;
    public JTree tree;
    public JTree tree2;
    public DefaultTreeModel treeModel;
   public DefaultMutableTreeNode root;
    public DefaultMutableTreeNode currentFile;
    public JScrollPane treeScroll2;
    public JScrollPane treeScroll;
    public JSplitPane splitPane;
    private Collections ArrayUtils;

    public JPanel getGui() {
        if (gui==null) {
            how_show_childrens=1;
            gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(0,5,5,5));
            fileSystemView = FileSystemView.getFileSystemView();
             root = new DefaultMutableTreeNode();
            treeModel = new DefaultTreeModel(root);
            TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent t){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)t.getPath().getLastPathComponent();
                    showChildren(node);
                    currentFile=null;
                    currentFile = (DefaultMutableTreeNode) t.getPath().getLastPathComponent();
                }
            };
            class MyTreeModelListener implements TreeModelListener {
                public void treeNodesChanged(TreeModelEvent e) {
                    DefaultMutableTreeNode node;
                    node = (DefaultMutableTreeNode)
                            (e.getTreePath().getLastPathComponent());
                    try {
                        int index = e.getChildIndices()[0];
                        node = (DefaultMutableTreeNode)
                                (node.getChildAt(index));
                    } catch (NullPointerException exc) {}
                    
                }
                public void treeNodesInserted(TreeModelEvent e) {

                }
                public void treeNodesRemoved(TreeModelEvent e) {
                }
                public void treeStructureChanged(TreeModelEvent e) {
                }
            }

            KeyListener keyListener_left = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F7) {
                        ;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_F8)
                    {;}            }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F7) {
                        AddingFolder.createFrame(currentFile,tree, treeModel,root);

                        checking(tree,tree2,how_show_childrens);

                        tree.revalidate();
                        tree2.revalidate();
                        tree.repaint();
                        tree2.repaint();
                    }
                   else if (e.getKeyCode() == KeyEvent.VK_F8) {
                        DeletingFolder.deleteFunction(currentFile,tree, treeModel,root);

                        checking(tree,tree2,how_show_childrens);

                        tree.revalidate();
                        tree2.revalidate();
                        tree.repaint();
                        tree2.repaint();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    ;
                }

            };
            KeyListener keyListener_right = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F7) {
                        ;
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_F8)
                    {;}            }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_F7) {

                        AddingFolder.createFrame(currentFile,tree2, treeModel,root);
                        expandAllNodes(tree, 0, tree.getRowCount());
                        expandAllNodes(tree2, 0, tree.getRowCount());

                        checking(tree,tree2,how_show_childrens);

                        tree.revalidate();
                        tree2.revalidate();
                        tree.repaint();
                        tree2.repaint();
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_F8) {
                        DeletingFolder.deleteFunction(currentFile,tree2, treeModel,root);
                        expandAllNodes(tree, 0, tree.getRowCount());
                        expandAllNodes(tree2, 0, tree.getRowCount());

                        checking(tree,tree2,how_show_childrens);

                        tree.revalidate();
                        tree2.revalidate();
                        tree.repaint();
                        tree2.repaint();
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    ;
                }
            };

            File[] roots = fileSystemView.getRoots();
            for (File fileSystemRoot : roots) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add( node );
                File[] files = fileSystemView.getFiles(fileSystemRoot, true);
                for (File file : files) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
            treeModel.addTreeModelListener(new MyTreeModelListener() {
            });
            tree = new JTree(treeModel);
            tree.setEditable(true);
            tree.getSelectionModel().setSelectionMode
                   (TreeSelectionModel.SINGLE_TREE_SELECTION);

            tree.setRootVisible(false);
            tree.addTreeSelectionListener(treeSelectionListener);
            tree.addKeyListener(keyListener_left);
            tree.setCellRenderer(new project.FileTreeCellRenderer());
            tree.expandRow(0);
            tree.setDragEnabled(true);
            treeScroll = new JScrollPane(tree);
            Dimension preferredSize = treeScroll.getPreferredSize();
            Dimension widePreferred = new Dimension((int) (preferredSize.getWidth()* 0.8), (int) (preferredSize.getHeight()*1.75));
            treeScroll.setPreferredSize( widePreferred );
            tree2 = new JTree(treeModel);
            tree2.setRootVisible(false);
            tree2.addTreeSelectionListener(treeSelectionListener);
            tree2.setCellRenderer(new project.FileTreeCellRenderer());
            tree2.addKeyListener(keyListener_right);
            tree2.expandRow(0);
            tree2.setDragEnabled(true);

            tree2.setTransferHandler(new TransferHandler(){
                @Override
                public boolean importData(TransferSupport support) {
                    if (!canImport(support)) {
                        return false;
                    }
                    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
                    TreePath path = dl.getPath();
                    var des =(dl.getPath().getLastPathComponent());
                    String dest = des.toString();
                    int childIndex = dl.getChildIndex();

                    String data;
                    try {
                        data = (String) support.getTransferable().getTransferData(
                                DataFlavor.stringFlavor);
                    } catch (Exception e) {
                        return false;
                    }

                    if (childIndex == -1) {
                        childIndex = tree.getModel().getChildCount(
                                path.getLastPathComponent());
                    }
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(data);
                    String source= newNode.toString();
                    File source_help = new File(source);
                    dest=dest + "\\"  +source_help.getName();
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();

                    try {
                        copyDirectory(source_help,new File(dest));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   addObject( parentNode,source_help);

                   checking(tree,tree2,how_show_childrens);

                    return true;
                }

                public boolean canImport(TransferSupport support) {
                    if (!support.isDrop()) {
                        return false;
                    }
                    support.setShowDropLocation(true);
                    if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        return false;
                    }
                    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
                    TreePath path = dl.getPath();
                    if (path == null) {
                        return false;
                    }
                    return true;
                }
            });

             treeScroll2 = new JScrollPane(tree2);
            treeScroll2.setPreferredSize(widePreferred);
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, treeScroll2);
            JPanel north_panel = new JPanel();
            north_panel.setPreferredSize(new Dimension(gui.getWidth(),30));

            JButton refresh= new JButton("Sort");
            refresh.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                   Sorting sorter = new Sorting();
                  sorter.sorting_alfabetic(tree,tree2);
                    expandAllNodes(tree, 0, tree.getRowCount());
                    expandAllNodes(tree2, 0, tree.getRowCount());
                    refresh(tree,tree2,gui,splitPane);
                    how_show_childrens=2;
                   treeModel.reload();
                }
            });


            JButton reverse_refresh= new JButton("Reverse Sort");
            reverse_refresh.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    Sorting sorter = new Sorting();
                    sorter.sorting_alfabetic_reversed(tree,tree2);
                    expandAllNodes(tree, 0, tree.getRowCount());
                    expandAllNodes(tree2, 0, tree.getRowCount());
                    refresh(tree,tree2,gui,splitPane);
                    how_show_childrens=3;
                    treeModel.reload();
                }
            });
            JButton date_sort= new JButton("Date Sort");
            date_sort.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    Sorting sorter = new Sorting();
                    sorter.sorting_by_date(tree,tree2);
                    expandAllNodes(tree, 0, tree.getRowCount());
                    expandAllNodes(tree2, 0, tree.getRowCount());
                    refresh(tree,tree2,gui,splitPane);
                    how_show_childrens=4;
                    treeModel.reload();
                }
            });

            JButton date_sort_reversed= new JButton("Reverse Date Sort");
            date_sort_reversed.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    Sorting sorter = new Sorting();
                    sorter.sorting_by_date_reversed(tree,tree2);
                    expandAllNodes(tree, 0, tree.getRowCount());
                    expandAllNodes(tree2, 0, tree.getRowCount());
                    refresh(tree,tree2,gui,splitPane);
                    how_show_childrens=5;
                    treeModel.reload();
                }
            });

            north_panel.add(refresh);
            north_panel.add(reverse_refresh);
            north_panel.add(date_sort);
            north_panel.add(date_sort_reversed);

            treeScroll.repaint();
            treeScroll2.repaint();
            splitPane.repaint();
            gui.add(north_panel,BorderLayout.NORTH);
            gui.add(splitPane, BorderLayout.CENTER);
            JPanel south_panel = new JPanel();
            south_panel.setPreferredSize(new Dimension(gui.getWidth(),40));
            JTextField F7 = new JTextField("  Add Component by pressing F7");
            F7.setEditable(false);
            F7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            JTextField F8 = new JTextField("  Delete Component by pressing F8");
            F8.setEditable(false);
            F8.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            JTextField info = new JTextField("  You can easily copy files by using drag and drop method");
            info.setEditable(false);
            info.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            F7.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            GroupLayout layout= new GroupLayout(south_panel);
            layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(
                    layout.createParallelGroup().addComponent(F7).addComponent(Box.createRigidArea(new Dimension(5,0))).addComponent(F8)
            )
            .addComponent(info));
            gui.add(south_panel,BorderLayout.SOUTH);
            gui.revalidate();
            gui.repaint();

        }
        return gui;
    }

    //---------------------------------------------------------------------
    private void refresh(JTree tree,JTree tree2,JPanel gui,JSplitPane splitPane)
    {
        tree.revalidate();
        tree.repaint();
        tree2.revalidate();
        tree2.repaint();
        gui.revalidate();
        gui.repaint();
        splitPane.repaint();
        splitPane.revalidate();
    }

    //------------------------------------------------------------------------------------------------------------------
    private void checking(JTree tree,JTree tree2,int how_show_childrens)
    {
        if(how_show_childrens==2)
        {
            Sorting sorter = new Sorting();
            sorter.sorting_alfabetic(tree,tree2);
            expandAllNodes(tree, 0, tree.getRowCount());
            expandAllNodes(tree2, 0, tree.getRowCount());
        }
        else if(how_show_childrens==3)
        {
            Sorting sorter = new Sorting();
            sorter.sorting_alfabetic_reversed(tree,tree2);
            expandAllNodes(tree, 0, tree.getRowCount());
            expandAllNodes(tree2, 0, tree.getRowCount());
        }
        else if(how_show_childrens==4)
        {
            Sorting sorter = new Sorting();
            sorter.sorting_by_date(tree,tree2);
            expandAllNodes(tree, 0, tree.getRowCount());
            expandAllNodes(tree2, 0, tree.getRowCount());
        }
        else if(how_show_childrens==5)
        {
            Sorting sorter = new Sorting();
            sorter.sorting_by_date_reversed(tree,tree2);
            expandAllNodes(tree, 0, tree.getRowCount());
            expandAllNodes(tree2, 0, tree.getRowCount());
        }
    }
    //-------------------------------------------------------------------------------

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
        for(int i=startingIndex;i<rowCount;++i){
            tree.expandRow(i);
        }
        if(tree.getRowCount()!=rowCount){
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    //---------------------------------------------------------------------------------------

public static void copyDirectory(File sourceDir, File targetDir)
        throws IOException {
        if(!targetDir.exists()) {
            if (sourceDir.isDirectory()) {
                copyDirectoryRecursively(sourceDir, targetDir);
            } else {
                Files.copy(sourceDir.toPath(), targetDir.toPath());
            }
        }else ;
}

    private static void copyDirectoryRecursively(File source, File target)
            throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }
        for (String child : source.list()) {
            copyDirectory(new File(source, child), new File(target, child));
        }
    }

    //------------------------------------------------------------------------------------------------------------------

public  DefaultMutableTreeNode addObject(Object child) {
    DefaultMutableTreeNode parentNode = null;
    TreePath parentPath = tree.getSelectionPath();

    if (parentPath == null) {
        parentNode = root;
    } else {
        parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
    }

    return addObject(parentNode, child, true);
}

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        if (parent == null) {
            parent = root;
        }
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void showChildren(final DefaultMutableTreeNode node) {
        if (how_show_childrens==1) {
            tree.setEnabled(false);
            tree2.setEnabled(false);
            SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true);
                        if (node.isLeaf()) {
                            for (File child : files) {
                                publish(child);
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }
                @Override
                protected void done() {
                    tree.setEnabled(true);
                    tree2.setEnabled(true);
                }
            };
            worker.execute();
        }
       else if (how_show_childrens==2) {
            tree.setEnabled(false);
            tree2.setEnabled(false);
            SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true);
                        if (node.isLeaf()) {
                            for (File child : files) {
                                publish(child);
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }
                @Override
                protected void done() {
                    tree.setEnabled(true);
                    tree2.setEnabled(true);
                }
            };
            worker.execute();
        }
        else if(how_show_childrens==3)
        {
            tree.setEnabled(false);
            tree2.setEnabled(false);
            SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true);
                        ArrayUtils.reverse(Arrays.asList(files));
                        if (node.isLeaf()) {
                            for (File child : files) {
                                publish(child);
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }
                @Override
                protected void done() {
                    tree.setEnabled(true);
                    tree2.setEnabled(true);
                }
            };
            worker.execute();
        }
        else if(how_show_childrens==4)
        {
            tree.setEnabled(false);
            tree2.setEnabled(false);
            SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true);
                        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                        if (node.isLeaf()) {
                            for (File child : files) {
                                publish(child);
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }
                @Override
                protected void done() {
                    tree.setEnabled(true);
                    tree2.setEnabled(true);
                }
            };
            worker.execute();
        } else
        {
            tree.setEnabled(false);
            tree2.setEnabled(false);
            SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
                @Override
                public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if (file.isDirectory()) {
                        File[] files = fileSystemView.getFiles(file, true);
                        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                        ArrayUtils.reverse(Arrays.asList(files));
                        if (node.isLeaf()) {
                            for (File child : files) {
                                publish(child);
                            }
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<File> chunks) {
                    for (File child : chunks) {
                        node.add(new DefaultMutableTreeNode(child));
                    }
                }
                @Override
                protected void done() {
                    tree.setEnabled(true);
                    tree2.setEnabled(true);
                }
            };
            worker.execute();
        }
    }

//----------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                }
                JFrame f = new JFrame("Total Commander");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                TotalCommander TotalCommander = new TotalCommander();
                f.setContentPane( TotalCommander.getGui());
                f.pack();
                f.setLocationByPlatform(true);
                f.revalidate();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }

        });

    }
}

//------------------------------------------------------------------------------------------
class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private FileSystemView fileSystemView;
    private JLabel label;

    FileTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        File file = (File) node.getUserObject();
        label.setIcon(fileSystemView.getSystemIcon(file));
        label.setText(fileSystemView.getSystemDisplayName(file));
        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }
        return label;
    }
}