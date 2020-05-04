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
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

public class DeletingFolder {
    public static void removeCurrentNode(JTree x, DefaultTreeModel treeModel) {
        TreePath currentSelection = x.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection
                    .getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }

    }
    public static void deleteFunction(DefaultMutableTreeNode currentFile, JTree x, DefaultTreeModel treeModel, DefaultMutableTreeNode root) {
        File main_file = (File) currentFile.getUserObject();
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        if (main_file.isDirectory()) {
            File[] files = fileSystemView.getFiles(main_file, true);
            for (File file : files) {
                if(file.isDirectory())
                {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
                    deleteFunction(node,x,treeModel,root);
                }
                removeCurrentNode(x,treeModel);
                file.delete();

            }
            main_file.delete();
            removeCurrentNode(x,treeModel);
        }
        else
        {
            main_file.delete();
            removeCurrentNode(x,treeModel);
        }
    }
}
