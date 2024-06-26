package com.tommycondon.ca1;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class Controller {
    public ImageView imageView = new ImageView();
    public ImageView blackAndWhiteView = new ImageView();
    public Image originalImageUploaded;
    public HashSet<Integer> roots = new HashSet<>();
    public TextField numSelected;
    public TextField hueSlider = new TextField();
    public ImageView colorView = new ImageView();
    public TextField noiseAdjust = new TextField();
    public ColorAdjust colorAdjust = new ColorAdjust();
    int[] imageArray;// array that stores each pixel in the unprocessed image
    public ListView<Pill> listView = new ListView();
    public ArrayList<Pill> pills = new ArrayList<>();

    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(Driver.primaryStage);
        if(file != null){
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            this.imageArray = new int[(int) image.getWidth()*(int) image.getHeight()];
            this.originalImageUploaded = image; // For things like resetting black and white view etc.
            System.out.println("Image Dimensions --> " + Utilities.getImageDimensions(imageView.getImage()));
            //noiseSliderObj.setShowTickLabels(true);
            //noiseSliderObj.setShowTickMarks(true);
        }
    }

    public int numberOfOccurencesOfoot(int root){
        int counter = 0;
        for(int val : imageArray){
            if (val==root) counter++;
        }
        return counter;
    }

    public void blackAndWhiteConversion(MouseEvent actionEvent) {
        listView.getItems().clear();

        // clear rectangles before next selection
        AnchorPane anchorPane = (AnchorPane) imageView.getParent();
        anchorPane.getChildren().removeIf(component -> component instanceof Rectangle);
        anchorPane.getChildren().removeIf(component -> component instanceof Label);
        anchorPane.getChildren().removeIf(component -> component instanceof Text);

        Image image = imageView.getImage();

        // Colour of pixel at the mouse click point
        Color colorOfClickedPixel = image.getPixelReader().getColor((int) actionEvent.getX(),(int) actionEvent.getY());
        PixelReader pixelReader = image.getPixelReader();

        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        WritableImage colorWritable = new WritableImage((int) image.getWidth(), (int) image.getHeight());

        PixelWriter pixelWriterColor = colorWritable.getPixelWriter();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int ycoord = 0; ycoord < image.getHeight(); ycoord++)
        {

            for (int xcoord = 0; xcoord < image.getWidth(); xcoord++)
            {
                Color colorOfPixel = pixelReader.getColor(xcoord, ycoord);

                if(areSimilar(colorOfPixel,colorOfClickedPixel)) {
                    pixelWriter.setColor(xcoord,ycoord,Color.WHITE);
                    pixelWriterColor.setColor(xcoord,ycoord,colorOfClickedPixel);
                    imageArray[xcoord + (ycoord*(int)image.getWidth())] = xcoord + (ycoord*(int)image.getWidth());
                }
                else {
                    pixelWriter.setColor(xcoord,ycoord,Color.BLACK);
                    pixelWriterColor.setColor(xcoord,ycoord,Color.BLACK);
                    imageArray[xcoord + (ycoord*(int)image.getWidth())] = -1; // Black pixels = -1
                }

            }

        }
        blackAndWhiteView.setImage(writableImage);
        colorView.setImage(colorWritable);

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

        // Initialize HashSet with root Values

        HashSet<Integer> diffValues = new HashSet<>();

        for (int i = 0; i < imageArray.length; i++){
            diffValues.add(UnionAndFind.find(imageArray,i));
        }
        this.roots = diffValues;

        //////////////////////////////////////////////////

        /* Mapping out Rectangle space */

        int leftx = 0;
        int lefty = 0;
        int width = 100;
        int height = 65;

        ArrayList<XYPoint> points = new ArrayList<>(); // storing x,y coords
        LinkedList<Integer> tempRoots = new LinkedList<>(); // Create a temporary list to check if root is already given rectangle
        LinkedList<Rectangle> rectanglesList = new LinkedList<>();


        for(int root : roots) {

            for (int i = 0; i < imageArray.length; i++)
            {
                if (imageArray[i] != -1)
                {
                    int foundRoot = UnionAndFind.find(imageArray,i);

                    if(foundRoot == root && !tempRoots.contains(root)){
                        /* Source: https://stackoverflow.com/questions/47951361/finding-x-y-based-off-of-index
                         * Converting index to X & Y coords
                         * */
                        leftx = i % (int) image.getWidth()-40;
                        lefty = i / (int) image.getWidth();

                        XYPoint point = new XYPoint(leftx,lefty);

                        points.add(point);
                        tempRoots.add(root);

                        Rectangle rectangle = new Rectangle(leftx,lefty,width,height);
                        drawRectangles(rectangle,rectanglesList);
                        double area = width*height;
                        System.out.println("Area of pill: " + area);
                        addPill(area,point,root,rectangle);
                    }

                }

            }

        }

        // Create label above pill, ordered fashion e.g. from top to bottom 1,2,3,4 etc
        //Collections.sort(tempRoots);

        String num = ""+rectanglesList.size();
        numSelected.setPromptText(num);
        System.out.println("Temp Roots:" + tempRoots);
        onscreenLabelling(tempRoots,rectanglesList);
        for(int value : tempRoots){
            System.out.println("Size of (" + value + "): "+ numberOfOccurencesOfoot(value) + "Pixels" + "\n");
        }
        //////////////////////////////////////////////////
        //////////////////////////////////////////////////

    }

    public boolean addPill(double area, XYPoint point, int rootval, Rectangle rectangle) {
        Pill newPill = new Pill(area,point,rootval,rectangle);
        listView.getItems().add(newPill);
        return pills.add(newPill);
    }

    private void onscreenLabelling(LinkedList<Integer> tempRoots, LinkedList<Rectangle> rectanglesList) {
        int id = 0;
        while(id < tempRoots.size()){
            Tooltip tooltip = new Tooltip("Pill");
            tooltip.setText("Pill" + id +"\n"+
                    "Size of pill: "+numberOfOccurencesOfoot(tempRoots.get(id))+ " pixel units" + "\n"
            );
            Tooltip.install(rectanglesList.get(id), tooltip);
            id++;
        }
    }

    public void drawRectangles(Rectangle rect, LinkedList<Rectangle> rectanglesList) {
        rect.setLayoutX(imageView.getLayoutX());
        rect.setLayoutY(imageView.getLayoutY());
        rect.setStroke(Color.DARKMAGENTA);
        rect.setStrokeWidth(3);
        rect.setFill(Color.TRANSPARENT);

        rectanglesList.add(rect);

        System.out.print("Added rectangle at [" + (int)rect.getX() + ", " + (int)rect.getY() + "]"+ "\n");

        // adding to anchor pane view
        AnchorPane ap = (AnchorPane) imageView.getParent();
        ap.getChildren().add(rect);
        // temp solution for size of selected pills
        //System.out.println("Size of selected pill: " + (rectanglesList.get(0).getWidth()*rectanglesList.get(0).getHeight()) +" pixels");

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

    public void displayImageArrayAsText(int[] ia){
        //System.out.println("Number of pills selected: " + numberOfPills());
        System.out.println("The DS Array\n--------------");
        for(int i=0;i<ia.length;i++)
            System.out.print(UnionAndFind.find(ia,i) + ((i+1)%imageView.getImage().getWidth()==0 ? "\n" : " "));
    }

    public int numberOfPills(){
        System.out.println("Number of pills: "+ (roots.size()-1)); // Excluding "-1" from hashset
        return (roots.size()-1);
        // HashSet values are now the root values so can easily access them for later stages
    }


    public void changeHue(ActionEvent mouseEvent) {
        double hueVal = Double.parseDouble(hueSlider.getText());
        colorAdjust.setHue(hueVal);
        imageView.setEffect(colorAdjust);
    }

    public void reset(){
        listView.getItems().removeAll(pills);
        numSelected.setPromptText(String.valueOf(0));
        AnchorPane anchorPane = (AnchorPane) imageView.getParent();
        anchorPane.getChildren().removeIf(component -> component instanceof Rectangle);
        anchorPane.getChildren().removeIf(component -> component instanceof Label);
        anchorPane.getChildren().removeIf(component -> component instanceof Text);
        imageView.setEffect(null);
    }

    private boolean checkTwoTone(Color colorOfPixel) {
        boolean twotone1 = colorOfPixel.getRed()==1.0 && colorOfPixel.getGreen()==0.5137255191802979 && colorOfPixel.getBlue()==0.0;
        boolean twotone2 = colorOfPixel.getRed()==0.0941176488995552 && colorOfPixel.getGreen()==0.0941176488995552 && colorOfPixel.getBlue()==0.0941176488995552;
        return twotone2 || twotone1;
    }
}

