package com.tommycondon.ca1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;

public class Controller {
    public ImageView imageView;
    public Image img;

    public Slider sliderObj = new Slider(0,255,255);

    public TextField saturationValue;

    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Driver.primaryStage);

        if(file != null){
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            this.img = image;
        }

        sliderObj.setShowTickLabels(true);
        sliderObj.setShowTickLabels(true);
        sliderObj.setValue(255);
    }

    public void setToGrayScale(ActionEvent actionEvent) {
        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int ycoord = 0; ycoord < image.getHeight(); ycoord++)
        {

            for (int xcoord = 0; xcoord < image.getWidth(); xcoord++)
            {
                Color color = pixelReader.getColor(xcoord, ycoord);
                double averageValues = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                pixelWriter.setColor(xcoord, ycoord, new Color(averageValues, averageValues, averageValues, color.getOpacity()));
            }

        }

        imageView.setImage(writableImage); // Setting image to grayscale
    }

    public void setToRGB(ActionEvent actionEvent) {
        imageView.setImage(img);
    }

    public void adjustOpacity(MouseEvent actionEvent) {

        Image image = imageView.getImage();
        PixelReader pixelReader = image.getPixelReader();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int ycoord = 0; ycoord < image.getHeight(); ycoord++)
        {

            for (int xcoord = 0; xcoord < image.getWidth(); xcoord++)
            {
                Color color = pixelReader.getColor(xcoord, ycoord);
                double opacity = sliderObj.valueProperty().getValue();
                pixelWriter.setColor(xcoord, ycoord, new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity));
            }

        }

        imageView.setImage(writableImage);
    }

    public void submitSaturation(ActionEvent actionEvent) {
        double sat = Double.parseDouble(saturationValue.getText());
        double newSat = sat/100;
        //saturationObj = new ColorAdjust();
        //saturationObj.setSaturation(newSat);
        //imageView.setEffect(saturationObj);
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