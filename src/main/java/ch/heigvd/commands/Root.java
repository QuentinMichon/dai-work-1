package ch.heigvd.commands;


import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.*;

@CommandLine.Command(
        description = "Un CLI qui permet de faire du traitement d'image au format BMP v3 24bits",
        version = "1.0.0",
        subcommands = {Filter.class, Rotation.class},
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)

public class Root implements Runnable {
    @Override
    public void run() {
        System.out.println("Duplication du fichier source...");

        try(FileInputStream fis =  new FileInputStream("IOFile/" + source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream("IOFile/" + destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            BMP bmp = new BMP(bis);
            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image à bien été générée : IOFile/" + destination);

        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
        }
    }

    @CommandLine.Parameters(index = "0", description = "Nom du fichier source (.bmp).")
    protected String source;

    @CommandLine.Parameters(index = "1", description = "Nom du nouveau fichier (.bmp).")
    protected String destination;


}
