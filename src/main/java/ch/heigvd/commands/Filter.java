package ch.heigvd.commands;

import ch.heigvd.image.BMP;
import picocli.CommandLine;
import java.io.*;

/**
 * Sous-commande "filter".
 * Permet d’appliquer un filtre sur une image BMP 24 bits.
 *
 * Les filtres disponibles sont :
 *  - Noir et blanc
 *  - Flou gaussien
 */
@CommandLine.Command(name = "filter", description = "Applique un filtre à l'image")
public class Filter implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    /**
     * Exécutée lorsque la commande "filter" est appelée.
     * Ouvre le fichier source, applique le filtre demandé puis écrit le résultat dans le fichier destination.
     */
    @Override
    public void run() {
        System.out.println("Ouverture du fichier source...");

        try (FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream("IOFile/" + parent.destination);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            BMP bmp = new BMP(bis);

            // Application du filtre choisi
            switch (filter) {
                case GAUSSIAN -> {
                    System.out.println("Application du filtre gaussien...");
                    bmp.appliesGaussianFilter();
                }
                case BLACK_WHITE -> {
                    System.out.println("Application du filtre noir et blanc...");
                    bmp.appliesBlackWhiteFilter();
                }
            }

            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image a bien été générée : IOFile/" + parent.destination);

        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
        }
    }

    /**
     * Liste des filtres disponibles pour l’image.
     */
    public enum AvailableFilter {
        BLACK_WHITE,
        GAUSSIAN,
    }

    /**
     * Option de ligne de commande permettant de choisir le filtre à appliquer.
     * Obligatoire, valeurs possibles : BLACK_WHITE ou GAUSSIAN.
     */
    @CommandLine.Option(
            names = {"-f", "--filter"},
            description = "Filtre à appliquer (valeurs possibles: ${COMPLETION-CANDIDATES})",
            required = true)
    protected AvailableFilter filter;
}
