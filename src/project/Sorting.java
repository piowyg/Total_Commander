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



import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.io.File;
import java.util.*;

public class Sorting {
    public FileSystemView fileSystemView;
    public DefaultTreeModel treeModel;
    public DefaultMutableTreeNode root;
    public DefaultMutableTreeNode currentFile3;
    private Collections ArrayUtils;

    public void settings(JTree tree,JTree tree2)
    {
        tree.setModel(null);
        tree2.setModel(null);
        fileSystemView = FileSystemView.getFileSystemView();
        root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent t){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)t.getPath().getLastPathComponent();
                currentFile3=null;
                currentFile3 = (DefaultMutableTreeNode) t.getPath().getLastPathComponent();
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
        treeModel.addTreeModelListener(new MyTreeModelListener() {
        });
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.setCellRenderer(new project.FileTreeCellRenderer());
        tree.expandRow(0);
        tree.setDragEnabled(true);
        tree2.setModel(treeModel);
        tree2.setRootVisible(false);
        tree2.addTreeSelectionListener(treeSelectionListener);
        tree2.setCellRenderer(new project.FileTreeCellRenderer());
        tree2.expandRow(0);
        tree2.setDragEnabled(true);
    }

    public void sorting_by_date(JTree tree,JTree tree2)
    {
        System.out.println("data sorting");

        settings(tree,tree2);
        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            Arrays.sort(files,Comparator.comparingLong(File::lastModified));
            for (File file : files) {
                node.add(new DefaultMutableTreeNode(file));
            }
        }
        tree.setModel(treeModel);
        treeModel.reload();
    }

    public void sorting_by_date_reversed(JTree tree,JTree tree2)
    {
        System.out.println("data sorting but reversed");

        settings(tree,tree2);
        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            Arrays.sort(files,Comparator.comparingLong(File::lastModified));
            ArrayUtils.reverse(Arrays.asList(files));
            for (File file : files) {
                node.add(new DefaultMutableTreeNode(file));
            }
        }
        tree.setModel(treeModel);
        treeModel.reload();
    }

    public void sorting_alfabetic_reversed(JTree tree,JTree tree2)
    {
        System.out.println("reverse");

        settings(tree,tree2);
        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            ArrayUtils.reverse(Arrays.asList(files));
            for (File file : files) {
                node.add(new DefaultMutableTreeNode(file));
            }
        }

        tree.setModel(treeModel);
        treeModel.reload();
    }
    public void  sorting_alfabetic(JTree tree,JTree tree2)
    {
        System.out.println("normal sorting ");

        settings(tree,tree2);
        File[] roots = fileSystemView.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                node.add(new DefaultMutableTreeNode(file));
            }
        }
        tree.setModel(treeModel);
        treeModel.reload();
    }
}
