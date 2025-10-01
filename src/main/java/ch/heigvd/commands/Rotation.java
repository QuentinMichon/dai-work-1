package ch.heigvd.commands;

import ch.heigvd.AvailableRotation;
import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.*;

@CommandLine.Command(name = "rotation", description = "Applique une rotation à l'image")
public class Rotation implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    @Override
    public void run() {
        try(FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream("IOFile/" + parent.destination);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            System.out.println("Ouverture du fichier source...");

            BMP bmp = new BMP(bis);
            // traitement sur l'image
            //----------------------------------------------
            bmp.appliesRotation(rotation);
            //----------------------------------------------

            bmp.writeBMP(bos);

            System.out.println("Votre nouvelle image à bien été générée : IOFile/" + parent.destination);

            bos.flush();
            bos.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("Erreur durant l'ouverture ou l'écriture du fichier : " + e);
        }
    }

    @CommandLine.Option(
            names = {"-r", "--rotation"},
            description = "Rotation (horaire) à appliquer (valeurs possibles: ${COMPLETION-CANDIDATES})",
            required = true)
    protected AvailableRotation rotation;
}
