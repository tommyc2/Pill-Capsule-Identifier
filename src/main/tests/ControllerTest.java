import com.tommycondon.ca1.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField.*;

public class ControllerTest {

    private Controller controllerObj = new Controller();
    private ImageView imageView = new ImageView();
    private LinkedList<Rectangle> rectangles = new LinkedList<>();

    @BeforeEach
    void setUp() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/main/tests/test-image-pills.png"));
        imageView.setImage(image);
    }
    @Test
    void addImage(){
        assertNotNull(imageView.getImage());
    }

    @Test
    void testColorSimilarityForPixels(){
        assertTrue(controllerObj.areSimilar(Color.BLACK, Color.BLACK));
        assertTrue(controllerObj.areSimilar(Color.WHITE, Color.WHITE));
        assertFalse(controllerObj.areSimilar(Color.RED, Color.PURPLE));
        assertFalse(controllerObj.areSimilar(Color.BLUE, Color.BROWN));
    }

    @Test
    void addRectangle(){
        Rectangle rectangle = new Rectangle(50,50,50,50);
        rectangles.add(rectangle);
        assertTrue(rectangles.contains(rectangle));
        assertTrue(rectangles.getFirst().getX() == 50);
        assertTrue(rectangles.getFirst().getY() == 50);
    }

  @Test
    void numOfPills(){
        HashSet<Integer> roots = controllerObj.roots;
        roots.add(50);
        roots.add(100);
        roots.add(200);
        roots.add(303);
      assertEquals(3, controllerObj.numberOfPills());
  }

  @Test
    void testingFind(){
      int[] imageArray = new int[50];
      imageArray[8] = 8;
      assertTrue(UnionAndFind.find(imageArray, imageArray[8])==8);
  }

  @Test
    void testingXYPoints(){
      ArrayList<XYPoint> points = new ArrayList<>();
      XYPoint point = new XYPoint(500,501);
      points.add(point);
      assertSame(points.get(0), point);
  }

  @Test
    void twoDecimalPlaces(){
        double num = 500.24254;
      assertEquals(500.24, Utilities.toTwoDecimalPlaces(num));
  }

  @Test
  void testImageDims() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/main/tests/test-image-pills.png"));
        assertTrue(image.getWidth()==616);
        assertTrue(image.getHeight()==617);
  }




}
