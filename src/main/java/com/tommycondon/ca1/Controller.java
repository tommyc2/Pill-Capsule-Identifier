package com.tommycondon.ca1;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.HashSet;

public class Controller {
    public ImageView imageView = new ImageView();
    public ImageView blackAndWhiteView = new ImageView();
    public Image originalImageUploaded;
    public TextField descriptionOfPixel;
    public HashSet<Integer> roots = new HashSet<>();
    int[] imageArray;// array that stores each pixel in the unprocessed image

    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Driver.primaryStage);
        if(file != null){
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            this.imageArray = new int[(int) image.getWidth()*(int) image.getHeight()];
            this.originalImageUploaded = image; // For things like resetting black and white view etc.
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
                    imageArray[xcoord + (ycoord*(int)image.getWidth())] = xcoord + (ycoord*(int)image.getWidth()); // white pixels = 0
                }
                else {
                    pixelWriter.setColor(xcoord,ycoord,Color.BLACK);
                    imageArray[xcoord + (ycoord*(int)image.getWidth())] = -1; // Black pixels = -1
                }

            }

        }
        blackAndWhiteView.setImage(writableImage); // Setting image to grayscale

        //displayDSAsText(imageArray);
        // formation of pills using union and find
        // image here refers to unprocessed Image
        for (int y = 0; y < image.getHeight(); y++)
        {
            for (int x = 0; x < image.getWidth(); x++)
            {
                int indexOfPixel = y * (int) image.getWidth() + x;

                if(imageArray[indexOfPixel] >= 0) {
                    // If equal to zero, check right + bottom and union them
                    int currentPixel = imageArray[indexOfPixel];
                    int rightIndex = indexOfPixel+1;
                    int belowIndex = indexOfPixel+((int)image.getWidth());

                    if((rightIndex < imageArray.length) && (imageArray[rightIndex] != -1)){
                        UnionAndFind.union(imageArray,currentPixel,imageArray[rightIndex]);
                    }
                    if((belowIndex < imageArray.length) && (imageArray[belowIndex] != -1)){
                        UnionAndFind.union(imageArray,currentPixel,imageArray[belowIndex]);
                    }
                }

            }
        }
        numberOfPills(imageArray);
        System.out.println(sizeOfSelectedPill(imageArray,(int) actionEvent.getX(),(int) actionEvent.getY()) + "pixel units");
        //displayDSAsText(imageArray);
        //drawRectangles();
    }

    private void drawRectangles() {
        //TODO --> DrawRectangles
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

    public void resetToDefaultImage(ActionEvent actionEvent) {
        imageView.setEffect(null);
        imageView.setImage(this.originalImageUploaded);
    }

    public void displayDSAsText(int[] ia){
        //System.out.println("Number of pills selected: " + numberOfPills());
        System.out.println("The DS Array\n--------------");
        for(int i=0;i<ia.length;i++)
            System.out.print(UnionAndFind.find(ia,i) + ((i+1)%imageView.getImage().getWidth()==0 ? "\n" : " "));
    }

    public void numberOfPills(int[] ia){
        HashSet<Integer> diffValues = new HashSet<>();
        for (int i = 0; i < ia.length; i++){
            diffValues.add(UnionAndFind.find(ia,i));
        }
        this.roots = diffValues;
        System.out.println("Number of pills selected: "+ (roots.size()-1)); // Excluding "-1" from hashset
        // HashSet values are now the root values so can easily access them for later stages
    }

    public int sizeOfSelectedPill(int[] ia, int x, int y){
        int indexOfPixel = y * (int) imageView.getImage().getWidth() + x;
        int counter = 0;
            for(int i = 0; i< ia.length; i++){
                if(ia[i] == ia[indexOfPixel]) counter++;
            }
            return counter;
        }

}