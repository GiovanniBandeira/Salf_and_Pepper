package Question_2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {
    public static void main(String[] args ) {

        Path FolderImages = Paths.get("src/DefaltImage/Noise");
        Path FolderNewImages3 = Paths.get("src/NewImage/Clean/M3x3");
        Path FolderNewImages5 = Paths.get("src/NewImage/Clean/M5x5");
        Path FolderNewImages7 = Paths.get("src/NewImage/Clean/M7x7");
        Path FolderNewImages9 = Paths.get("src/NewImage/Clean/M9x9");

        try {
            Files.walkFileTree(FolderImages, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    processImage(file, FolderNewImages3,FolderNewImages5, FolderNewImages7, FolderNewImages9);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processImage(Path imagePath, Path NewImagePath3, Path NewImagePath5, Path NewImagePath7, Path NewImagePath9) throws IOException {

        BufferedImage img = ImageIO.read(new File(String.valueOf(imagePath)));
        int w = img.getWidth();
        int h = img.getHeight();

        String imageName = imagePath.getFileName().toString();
        String imageBaseName = imageName.substring(0, imageName.lastIndexOf('.'));

        // Matriz 3x3
        double[][] Filter3x3 = {
                {0.0625, 0.125, 0.0625},
                {0.125,   0.25,  0.125},
                {0.0625, 0.125, 0.0625}
        };

        // Matriz 5x5
        double[][] Filter5x5 = {
                {0.02778, 0.05556, 0.02778, 0.05556, 0.02778},
                {0.02778, 0.0,     0.11111, 0.0,     0.02778},
                {0.02778, 0.11111, 0.11111, 0.11111, 0.02778},
                {0.05556, 0.0,     0.11111, 0.0,     0.05556},
                {0.02778, 0.05556, 0.02778, 0.05556, 0.02778},
        };

        // Matriz 7x7
        double[][] Filter7x7 = {
                {0.0053, 0.0844, 0.0844, 0.0105, 0.0844, 0.0844, 0.0053},
                {0.0105, 0.0000, 0.0000, 0.0211, 0.0000, 0.0000, 0.0105},
                {0.0053, 0.0000, 0.0211, 0.0211, 0.0211, 0.0000, 0.0053},
                {0.0105, 0.0211, 0.0211, 0.0211, 0.0211, 0.0211, 0.0105},
                {0.0053, 0.0000, 0.0211, 0.0211, 0.0211, 0.0000, 0.0053},
                {0.0105, 0.0000, 0.0000, 0.0211, 0.0000, 0.0000, 0.0105},
                {0.0053, 0.0844, 0.0844, 0.0105, 0.0844, 0.0844, 0.0053}
        };

        // Matriz 9x9
        double[][] Filter9x9 = {
            {0.0068, 0.0135, 0.0068, 0.0135, 0.0068, 0.0135, 0.0068, 0.0135, 0.0068},
            {0.0135, 0.0000, 0.0000, 0.0000, 0.0270, 0.0000, 0.0000, 0.0000, 0.0135},
            {0.0068, 0.0000, 0.0000, 0.0270, 0.0270, 0.0270, 0.0000, 0.0000, 0.0068},
            {0.0135, 0.0000, 0.0270, 0.0270, 0.0270, 0.0270, 0.0270, 0.0000, 0.0135},
            {0.0068, 0.0270, 0.0270, 0.0270, 0.0270, 0.0270, 0.0270, 0.0270, 0.0068},
            {0.0135, 0.0000, 0.0270, 0.0270, 0.0270, 0.0270, 0.0270, 0.0000, 0.0135},
            {0.0068, 0.0000, 0.0000, 0.0270, 0.0270, 0.0270, 0.0000, 0.0000, 0.0068},
            {0.0135, 0.0000, 0.0000, 0.0000, 0.0270, 0.0000, 0.0000, 0.0000, 0.0135},
            {0.0068, 0.0135, 0.0068, 0.0135, 0.0068, 0.0135, 0.0068, 0.0135, 0.0068}
        };

        // Deviation Model
        int deviation3 = Filter3x3.length / 2;
        int deviation5 = Filter5x5.length / 2;
        int deviation7 = Filter7x7.length / 2;
        int deviation9 = Filter9x9.length / 2;

        BufferedImage newImage3 = applyFiltro(img, w, h, Filter3x3, deviation3);
        ImageIO.write(newImage3, "jpg", new File(NewImagePath3.resolve(imageBaseName + "_3x3.jpg").toString()));

        BufferedImage newImage5 = applyFiltro(img, w, h, Filter5x5, deviation5);
        ImageIO.write(newImage5, "jpg", new File(NewImagePath5.resolve(imageBaseName + "_5x5.jpg").toString()));

        BufferedImage newImage7 = applyFiltro(img, w, h, Filter7x7, deviation7);
        ImageIO.write(newImage7, "jpg", new File(NewImagePath7.resolve(imageBaseName + "_7x7.jpg").toString()));

        BufferedImage newImage9 = applyFiltro(img, w, h, Filter9x9, deviation9);
        ImageIO.write(newImage9, "jpg", new File(NewImagePath9.resolve(imageBaseName + "_9x9.jpg").toString()));
    }

    public static BufferedImage applyFiltro(BufferedImage img, int w, int h, double[][] Filter, int deviation) {
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int lin = deviation; lin < w - deviation; lin++) {
            for (int col = deviation; col < h - deviation; col++) {

                int vetorR = 0;
                int vetorG = 0;
                int vetorB = 0;

                for (int l = 0; l < Filter.length; l++) {
                    for (int c = 0; c < Filter.length; c++) {
                        Color pixel = new Color(img.getRGB(lin + l - deviation, col + c - deviation));
                        vetorR += Filter[l][c] * pixel.getRed();
                        vetorG += Filter[l][c] * pixel.getGreen();
                        vetorB += Filter[l][c] * pixel.getBlue();
                    }
                }

                vetorR = Math.min(Math.max(vetorR, 0), 255);
                vetorG = Math.min(Math.max(vetorG, 0), 255);
                vetorB = Math.min(Math.max(vetorB, 0), 255);

                newImage.setRGB(lin, col, new Color(vetorR, vetorG, vetorB).getRGB());
            }
        }

        return newImage;
    }

}
