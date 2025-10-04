/**
 * @file : Rotation.java
 * @author : Quentin Michon, Gianni BEE
 * @date : 2025-09-29
 * @since : 2.0
 *
 * Description:
 *   Sous-commande CLI pour appliquer une rotation sur une image BMP.
 */

package ch.heigvd.commands;

import ch.heigvd.AvailableRotation;
import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.*;

/**
 * Sous-commande "rotation".
 * Permet d’appliquer une rotation sur une image BMP 24 bits.
 *
 * Les rotations disponibles sont définies dans l’énumération AvailableRotation :
 *  - ROTATE_90
 *  - ROTATE_180
 *  - ROTATE_270
 */
@CommandLine.Command(name = "rotation", description = "Applique une rotation à l'image")
public class Rotation implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    /**
     * Exécutée lorsque la commande "rotation" est appelée.
     * Ouvre le fichier source, applique la rotation choisie, puis écrit le résultat dans le fichier destination.
     */
    @Override
    public void run() {
        try (FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream("IOFile/" + parent.destination);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            System.out.println("Ouverture du fichier source...");

            BMP bmp = new BMP(bis);

            // Application de la rotation choisie
            bmp.appliesRotation(rotation);

            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image a bien été générée : IOFile/" + parent.destination);
        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
        }
    }

    /**
     * Option de ligne de commande permettant de choisir la rotation à appliquer.
     * Obligatoire, valeurs possibles : ROTATE_90, ROTATE_180, ROTATE_270.
     */
    @CommandLine.Option(
            names = {"-r", "--rotation"},
            description = "Rotation (horaire) à appliquer (valeurs possibles: ${COMPLETION-CANDIDATES})",
            required = true)
    protected AvailableRotation rotation;
}
