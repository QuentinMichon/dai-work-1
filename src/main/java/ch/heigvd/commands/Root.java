/**
 * @file : Root.java
 * @author : Quentin Michon, Gianni BEE
 * @date : 2025-09-29
 * @since : 1.0
 *
 * Description:
 *   Commande principale CLI pour traiter des images BMP v3 24 bits.
 *   Par défaut, elle duplique un fichier BMP source.
 */

package ch.heigvd.commands;

import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.*;

/**
 * Commande principale du programme.
 * Permet de traiter des images BMP 24 bits v3.
 *
 * Par défaut (sans sous-commande), cette commande duplique simplement un fichier BMP.
 * Elle lit le fichier source et en génère une copie dans le dossier IOFile/.
 */
@CommandLine.Command(
        description = "Un CLI qui permet de faire du traitement d'image au format BMP v3 24bits",
        version = "1.0.0",
        subcommands = {Filter.class, Rotation.class},
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)
public class Root implements Runnable {

    /**
     * Exécutée si aucune sous-commande n’est appelée.
     * Duplique le fichier BMP source dans un nouveau fichier destination.
     *
     * Affiche un message en cas de succès ou d’erreur.
     */
    @Override
    public void run() {
        System.out.println("Duplication du fichier source...");

        try (FileInputStream fis = new FileInputStream("IOFile/" + source);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream("IOFile/" + destination);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            BMP bmp = new BMP(bis);
            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image a bien été générée : IOFile/" + destination);

        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
        }
    }

    /**
     * Nom du fichier source BMP.
     * Premier argument obligatoire de la commande.
     */
    @CommandLine.Parameters(index = "0", description = "Nom du fichier source (.bmp).")
    protected String source;

    /**
     * Nom du fichier destination BMP.
     * Deuxième argument obligatoire de la commande.
     * Le fichier est créé dans le dossier IOFile/.
     */
    @CommandLine.Parameters(index = "1", description = "Nom du nouveau fichier (.bmp).")
    protected String destination;
}
