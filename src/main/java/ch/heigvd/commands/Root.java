package ch.heigvd.commands;


import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@CommandLine.Command(
        description = "Un CLI qui permet de faire du traitement d'image au format BMP v3 24bits",
        version = "1.0.0",
        subcommands = {Filter.class, Rotation.class},
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)

public class Root implements Runnable {
    @Override
    public void run() {
        System.out.println("Duplication du fichier source\n");

        try {
            FileInputStream fis =  new FileInputStream("IOFile/" + source);
            BufferedInputStream bis = new BufferedInputStream(fis);

            BMP bmp = new BMP(bis);

            FileOutputStream fos = new FileOutputStream("IOFile/" + destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            bmp.writeBMP(bos);

            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CommandLine.Parameters(index = "0", description = "Nom du fichier source.")
    protected String source;

    @CommandLine.Parameters(index = "1", description = "Nom du nouveau fichier.")
    protected String destination;


}
