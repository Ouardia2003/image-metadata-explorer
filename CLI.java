package application;

import java.io.IOException;
import java.util.List;

/**
 * La classe CLI offre une interface en ligne de commande permettant
 * d'analyser des fichiers et des répertoires, d'afficher des statistiques,
 * de gérer des snapshots et d'extraire des métadonnées.
 * 
 * <p>Les options disponibles incluent :</p>
 * <ul>
 *     <li>{@code -h, --help} : Affiche l'aide.</li>
 *     <li>{@code -d, --directory <path>} : Analyse un répertoire.</li>
 *     <li>{@code -f, --file <file>} : Analyse un fichier.</li>
 *     <li>{@code --stat} : Affiche des statistiques sur le fichier ou répertoire.</li>
 *     <li>{@code --info} : Affiche les métadonnées d'un fichier.</li>
 *     <li>{@code --snapshotsave} : Sauvegarde l'état d'un répertoire dans un fichier snapshot.</li>
 *     <li>{@code --snapshotcompare <snapshot>} : Compare un répertoire avec un fichier snapshot.</li>
 *     <li>{@code --search <mot-clé>} : Recherche les images dont le nom contient le mot-clé.</li>
 * </ul>
 * 
 * @author Fariza
 */
public class CLI {

    /**
     * Constructeur par défaut de la classe CLI.
     * Utilisé pour initialiser les structures ou variables si nécessaire.
     */
    public CLI() {
        // Constructeur vide par défaut
    }

    /**
     * Point d'entrée principal du programme.
     * 
     * @param args Arguments de la ligne de commande.
     *             Options disponibles :
     *             <ul>
     *                 <li>{@code -h, --help} : Affiche l'aide.</li>
     *                 <li>{@code -d, --directory <path>} : Analyse un répertoire.</li>
     *                 <li>{@code -f, --file <file>} : Analyse un fichier.</li>
     *                 <li>{@code --stat} : Affiche des statistiques sur le fichier ou répertoire.</li>
     *                 <li>{@code --info} : Affiche les métadonnées d'un fichier.</li>
     *                 <li>{@code --snapshotsave} : Sauvegarde l'état d'un répertoire dans un fichier snapshot.</li>
     *                 <li>{@code --snapshotcompare <snapshot>} : Compare un répertoire avec un fichier snapshot.</li>
     *                 <li>{@code --search <mot-clé>} : Recherche les images dont le nom contient le mot-clé.</li>
     *             </ul>
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            afficherErreur("Aucun paramètre fourni. Utilisez -h ou --help pour afficher l'aide.");
            return;
        }
        try {
            if (containsOption(args, "-h", "--help")) {
                afficherAide();
            } else if (containsOption(args, "-d", "--directory")) {
                String chemin = getOptionValue(args, "-d", "--directory");

                if (chemin == null) {
                    afficherErreur("Aucun répertoire spécifié.");
                    return;
                }

                Repertoire repertoire = new Repertoire(chemin);
                repertoire.listerFichiers();

                if (containsOption(args, "--list", null)) {
                    afficherListeImages(repertoire);
                }

                if (containsOption(args, "--stat", null)) {
                    repertoire.afficherStatistiques();
                }

                if (containsOption(args, "--snapshotsave", null)) {
                    repertoire.snapshotsave("snapshot.txt");
                }

                if (containsOption(args, "--snapshotcompare", null)) {
                    String capture = getOptionValue(args, "--snapshotcompare", null);

                    if (capture == null) {
                        afficherErreur("Aucun snapshot spécifié pour la comparaison.");
                        return;
                    }

                    comparerSnapshots(capture, chemin);
                }

                if (containsOption(args, "--search", null)) {
                    String motCle = getOptionValue(args, "--search", null);

                    if (motCle == null) {
                        afficherErreur("Aucun mot-clé de recherche spécifié.");
                        return;
                    }

                    rechercherImage(repertoire, motCle);
                }

            } else if (containsOption(args, "-f", "--file")) {
                String cheminFichier = getOptionValue(args, "-f", "--file");

                if (cheminFichier == null) {
                    afficherErreur("Aucun fichier spécifié.");
                    return;
                }

                Fichier fichier = new Fichier(cheminFichier);

                if (containsOption(args, "--stat", null)) {
                    System.out.println(fichier);
                }

                if (containsOption(args, "--info", "-i")) {
                    Metadonnees metadonnees = new Metadonnees(cheminFichier);
                    System.out.println(metadonnees);
                }

            } else {
                afficherErreur("Options non reconnues. Utilisez -h ou --help pour afficher l'aide.");
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    /**
     * Affiche un message d'aide décrivant les options disponibles.
     */
    private static void afficherAide() {
        System.out.println("Usage : java -jar cli.jar [options]");
        System.out.println("Options disponibles :");
        System.out.println("-h, --help              Afficher cette aide.");
        System.out.println("-d, --directory <path>  Spécifie un répertoire à analyser.");
        System.out.println("-f, --file <file>       Spécifie un fichier à analyser.");
        System.out.println("--list                  Affiche la liste des fichiers images d'un répertoire.");
        System.out.println("--stat                  Affiche les statistiques (répertoire ou fichier).");
        System.out.println("--info                  Affiche les métadonnées d'un fichier.");
        System.out.println("--snapshotsave          Sauvegarde l'état d'un répertoire.");
        System.out.println("--snapshotcompare       Compare un répertoire à un état précédemment sauvegardé.");
        System.out.println("--search <mot-clé>      Recherche les images dont le nom contient le mot-clé.");
    }

    /**
     * Affiche un message d'erreur sur la sortie standard des erreurs.
     * @param message Le message d'erreur à afficher.
     */
    private static void afficherErreur(String message) {
        System.err.println(message);
    }

    /**
     * Vérifie si une option spécifique est présente dans les arguments.
     * @param args Tableau des arguments.
     * @param shortOpt Nom de l'option courte (ex. -d).
     * @param longOpt Nom de l'option longue (ex. --directory).
     * @return true si l'option est présente, false sinon.
     */
    private static boolean containsOption(String[] args, String shortOpt, String longOpt) {
        for (String arg : args) {
            if (arg.equals(shortOpt) || (longOpt != null && arg.equals(longOpt))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère la valeur associée à une option spécifique.
     * @param args Tableau des arguments.
     * @param shortOpt Nom de l'option courte (ex. -d).
     * @param longOpt Nom de l'option longue (ex. --directory).
     * @return La valeur associée à l'option, ou null si aucune valeur n'est spécifiée.
     */
    private static String getOptionValue(String[] args, String shortOpt, String longOpt) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(shortOpt) || (longOpt != null && args[i].equals(longOpt))) {
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    return args[i + 1];
                }
            }
        }
        return null;
    }

    /**
     * Affiche la liste des fichiers images d'un répertoire.
     * @param repertoire Le répertoire dont les fichiers seront listés.
     */
    private static void afficherListeImages(Repertoire repertoire) {
        List<Fichier> fichiersImages = repertoire.getFichiers();
        System.out.println("Liste des fichiers images :");
        for (Fichier fichier : fichiersImages) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image")) {
                System.out.println(fichier.getNom());
            }
        }
    }

    /**
     * Compare l'état actuel d'un répertoire avec un snapshot sauvegardé.
     * @param capture Le fichier snapshot à utiliser pour la comparaison.
     * @param cheminRepertoire Le chemin du répertoire à comparer.
     */
    private static void comparerSnapshots(String capture, String cheminRepertoire) {
        try {
            Repertoire repertoire = new Repertoire(cheminRepertoire);
            repertoire.snapshotCompare(cheminRepertoire, capture);
        } catch (IOException e) {
            afficherErreur("Erreur lors de la comparaison des snapshots : " + e.getMessage());
        }
    }

    /**
     * Recherche les fichiers images contenant un mot-clé dans leur nom.
     * @param repertoire Le répertoire dans lequel rechercher.
     * @param motCle Le mot-clé à rechercher dans les noms de fichiers.
     */
    private static void rechercherImage(Repertoire repertoire, String motCle) {
        List<Fichier> fichiersImages = repertoire.getFichiers();
        boolean trouve = false;

        System.out.println("Résultats de recherche pour \"" + motCle + "\" :");

        for (Fichier fichier : fichiersImages) {
            if (fichier.getTypeMime() != null && fichier.getTypeMime().startsWith("image") &&
                    fichier.getNom().toLowerCase().contains(motCle.toLowerCase())) {
                System.out.println(fichier.getNom() + " existe dans le répertoire.");
                trouve = true;
            }
        }

        if (!trouve) {
            System.out.println("Aucune image trouvée contenant le mot-clé \"" + motCle + "\".");
        }
    }
}
