package application;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

import java.io.File;
import java.io.IOException;

/**
 * La classe Metadonnees permet d'extraire les informations de métadonnées d'une image.
 * Elle fournit des données telles que les dimensions de l'image, la résolution DPI, et les coordonnées GPS.
 * @author Ouardia
 */
public class Metadonnees {

    
    private String dimensions;    
    private String dpi;           
    private String coordonneesGPS; 
    /**
     * Constructeur de la classe Metadonnees.
     * Lit les métadonnées d'une image et les initialise dans les attributs.
     *
     * @param cheminImage Chemin absolu ou relatif vers le fichier image.
     * @throws IOException Si le fichier ne peut pas être lu.
     * @throws ImageProcessingException Si une erreur se produit lors de l'analyse des métadonnées.
     */
    public Metadonnees(String cheminImage) throws IOException, ImageProcessingException {
        File imageFile = new File(cheminImage);

       
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

      
        this.dimensions = extraireDimensions(metadata);
        this.dpi = extraireDPI(metadata);
        this.coordonneesGPS = extraireCoordonneesGPS(metadata);
    }

    /**
     * Extrait les dimensions de l'image à partir des métadonnées.
     *
     * @param metadata Métadonnées de l'image.
     * @return Dimensions au format "Largeur x Hauteur" ou "Non disponible".
     */
    private String extraireDimensions(Metadata metadata) {
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        if (jpegDirectory != null) {
            try {
                int width = jpegDirectory.getImageWidth();
                int height = jpegDirectory.getImageHeight();
                return width + "x" + height;
            } catch (Exception e) {
                return "Erreur dimensions JPEG";
            }
        }
        return "Non disponible";
    }

    /**
     * Extrait la résolution DPI de l'image à partir des métadonnées.
     *
     * @param metadata Métadonnées de l'image.
     * @return Résolution au format "dpiX x dpiY" ou "Non disponible".
     */
    private String extraireDPI(Metadata metadata) {
        ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (exifDirectory != null) {
            try {
                int dpiX = exifDirectory.getInt(ExifIFD0Directory.TAG_X_RESOLUTION);
                int dpiY = exifDirectory.getInt(ExifIFD0Directory.TAG_Y_RESOLUTION);
                return dpiX + "x" + dpiY;
            } catch (Exception e) {
                return "Erreur DPI";
            }
        }
        return "Non disponible";
    }

    /**
     * Extrait les coordonnées GPS de l'image à partir des métadonnées.
     *
     * @param metadata Métadonnées de l'image.
     * @return Coordonnées GPS au format "latitude, longitude" ou "Non disponible".
     */
    private String extraireCoordonneesGPS(Metadata metadata) {
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
            return gpsDirectory.getGeoLocation().toString();
        }
        return "Non disponible";
    }

    /**
     * Génère une représentation sous forme de chaîne de caractères des métadonnées extraites.
     *
     * @return Une chaîne décrivant les métadonnées (dimensions, DPI, coordonnées GPS).
     */
    @Override
    public String toString() {
        return "Métadonnées de l'image :\n" +
               "Dimensions : " + dimensions + "\n" +
               "DPI : " + dpi + "\n" +
               "Coordonnées GPS : " + coordonneesGPS;
    }

   
    /**
     * @return Dimensions de l'image (Largeur x Hauteur).
     */
    public String getDimensions() {
        return dimensions;
    }

    /**
     * @return Résolution DPI de l'image (dpiX x dpiY).
     */
    public String getDpi() {
        return dpi;
    }

    /**
     * @return Coordonnées GPS de l'image au format "latitude, longitude".
     */
    public String getCoordonneesGPS() {
        return coordonneesGPS;
    }
}
