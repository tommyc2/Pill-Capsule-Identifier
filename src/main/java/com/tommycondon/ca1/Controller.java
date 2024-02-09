package com.tommycondon.ca1;

import javafx.event.ActionEvent;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    public ImageView imageView = new ImageView();
    public ImageView blackAndWhiteView = new ImageView();

    int[][] imageArray;// 2D array that stores each pixel in the image

    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Driver.primaryStage);
        if(file != null){
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageArray = new int[(int) image.getWidth()][(int) image.getHeight()]; // 2D array to store pixel values
            System.out.println("Image Dimensions --> " + Utilities.getImageDimensions(imageView.getImage()));
        }
    }

    public void blackAndWhiteConversion(MouseEvent actionEvent) {
        Image image = imageView.getImage();

        // Colour of pixel at the mouse click point
        Color colorOfClickedPixel = image.getPixelReader().getColor((int) actionEvent.getX(),(int) actionEvent.getY());
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int ycoord = 0; ycoord < image.getHeight(); ycoord++)
        {

            for (int xcoord = 0; xcoord < image.getWidth(); xcoord++)
            {
                Color colorOfPixel = pixelReader.getColor(xcoord, ycoord);

                if(areSimilar(colorOfPixel,colorOfClickedPixel)) {
                    pixelWriter.setColor(xcoord,ycoord,Color.WHITE);
                    imageArray[xcoord][ycoord] = 0; // white pixels = 0
                }
                else {
                    pixelWriter.setColor(xcoord,ycoord,Color.BLACK);
                    imageArray[xcoord][ycoord] = -1; // Black pixels = -1
                }

            }

        }

        blackAndWhiteView.setImage(writableImage); // Setting image to grayscale
    }

    /* Check if colour is similar to another pixel's colour */
    public boolean areSimilar(Color color, Color color1){
        boolean blue = (Math.abs(color.getBlue() - color1.getBlue()) <= 0.065);
        boolean green = (Math.abs(color.getGreen() - color1.getGreen()) <= 0.065);
        boolean red = (Math.abs(color.getRed() - color1.getRed()) <= 0.065);
        boolean saturation = (Math.abs(color.getSaturation() - color1.getSaturation()) <= 0.065);
        boolean brightness = (Math.abs(color.getBrightness()-color1.getBrightness()) <= 0.065);

        return (red && blue && green && saturation && brightness);
    }

    public void redChannelEvent(ActionEvent actionEvent) {
        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, new Color(pixel.getRed(), 0, 0, pixel.getOpacity()));
            }
        }
        imageView.setImage(writableImage);
    }

    public void blueChannelEvent(ActionEvent actionEvent) {
        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, new Color(0, 0, pixel.getBlue(), pixel.getOpacity()));
            }
        }
        imageView.setImage(writableImage);
    }

    public void greenChannelEvent(ActionEvent actionEvent) {
        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, new Color(0, pixel.getGreen(), 0, pixel.getOpacity()));
            }
        }
        imageView.setImage(writableImage);
    }

    public void resetToDefaultSettings(ActionEvent actionEvent) {
        imageView.setEffect(null);
    }
}