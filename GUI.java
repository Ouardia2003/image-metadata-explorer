package application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Classe GUI pour fournir une interface graphique permettant de gérer
 * et d'explorer des fichiers et répertoires. Cette application offre
 * des fonctionnalités pour afficher des fichiers, sauvegarder des snapshots,
 * et comparer des snapshots.
 * @author Ouardia
 */
public class GUI {

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JFileChooser fileChooser;
    private JLabel imageLabel;
    private ImageIcon imageIcon;

    
    public GUI() {
        frame = new JFrame("Explorateur de fichiers");
        panel = new JPanel();
        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        imageLabel = new JLabel();

       
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

       
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, "Voulez-vous vraiment quitter ?", "Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });

        
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(imageLabel, BorderLayout.EAST);

        
        JPanel buttonPanel = new JPanel();
        JButton saveSnapshotButton = new JButton("Sauvegarder Snapshot");
        JButton compareSnapshotButton = new JButton("Comparer Snapshot");

        saveSnapshotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sauvegarderSnapshot();
            }
        });

        compareSnapshotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comparerSnapshot();
            }
        });

        buttonPanel.add(saveSnapshotButton);
        buttonPanel.add(compareSnapshotButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        
        frame.add(panel, BorderLayout.CENTER);

       
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Fichier");
        JMenuItem openItem = new JMenuItem("Ouvrir un répertoire");
        JMenuItem fileInfoItem = new JMenuItem("Afficher les informations du fichier");

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ouvrirRepertoire();
            }
        });

        fileInfoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    afficherInfosFichier();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        menu.add(openItem);
        menu.add(fileInfoItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    /**
     * Ouvre un répertoire et liste les fichiers qu'il contient.
     */
    private void ouvrirRepertoire() {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            if (directory.isDirectory()) {
                try {
                    Repertoire repertoire = new Repertoire(directory.getAbsolutePath());
                    repertoire.listerFichiers();
                    textArea.setText("Liste des fichiers dans : " + directory.getAbsolutePath() + "\n");

                    List<Fichier> fichiers = repertoire.getFichiers();
                    for (Fichier fichier : fichiers) {
                        textArea.append(fichier.getNom() + "\n");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de l'ouverture du répertoire : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un répertoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Affiche les informations détaillées sur un fichier sélectionné.
     *
     * @throws IOException si une erreur d'E/S se produit.
     */
    private void afficherInfosFichier() throws IOException {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.exists() && file.isFile()) {
                Fichier fichier = new Fichier(file.getAbsolutePath());
                textArea.setText("Informations sur le fichier : " + file.getName() + "\n");
                textArea.append("Taille : " + fichier.getTaille() + " Ko\n");
                textArea.append("Date de dernière modification : " + fichier.getDerniereModification() + "\n");

                try {
                    Metadonnees metadonnees = new Metadonnees(file.getAbsolutePath());
                    textArea.append("\nMétadonnées :\n" + metadonnees);
                } catch (Exception e) {
                    textArea.append("\nErreur lors de l'extraction des métadonnées : " + e.getMessage() + "\n");
                }

                if (file.getName().endsWith(".png") || file.getName().endsWith(".webp") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpg")) {
                    ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                    Image originalImage = originalIcon.getImage();

                    int width = 400;
                    int height = (int) (originalImage.getHeight(null) * ((double) width / originalImage.getWidth(null)));
                    Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

                    imageIcon = new ImageIcon(resizedImage);
                    imageLabel.setIcon(imageIcon);
                    imageLabel.setText("");
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("Aucune image à afficher.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fichier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Sauvegarde un snapshot du répertoire sélectionné dans un emplacement donné.
     */
    private void sauvegarderSnapshot() {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            String saveFolder = JOptionPane.showInputDialog(frame, "Entrez le chemin pour sauvegarder le snapshot :");

            if (saveFolder != null && !saveFolder.isEmpty()) {
                try {
                    Repertoire repertoire = new Repertoire(directory.getAbsolutePath());
                    repertoire.listerFichiers();
                    repertoire.snapshotsave(saveFolder);
                    JOptionPane.showMessageDialog(frame, "Snapshot sauvegardé avec succès dans : " + saveFolder,
                            "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la sauvegarde du snapshot : " + e.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Chemin de sauvegarde invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Compare un snapshot sauvegardé avec l'état actuel d'un répertoire.
     */
    private void comparerSnapshot() {
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            returnValue = fileChooser.showOpenDialog(frame);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File snapshotFile = fileChooser.getSelectedFile();
                try {
                    @SuppressWarnings("unused")
					Repertoire repertoire = new Repertoire(directory.getAbsolutePath());
                    textArea.setText(""); 
                    
                    
                    Set<String> removedFiles = new HashSet<>();
                    Set<String> addedFiles = new HashSet<>();

                    
                    File[] currentFiles = directory.listFiles();
                    if (currentFiles != null) {
                        for (File file : currentFiles) {
                            addedFiles.add(file.getAbsolutePath());
                        }
                    }

                   
                    try (BufferedReader reader = new BufferedReader(new FileReader(snapshotFile))) {
                        String line;
                        boolean detailsSection = false;

                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.startsWith("Détails des fichiers")) {
                                detailsSection = true;
                                continue;
                            }
                            if (detailsSection && line.startsWith("Chemin :")) {
                                String chemin = line.substring("Chemin :".length()).trim();
                                if (!addedFiles.remove(chemin)) {
                                    removedFiles.add(chemin);
                                }
                            }
                        }
                    }

                 
                    StringBuilder result = new StringBuilder();
                    if (!removedFiles.isEmpty()) {
                        result.append("Fichiers supprimés :\n");
                        for (String removed : removedFiles) {
                            result.append(removed).append("\n");
                        }
                    }
                    if (!addedFiles.isEmpty()) {
                        result.append("\nFichiers ajoutés :\n");
                        for (String added : addedFiles) {
                            result.append(added).append("\n");
                        }
                    }
                    if (removedFiles.isEmpty() && addedFiles.isEmpty()) {
                        result.append("Aucun changement détecté.");
                    }

                    
                    textArea.setText(result.toString());

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Erreur lors de la comparaison : " + e.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Aucun fichier snapshot sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Aucun répertoire sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Affiche la fenêtre principale de l'application.
     */
    public void afficher() {
        frame.setVisible(true);
    }

    /**
     * Point d'entrée principal de l'application.
     * Initialise et lance l'interface utilisateur.
     *
     * @param args Arguments de ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI().afficher();
            }
        });
    }
}