package ch.heigvd.image;

/**
 * Représente une grille (matrice) de pixels.
 * Fournit des méthodes pour appliquer différents filtres ou transformations
 * sur l’image : noir et blanc, flou gaussien, rotation à 90°.
 */
public class PixelGrid {
    private final int width;
    private final int height;
    private final Pixel[][] pixels;

    // Noyau 3x3 utilisé pour le filtre gaussien
    private final int[] kernel = {1,2,1,2,4,2,1,2,1};
    // Décalages en Y pour parcourir les 9 voisins
    private final int[] yShift = {1,1,1,0,0,0,-1,-1,-1};
    // Décalages en X pour parcourir les 9 voisins
    private final int[] xShift = {-1,0,1,-1,0,1,-1,0,1};

    /**
     * Construit une grille de pixels vide.
     *
     * @param height hauteur (nombre de lignes)
     * @param width  largeur (nombre de colonnes)
     */
    public PixelGrid(int height, int width) {
        if(height < 0 || width < 0) {
            throw new IllegalArgumentException("Argument invalid pour height ou width : valeur négative impossible");
        }
        this.width = width;
        this.height = height;
        pixels = new Pixel[height][width];
    }

    /**
     * Définit un pixel à une position donnée.
     *
     * @param height indice de ligne
     * @param width  indice de colonne
     * @param pixel  pixel à placer
     * @throws IndexOutOfBoundsException si la position est en dehors de la grille
     */
    public void setPixel(int height, int width, Pixel pixel) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        pixels[height][width] = pixel;
    }

    /**
     * Récupère le pixel à une position donnée.
     *
     * @param height indice de ligne
     * @param width  indice de colonne
     * @return le pixel à la position donnée
     * @throws IndexOutOfBoundsException si la position est en dehors de la grille
     */
    public Pixel getPixel(int height, int width) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        return pixels[height][width];
    }

    /**
     * Applique un filtre noir et blanc (grayscale) à toute l’image.
     *
     * @return une nouvelle grille contenant la version en noir et blanc
     */
    public PixelGrid appliesBlackWhiteFilter() {
        PixelGrid newPixelGrid = new PixelGrid(height, width);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(_h, _w, pixels[_h][_w].getBWPixel());
            }
        }
        return newPixelGrid;
    }

    /**
     * Applique un filtre gaussien (flou) sur l’image.
     * Utilise un noyau 3x3 pondéré.
     *
     * @return une nouvelle grille contenant l’image floutée
     */
    public PixelGrid appliesGaussianFilter() {
        PixelGrid newPixelGrid = new PixelGrid(height, width);

        int rSomme;
        int gSomme;
        int bSomme;
        int weight;

        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                rSomme = 0;
                gSomme = 0;
                bSomme = 0;
                weight = 0;

                for (int i = 0; i < 9; i++) {
                    int xIndex = _w + xShift[i];
                    int yIndex = _h + yShift[i];
                    if(xIndex >= 0 && xIndex < width && yIndex >= 0 && yIndex < height) {
                        byte[] rgb = this.pixels[yIndex][xIndex].getRGB();
                        rSomme += (rgb[2] & 0xFF) * kernel[i];
                        gSomme += (rgb[1] & 0xFF) * kernel[i];
                        bSomme += (rgb[0] & 0xFF) * kernel[i];
                        weight += kernel[i];
                    }
                }

                newPixelGrid.setPixel(
                        _h, _w,
                        new Pixel((byte) (rSomme/weight),
                                (byte) (gSomme/weight),
                                (byte) (bSomme/weight))
                );
            }
        }
        return newPixelGrid;
    }

    /**
     * Effectue une rotation de l’image de 90° (sens horaire).
     *
     * @return une nouvelle grille contenant l’image pivotée
     */
    public PixelGrid applies90DegreeRotation() {
        PixelGrid newPixelGrid = new PixelGrid(width, height);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(width-_w-1, _h, pixels[_h][_w].copy());
            }
        }
        return newPixelGrid;
    }

    /**
     * Effectue une rotation de l’image de 180° (sens horaire).
     *
     * @return une nouvelle grille contenant l’image pivotée
     */
    public PixelGrid applies180DegreeRotation() {
        PixelGrid newPixelGrid = new PixelGrid(height, width);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(height-_h-1, width-_w-1, pixels[_h][_w].copy());
            }
        }
        return newPixelGrid;
    }

    /**
     * Effectue une rotation de l’image de 270° (sens horaire).
     *
     * @return une nouvelle grille contenant l’image pivotée
     */
    public PixelGrid applies270DegreeRotation() {
        PixelGrid newPixelGrid = new PixelGrid(width, height);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(_w, height-_h-1, pixels[_h][_w].copy());
            }
        }
        return newPixelGrid;
    }
}
