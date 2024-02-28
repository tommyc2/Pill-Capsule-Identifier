package com.tommycondon.ca1;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
                    imageArray[xcoord + (ycoord*(int)image.getWidth())] = xcoord + (ycoord*(int)image.getWidth());
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

        System.out.println(sizeOfSelectedPill(imageArray,(int) actionEvent.getX(),(int) actionEvent.getY()) + "pixel units");

        // Initialize HashSet with root Values

        HashSet<Integer> diffValues = new HashSet<>();

        for (int i = 0; i < imageArray.length; i++){
            diffValues.add(UnionAndFind.find(imageArray,i));
        }
        this.roots = diffValues;


        //////////////////////////////////////////////////

        /* Mapping out Rectangle space */

        int leftx = (int) image.getWidth();
        int lefty = (int) image.getWidth();
        int width = 50;
        int height = 50;

        for(int root : roots) {

         for (int i = 0; i < imageArray.length; i++)
         {
             if (imageArray[i] != -1)
             {
                 int foundRoot = UnionAndFind.find(imageArray,i);

                 if(foundRoot == root){

                 }

             }
         }

        }

        drawRectangles(imageArray,width,height,leftx,lefty);

        //////////////////////////////////////////////////
        //////////////////////////////////////////////////

    }

    private void drawRectangles(int[] ia,int width, int height, int topleftx, int toplefty) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        rectangle.setStroke(Color.DARKMAGENTA);
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setStrokeWidth(4);
        rectangle.setFill(Color.TRANSPARENT);


    }


    /* Check if colour is similar to another pixel's colour */
    public boolean areSimilar(Color color, Color color1){
        boolean blue = (Math.abs(color.getBlue() - color1.getBlue()) <= 0.1);
        boolean green = (Math.abs(color.getGreen() - color1.getGreen()) <= 0.1);
        boolean red = (Math.abs(color.getRed() - color1.getRed()) <= 0.1);
        boolean hue = (Math.abs(color.getHue() - color1.getHue()) < 0.1);
        boolean sat = (Math.abs(color.getSaturation() - color1.getSaturation()) < 0.1);
        boolean brightness = (Math.abs(color.getBrightness() - color1.getBrightness()) < 0.1);
       // return (red && blue && green);
        return sat && brightness && hue && green && red && blue;
    }

    public void displayDSAsText(int[] ia){
        //System.out.println("Number of pills selected: " + numberOfPills());
        System.out.println("The DS Array\n--------------");
        for(int i=0;i<ia.length;i++)
            System.out.print(UnionAndFind.find(ia,i) + ((i+1)%imageView.getImage().getWidth()==0 ? "\n" : " "));
    }

    public void numberOfPills(int[] ia){
        System.out.println("Number of pills selected: "+ (roots.size()-1)); // Excluding "-1" from hashset
        // HashSet values are now the root values so can easily access them for later stages
    }

    public int sizeOfSelectedPill(int[] ia, int x, int y){
        int indexOfPixel = y * (int) imageView.getImage().getWidth() + x;
        int counter = 0;

            for(int i = 0; i < ia.length; i++)
            {
                if(ia[i] == ia[indexOfPixel])
                {
                    counter++;
                }
            }
            return counter;
        }

}