package ch.heigvd.image;

/**
 * Représente un pixel en couleur RVB (rouge, vert, bleu).
 * Chaque composante est stockée sur un octet (valeur entre 0 et 255).
 */
public class Pixel {
    private byte r;
    private byte g;
    private byte b;

    /**
     * Construit un pixel avec ses trois composantes.
     *
     * @param r valeur du rouge (0–255)
     * @param g valeur du vert (0–255)
     * @param b valeur du bleu (0–255)
     */
    public Pixel(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Crée une copie du pixel courant.
     *
     * @return un nouveau pixel identique à celui-ci
     */
    public Pixel copy() {
        return new Pixel(r, g, b);
    }

    /**
     * Retourne les composantes du pixel sous forme d'un tableau d'octets.
     * L'ordre est : bleu, vert, rouge (BGR).
     *
     * @return un tableau contenant {b, g, r}
     */
    public byte[] getRGB() {
        return new byte[]{this.b, this.g, this.r};
    }

    /**
     * Génère une version en niveaux de gris (noir et blanc) du pixel.
     * La conversion utilise la formule de luminosité standard :
     * gray = 0.299 * R + 0.587 * G + 0.114 * B
     *
     * @return un nouveau pixel dont les trois composantes sont égales au niveau de gris calculé
     */
    public Pixel getBWPixel() {
        int intR = this.r & 0xFF;
        int intG = this.g & 0xFF;
        int intB = this.b & 0xFF;

        byte gray = (byte)(Math.floor((double)(0.299F*intR + 0.587F*intG + 0.114F*intB)));
        return new Pixel(gray, gray, gray);
    }
}
