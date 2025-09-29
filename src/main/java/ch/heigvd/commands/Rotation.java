package ch.heigvd.commands;

import ch.heigvd.image.BMP;
import picocli.CommandLine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@CommandLine.Command(name = "rotation", description = "Applique une rotation à l'image")
public class Rotation implements Runnable {
    @CommandLine.ParentCommand protected Root parent;

    @Override
    public void run() {
        try {
            FileInputStream fis = new FileInputStream("IOFile/" + parent.source);
            BufferedInputStream bis = new BufferedInputStream(fis);
            BMP bmp = new BMP(bis);
            // traitement sur l'image
            //----------------------------------------------
            switch (rotation) {
                case ROTATE_90:
                    bmp.applies90DegreeRotation();
                    break;
                case ROTATE_180:
                    bmp.applies90DegreeRotation();
                    bmp.applies90DegreeRotation();
                    break;
                case ROTATE_270:
                    bmp.applies90DegreeRotation();
                    bmp.applies90DegreeRotation();
                    bmp.applies90DegreeRotation();
                    break;
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

    public enum AvailableRotation {
        ROTATE_90,
        ROTATE_180,
        ROTATE_270,
    }

    @CommandLine.Option(
            names = {"-r", "--rotation"},
            description = "Rotation (horaire) à appliquer (valeurs possibles: ${COMPLETION-CANDIDATES})",
            required = true)
    protected AvailableRotation rotation;
}
