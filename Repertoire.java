package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe représentant un répertoire sur le système de fichiers.
 * Cette classe permet de lister les fichiers d'un répertoire, afficher des statistiques,
 * et identifier les fichiers image.
 *
 * @author Binome
 */
public class Repertoire {

    /**
     * Chemin absolu du répertoire.
     */
    public String chemin;

    /**
     * Liste des fichiers présents dans le répertoire.
     */
    private List<Fichier> fichiers;

    /**
     * Constructeur de la classe Repertoire.
     * Initialise le chemin et crée une liste vide pour stocker les fichiers.
     *
     * @param chemin le chemin absolu du répertoire.
     */
    public Repertoire(String chemin) {
        this.chemin = chemin;
        this.fichiers = new ArrayList<>();
    }

    /**
     * Liste les fichiers contenus dans le répertoire et les ajoute à la liste {@code fichiers}.
     *
     * @throws IOException si le répertoire n'existe pas ou n'est pas valide.
     */
    public void listerFichiers() throws IOException {
        File repertoire = new File(chemin);

        if (!repertoire.exists() || !repertoire.isDirectory()) {
            throw new IOException("Le répertoire spécifié n'existe pas ou n'est pas valide.");
        }

        File[] fichiersListe = repertoire.listFiles();

        if (fichiersListe != null) {
            for (File fichier : fichiersListe) {
                // Créez des objets Fichier avec leur chemin absolu
                Fichier fichierObjet = new Fichier(fichier.getAbsolutePath());
                fichiers.add(fichierObjet); // Ajoutez à la liste
            }
        }
    }

    /**
     * Affiche des statistiques générales sur les fichiers présents dans le répertoire.
     * Affiche notamment :
     * - Le nombre total de fichiers.
     * - Le nombre total de fichiers image.
     */
    public void afficherStatistiques() {
        System.out.println("Statistiques des fichiers du répertoire : ");
        System.out.println("Total de fichiers : " + fichiers.size());
        System.out.println("Total de fichiers image : " + compterFichiersImages());
        System.out.println("Nombre d'images PNG : " + compterFichiersPng());
        System.out.println("Nombre d'images JPEG : " + compterFichiersJPEG());
        System.out.println("Nombre d'images WEBP : " + compterFichiersWebp());
    }

    /**
     * Compte le nombre de fichiers image présents dans le répertoire.
     *
     * @return le nombre de fichiers image.
     */
    private int compterFichiersImages() {
        int count = 0;
        for (Fichier fichier : fichiers) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compte le nombre d'images PNG dans le répertoire.
     *
     * @return le nombre d'images PNG.
     */
    public int compterFichiersPng() {
        int png = 0;
        for (Fichier fichier : fichiers) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image/png")) {
                png++;
            }
        }
        return png;
    }

    /**
     * Compte le nombre d'images JPEG dans le répertoire.
     *
     * @return le nombre d'images JPEG.
     */
    public int compterFichiersJPEG() {
        int jpeg = 0;
        for (Fichier fichier : fichiers) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image/jpeg")) {
                jpeg++;
            }
        }
        return jpeg;
    }

    /**
     * Compte le nombre d'images WEBP dans le répertoire.
     *
     * @return le nombre d'images WEBP.
     */
    public int compterFichiersWebp() {
        int webp = 0;
        for (Fichier fichier : fichiers) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image/webp")) {
                webp++;
            }
        }
        return webp;
    }

    /**
     * Affiche la liste des fichiers image trouvés dans le répertoire.
     */
    public void afficherFichiersImages() {
        System.out.println("Fichiers images trouvés : ");
        for (Fichier fichier : fichiers) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image")) {
                System.out.println(fichier);
            }
        }
    }

    /**
     * Retourne la liste des fichiers présents dans le répertoire.
     *
     * @return une liste de fichiers.
     */
    public List<Fichier> getFichiers() {
        return fichiers;
    }

    /**
     * Retourne le chemin absolu du répertoire.
     *
     * @return le chemin du répertoire.
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Retourne une représentation textuelle du répertoire, incluant son chemin.
     *
     * @return une chaîne de caractères décrivant le répertoire.
     */
    @Override
    public String toString() {
        return "Répertoire : " + chemin;
    }

    /**
     * Capture un snapshot de l'état actuel du répertoire et l'enregistre dans un fichier texte.
     *
     * @param dossierSnapshots le chemin du dossier où le snapshot doit être enregistré.
     * @throws IOException si une erreur survient lors de l'écriture du fichier.
     */
    public void snapshotsave(String dossierSnapshots) throws IOException {
        File repertoireSnapshots = new File(dossierSnapshots);
        if (!repertoireSnapshots.exists()) {
            if (!repertoireSnapshots.mkdirs()) {
                throw new IOException("Impossible de créer le répertoire pour les snapshots.");
            }
        }

        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String cheminSnapshot = dossierSnapshots + File.separator + "snapshot_" + dateStr + ".txt";

        File fichierSnapshot = new File(cheminSnapshot);
        try (FileWriter writer = new FileWriter(fichierSnapshot)) {
            writer.write("Snapshot du répertoire : " + chemin + "\n");
            writer.write("Nombre total de fichiers : " + fichiers.size() + "\n");
            writer.write("Nombre de fichiers image : " + compterFichiersImages() + "\n");
            writer.write("Nombre d'images PNG : " + compterFichiersPng() + "\n");
            writer.write("Nombre d'images JPEG : " + compterFichiersJPEG() + "\n");
            writer.write("Nombre d'images WEBP : " + compterFichiersWebp() + "\n\n");
            writer.write("Détails des fichiers :\n");

            for (Fichier fichier : fichiers) {
                writer.write(fichier.toString() + "\n");
            }
        }

        System.out.println("Snapshot enregistré dans : " + cheminSnapshot);
    }

    /**
     * Compare l'état actuel du répertoire avec un snapshot existant.
     *
     * @param cheminRepertoire le chemin du répertoire à comparer.
     * @param captureFile le fichier snapshot à comparer.
     * @throws IOException si une erreur survient lors de la lecture ou de l'accès.
     */
    public void snapshotCompare(String cheminRepertoire, String captureFile) throws IOException {
        File directory = new File(cheminRepertoire);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Le répertoire spécifié n'existe pas ou n'est pas valide.");
        }

        Set<String> currentFilePaths = new HashSet<>();
        File[] currentFiles = directory.listFiles();
        if (currentFiles != null) {
            for (File file : currentFiles) {
                if (file.isFile()) {
                    currentFilePaths.add(file.getAbsolutePath());
                }
            }
        }

        Set<String> snapshotFilePaths = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(captureFile))) {
            String line;
            boolean isInDetailsSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Détails des fichiers")) {
                    isInDetailsSection = true;
                    continue;
                }

                if (isInDetailsSection && line.startsWith("Chemin :")) {
                    String chemin = line.substring("Chemin :".length()).trim();
                    snapshotFilePaths.add(chemin);
                }
            }
        }

        boolean changesDetected = false;

        for (String snapshotPath : snapshotFilePaths) {
            if (!currentFilePaths.contains(snapshotPath)) {
                System.out.println("Fichier supprimé : " + snapshotPath);
                changesDetected = true;
            }
        }

        for (String currentPath : currentFilePaths) {
            if (!snapshotFilePaths.contains(currentPath)) {
                System.out.println("Fichier ajouté : " + currentPath);
                changesDetected = true;
            }
        }

        if (!changesDetected) {
            System.out.println("Aucun changement détecté dans le répertoire.");
        }
    }
}
