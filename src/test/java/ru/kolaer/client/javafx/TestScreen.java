package ru.kolaer.client.javafx;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

		System.out.println(imageInByte.length);

		System.out.println(new PackageNetwork(imageInByte, true).getPureData().length);

		InputStream in = new ByteArrayInputStream(new PackageNetwork(imageInByte, true).getPureData());
		BufferedImage bImageFromConvert = ImageIO.read(in);

		ImageIO.write(bImageFromConvert, "jpg", new File("c:/new-darksouls.jpg"));
		// restTemplate.postForObject("http://localhost:8080/kolaer/system/user/test/screen", imageInByte, byte[].class);

	}
}

class PackageNetwork implements Serializable {
	private static final long serialVersionUID = -1738414013319887507L;
	private final Logger LOG = LoggerFactory.getLogger(PackageNetwork.class);

	private byte[] pureData;
	private boolean compressed;
	private int uncompressedSize;

	public PackageNetwork(byte[] pureData, boolean compressed) {
		uncompressedSize = pureData.length;
		this.compressed = compressed;
		if(compressed){
			ByteArrayOutputStream baos = new ByteArrayOutputStream(uncompressedSize);
			try(GZIPOutputStream zos = new GZIPOutputStream(baos)){
				zos.write(pureData, 0, uncompressedSize);
			}catch(IOException e){
				LOG.error("Ошибка при сжитии байтов!", e);
			}
			this.pureData = baos.toByteArray();
		}else{
			this.pureData = pureData;
		}

	}

	public byte[] getPureData(){
		if(compressed){
			ByteArrayInputStream bais = new ByteArrayInputStream(pureData);
			try(ByteArrayOutputStream uncompressed = new ByteArrayOutputStream(uncompressedSize); GZIPInputStream zis = new GZIPInputStream(bais)) {
				byte[] buf = new byte[1024];
				int len;
				while((len = zis.read(buf)) > 0){
					uncompressed.write(buf, 0, len);
				}
				return uncompressed.toByteArray();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return pureData;
	}
}