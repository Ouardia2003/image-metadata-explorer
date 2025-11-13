package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Classe représentant un fichier sur le système avec ses métadonnées (chemin, nom, taille, date de dernière modification, type MIME).
 * 
 * @author Ouardia  
 */

public class Fichier {
    private String chemin;
    private String nom;
    private long taille;
    private long derniereModification;
    private String typeMime;
    

    /**
     * Constructeur qui initialise une instance de Fichier en récupérant les métadonnées du fichier spécifié.
     *
     * @param chemin le chemin absolu du fichier.
     * @throws IOException si le fichier spécifié n'existe pas.
     */
    public Fichier(String chemin) throws IOException {
        this.chemin = chemin;
        File file = new File(chemin);

        if (!file.exists()) {
            throw new IOException("Le fichier n'existe pas.");
        }

        this.nom = file.getName();
        this.taille = file.length();
        this.derniereModification = file.lastModified();
        this.typeMime = Files.probeContentType(file.toPath());
        
    }

    /**
     * Retourne le chemin absolu du fichier.
     *
     * @return le chemin absolu du fichier.
     */
    public String getChemin() {
        return chemin;
    }

    /**
     * Retourne le nom du fichier, y compris son extension.
     *
     * @return le nom du fichier.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la taille du fichier en octets.
     *
     * @return la taille du fichier en octets.
     */
    public long getTaille() {
        return taille;
    }

    /**
     * Retourne la date de dernière modification du fichier en millisecondes depuis le 1er janvier 1970.
     *
     * @return le timestamp de la dernière modification du fichier.
     */
    public long getDerniereModification() {
        return derniereModification;
    }

    /**
     * Retourne le type MIME du fichier.
     *
     * @return le type MIME du fichier, ou {@code null} si le type ne peut pas être déterminé.
     */
    public String getTypeMime() {
        return typeMime;
    }

    /**
     * Retourne une représentation textuelle du fichier contenant ses métadonnées principales.
     *
     * @return une chaîne de caractères formatée décrivant le fichier.
     */
    @Override
    public String toString() {
        return "Nom : " + nom + "\n" +
               "Chemin : " + chemin + "\n" +
               "Taille : " + taille + " octets\n" +
               "Dernière modification : " + new java.util.Date(derniereModification) + "\n" +
               "Type MIME : " + (typeMime != null ? typeMime : "Inconnu");
    }
}
