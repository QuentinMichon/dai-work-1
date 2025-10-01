package ch.heigvd.commands;


import ch.heigvd.image.BMP;
import picocli.CommandLine;
import java.io.*;

@CommandLine.Command(name = "filter", description = "Applique un filtre à l'image")
public class Filter implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    @Override
    public void run() {
        System.out.println("Ouverture du fichier source...");

        try(FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream("IOFile/" + parent.destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {

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

            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image à bien été générée : IOFile/" + parent.destination);

            bos.flush();
            bos.close();
            bis.close();
        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
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
