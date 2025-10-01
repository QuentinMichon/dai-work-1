package ch.heigvd.image;

public class PixelGrid {
    private final int width;
    private final int height;
    private final Pixel[][] pixels;

    final private int[] kernel = {1,2,1,2,4,2,1,2,1};
    final private int[] yShift = {1,1,1,0,0,0,-1,-1,-1};
    final private int[] xShift = {-1,0,1,-1,0,1,-1,0,1};

    public PixelGrid(int height, int width) {
        this.width = width;
        this.height = height;
        pixels = new Pixel[height][width];
    }

    public void setPixel(int height, int width, Pixel pixel) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        pixels[height][width] = pixel;
    }

    public Pixel getPixel(int height, int width) {
        if (height < 0 || height >= this.height || width < 0 || width >= this.width) {
            throw new IndexOutOfBoundsException();
        }
        return pixels[height][width];
    }

    public PixelGrid appliesBlackWhiteFilter() {
        PixelGrid newPixelGrid = new PixelGrid(height, width);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(_h, _w, pixels[_h][_w].getBWPixel());
            }
        }
        return newPixelGrid;
    }

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

                newPixelGrid.setPixel(_h, _w, new Pixel((byte) (rSomme/weight), (byte) (gSomme/weight), (byte) (bSomme/weight)));
            }
        }
        return newPixelGrid;
    }

    public PixelGrid applies90DegreeRotation() {
        PixelGrid newPixelGrid = new PixelGrid(width, height);
        for(int _h = 0; _h < height; _h++) {
            for(int _w = 0; _w < width; _w++) {
                newPixelGrid.setPixel(width-_w-1, _h, pixels[_h][_w].copy());
            }
        }
        return newPixelGrid;
    }
}
