package application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe représentant un fichier image, héritant de la classe {@link Fichier}.
 * Cette classe permet de gérer les dimensions d'une image (largeur et hauteur).
 * 
 * @author Fariza 
 */
public class FichierImage extends Fichier {
    private int largeur;
    private int hauteur;

    /**
     * Constructeur qui initialise une instance de FichierImage et extrait les dimensions de l'image.
     *
     * @param chemin le chemin absolu du fichier image.
     * @throws IOException si le fichier n'existe pas ou n'est pas une image valide.
     */
    public FichierImage(String chemin) throws IOException {
        super(chemin); 
        extraireDimensions(); 
    }

    /**
     * Méthode privée qui extrait les dimensions de l'image (largeur et hauteur).
     *
     * @throws IOException si le fichier n'est pas une image valide.
     */
    private void extraireDimensions() throws IOException {
        File file = new File(getChemin());
        BufferedImage image = ImageIO.read(file);

        if (image != null) {
            this.largeur = image.getWidth();
            this.hauteur = image.getHeight();
        } else {
            throw new IOException("Le fichier n'est pas une image valide.");
        }
    }

    /**
     * Retourne une représentation textuelle du fichier image, incluant les dimensions de l'image.
     *
     * @return une chaîne de caractères formatée décrivant le fichier image et ses dimensions.
     */
    @Override
    public String toString() {
        return super.toString() + "\n" +
               "Dimensions : " + largeur + " x " + hauteur;
    }

    /**
     * Retourne la largeur de l'image en pixels.
     *
     * @return la largeur de l'image.
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * Retourne la hauteur de l'image en pixels.
     *
     * @return la hauteur de l'image.
     */
    public int getHauteur() {
        return hauteur;
    }
}
