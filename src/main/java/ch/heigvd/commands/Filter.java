/**
 * @file : Filter.java
 * @author : Quentin Michon, Gianni BEE
 * @date : 2025-09-29
 * @since : 2.0
 *
 * Description:
 *   Sous-commande CLI pour appliquer des filtres
 *   (noir et blanc, flou gaussien) sur une image BMP.
 */

package ch.heigvd.commands;

import ch.heigvd.image.BMP;
import picocli.CommandLine;
import java.io.*;
import java.util.function.Consumer;

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
        System.out.println("Application d'un filtre sur fichier source...");

        parent.openAndAppliesModifier(appliesFilter);
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

    private void appliesFilter(BMP bmp) {
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
    }

    private final Consumer<BMP> appliesFilter = this::appliesFilter;
}
