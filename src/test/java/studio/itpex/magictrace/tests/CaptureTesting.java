package studio.itpex.magictrace.tests;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sarxos.webcam.Webcam;

public class CaptureTesting {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException {
        String log4jConfPath = "src/main/resources/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        
        File file =new File("");
        System.out.println(file.getAbsolutePath());
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        ImageIO.write(webcam.getImage(), "jpg", new File("output/test2.jpg"));
    }

}
