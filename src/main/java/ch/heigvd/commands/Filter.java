package ch.heigvd.commands;


import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@CommandLine.Command(name = "filter", description = "Applique un filtre à l'image")
public class Filter implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    @Override
    public void run() {

        try {
            FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BMP bmp = new BMP(bis);

            // traitement sur l'image
            //----------------------------------------------
            switch (filter) {
                case GAUSSIAN -> {
                    System.out.println("Application du filtre gaussien...");
                    bmp.appliesGaussianFilter();
                }
                case BLACK_WHITE -> {
                    System.out.println("Application du filtre noir blanc...");
                    bmp.appliesBlackWhiteFilter();
                }
            }
            //----------------------------------------------
            FileOutputStream fos = new FileOutputStream("IOFile/" + parent.destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image à bien été générée : IOFile/" + parent.destination);

            bos.flush();
            bos.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum AvailableFilter {
        BLACK_WHITE,
        GAUSSIAN,
    }

    @CommandLine.Option(
            names = {"-f", "--filter"},
            description = "Filtre à appliquer (valeurs possibles: ${COMPLETION-CANDIDATES})",
            required = true)
    protected AvailableFilter filter;
}
