package project;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Simple Total Commander copy, based on JTree Components.

In this program available are functions like:

-adding new folder,
-deleting files and folders,
-coping files and folders using drag and drop method (simple drag an interesing you file to second JTree)
-sorting: alfabetic, reverse alfabetic, by modified date and reverse modified date

Author: Piotr Wygocki
 */



public class AddingFolder {
    public static void createFrame(DefaultMutableTreeNode currentFile, JTree tree,DefaultTreeModel treeModel,DefaultMutableTreeNode root) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Adding new folder");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {

                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);
                JPanel inputpanel = new JPanel();
                inputpanel.setLayout(new FlowLayout());
                JTextField input = new JTextField(25);
                JButton button = new JButton("Enter");
                FileSystemView fileSystemView = FileSystemView.getFileSystemView();
                button.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        String textFieldValue = input.getText();
                        String folder= currentFile + "\\" + textFieldValue;
                        File file = (File) currentFile.getUserObject();
                        File[] files = fileSystemView.getFiles(file, true);
                        try {
                            Files.createDirectory(Paths.get(folder));
                            File nowka = new File(folder);
                            addObject(nowka);

                        } catch (Exception FileAlreadyExistsException  ) {

                        }
                    }
                });
                inputpanel.add(input);
                inputpanel.add(button);
                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                tree.repaint();
                FileSystemView fileSystemView2 = FileSystemView.getFileSystemView();
                File file = (File) currentFile.getUserObject();
                File[] files = fileSystemView2.getFiles(file, true);
            }

            public DefaultMutableTreeNode addObject(Object child) {
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

            public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                                    Object child, boolean shouldBeVisible) {
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
        });
    }

}