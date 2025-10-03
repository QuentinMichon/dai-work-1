/**
 * @file : Main.java
 * @author : Quentin Michon, Gianni BEE
 * @date : 2025-09-26
 * @since : 2.0
 *
 * Description:
 *   Point d’entrée du programme.
 *   Lance la commande principale CLI (Root).
 */

package ch.heigvd;

import ch.heigvd.commands.Root;
import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        new CommandLine(new Root()).execute(args);
    }
}