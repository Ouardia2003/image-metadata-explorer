# Projet POO : Gestion des images et métadonnées

## Membres du projet
- **Nom et prénom 1** : ACHAB Ouardia
- **Nom et prénom 2** : AMZAL Fariza
- **Groupe TD** : B

## Description du projet
Ce projet a pour objectif de développer une application Java permettant :
- D'explorer et de lister des images dans des répertoires.
- D'extraire et d'afficher les métadonnées des images (EXIF, XMP).
- De fournir une interface utilisateur en mode console (CLI) et graphique (GUI).

## Fonctionnalités principales
1. **Mode console (CLI)** :
   - Parcours des répertoires pour lister les images et leurs métadonnées.
   - Extraction de statistiques globales ou détaillées.
   - Sauvegarde et comparaison des états de répertoires.
2. **Mode graphique (GUI)** :
   - Exploration des dossiers avec affichage interactif des images et métadonnées.
   - Affichage des miniatures (thumbnails).

## Instructions d'installation et d'exécution
### Pré-requis
- **Java SE 21 (LTS)** doit être installé.
- Un environnement de développement Java tel qu'Eclipse, IntelliJ IDEA ou Visual Studio Code.

### Compilation
1. Ouvrez le projet dans votre environnement de développement (par exemple, Eclipse).
2. Générez le fichier `.jar` correspondant au mode que vous souhaitez :
   - **Mode console (CLI)** : Générez `cli.jar`.
   - **Mode graphique (GUI)** : Générez `gui.jar`.

### Exécution
#### Mode console (CLI)
1. Ouvrez l'invite de commande.
2. Naviguez vers le répertoire contenant le fichier `cli.jar`.
3. Lancez l'application avec la commande suivante :
   ```bash
   java -jar cli.jar <paramètre>
Le paramètre est obligatoire et peut être l'un des suivants :

    -h, --help : Affiche l'aide.
    -d, --directory <path> : Analyse un répertoire.
    -f, --file <file> : Analyse un fichier.
    --stat : Affiche des statistiques sur le fichier ou répertoire.
    --info : Affiche les métadonnées d'un fichier.
    --snapshotsave : Sauvegarde l'état d'un répertoire dans un fichier snapshot.
    --snapshotcompare <snapshot> : Compare un répertoire avec un fichier snapshot.
    --search <mot-clé> : Recherche les images dont le nom contient le mot-clé.

Exemple de commande pour le mode console :
 java -jar cli.jar -d ./images --stat
### Mode graphique (GUI)

    Ouvrez l'invite de commande.
    Naviguez vers le répertoire contenant le fichier gui.jar.
    Lancez l'application avec la commande suivante :
    java -jar gui.jar

### Structure du projet

    src/ : Contient les fichiers source (.java).
    bin/ : Contient les fichiers compilés (.class).
    doc/ : Contient la documentation générée par Javadoc.
    cli.jar : Archive exécutable pour le mode console.
    gui.jar : Archive exécutable pour le mode graphique.

### Documentation

    La documentation technique générée avec Javadoc est disponible dans le dossier doc/.

## Bibliothèques externes utilisées
Le projet utilise les bibliothèques suivantes pour l'extraction et la manipulation des métadonnées :
1. **`metadata-extractor-2.19.0.jar`** :
   - Permet d'extraire des métadonnées EXIF et autres formats associés aux images.
   - Site officiel : [metadata-extractor](https://github.com/drewnoakes/metadata-extractor).
2. **`xmpcore-5.1.3.jar`** et **`xmpcore-6.1.11.jar`** :
   - Fournissent des outils pour lire et manipuler des métadonnées au format XMP.
   - Site officiel : [xmpcore](https://adobe.github.io/XMP-Toolkit-SDK/).