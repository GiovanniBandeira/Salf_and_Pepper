package Question_1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args ) throws IOException {

        int ruidoNivel = 20;
        Random numRand = new Random();

        for (int i = 22903; i < 23326; i++){
            String img = "src/DefaltImage/Clean/";
            String file = i + ".jpg";
            img += file;

            boolean exists = (new File(img)).exists();
            if (exists){
                BufferedImage image = ImageIO.read(new File(img));
                int w = image.getWidth(); int h = image.getHeight();
                BufferedImage NewImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

                for (int lin = 0; lin < w; lin++) {
                    for (int col = 0; col < h; col++) {
                        int num = numRand.nextInt(0, 100);
                        if (num < ruidoNivel){
                            int num1 = numRand.nextInt(0, 2);
                            if (num1 == 1){
                                image.setRGB(lin, col, new Color(255,255,255).getRGB());
                            }else {
                                image.setRGB(lin, col, new Color(0,0,0).getRGB());
                            }
                        }
                        NewImage.setRGB(lin, col, new Color(image.getRGB(lin, col)).getRGB());
                    }
                }
                ImageIO.write(NewImage,  "jpg", new File("src/NewImage/Dirty/" + file));
            }
            System.out.printf("Terminou\n");
        }

    }
}



