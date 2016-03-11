package ru.kolaer.client.javafx;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.client.RestTemplate;

public class TestScreen {
	
	public static void main(String[] args) throws Exception {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage capture = new Robot().createScreenCapture(screenRect);
		RestTemplate restTemplate = new RestTemplate();

		byte[] imageInByte;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(capture, "jpg", baos);
        baos.flush();
        imageInByte = baos.toByteArray();
        baos.close();
        
        /*InputStream in = new ByteArrayInputStream(imageInByte);
        BufferedImage bImageFromConvert = ImageIO.read(in);

        ImageIO.write(bImageFromConvert, "jpg", new File(
                "c:/new-darksouls.jpg"));*/
		restTemplate.postForObject("http://localhost:8080/kolaer/system/user/test/screen", imageInByte, byte[].class);
		
		
	}

}
