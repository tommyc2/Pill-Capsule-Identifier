import com.tommycondon.ca1.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static javafx.scene.image.Image.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {

    private Controller controllerObj = new Controller();
    private ImageView imageView = new ImageView();

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





}
