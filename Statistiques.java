package application;

/**
 * La classe Statistiques permet de stocker et d'afficher des statistiques 
 * concernant des fichiers, notamment des fichiers images et leurs extensions spécifiques.
 * Elle inclut des compteurs pour :
 * - Le nombre total de fichiers.
 * - Le nombre total de fichiers images.
 * - Le nombre d'images par type d'extension (PNG, JPEG, WEBP).
 * 
 * Cette classe est utilisée pour analyser le contenu des répertoires.
 * 
 * @author Fariza
 */
public class Statistiques {

   
    public int nbFichiers; 
    public int nbImages;    
    public int nbPNG;     
    public int nbJPEG;      
    public int nbWEBP;      
    /**
     * Constructeur par défaut.
     * Initialise tous les compteurs à 0.
     */
    public Statistiques() {
        this.nbFichiers = 0;
        this.nbImages = 0;
        this.nbPNG = 0;
        this.nbJPEG = 0;
        this.nbWEBP = 0;
    }

    /**
     * Retourne une représentation textuelle des statistiques.
     * 
     * @return une chaîne de caractères contenant les statistiques :
     *         - Nombre total de fichiers.
     *         - Nombre total d'images.
     *         - Nombre d'images PNG.
     *         - Nombre d'images JPEG.
     *         - Nombre d'images WEBP.
     */
    @Override
    public String toString() {
        return "Statistiques :\n" +
               "Nombre de fichiers : " + nbFichiers + "\n" +
               "Nombre d'images : " + nbImages + "\n" +
               "Nombre d'images PNG : " + nbPNG + "\n" +
               "Nombre d'images JPEG : " + nbJPEG + "\n" +
               "Nombre d'images WEBP : " + nbWEBP;
    }

   
}
