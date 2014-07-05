package ru.kolaer.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Класс для работы с FTP.
 * 
 * @author Danilov E.Y.
 *
 */
public class FTPUpload
{
	private String server = "zhekichan.webuda.com";
	private int port = 21;
	private String userName = "a9004178";
	private String password = "h8z7cGkbCMXxZu6";
	
	private String pathActivFolder = "/public_html/AER-DecktopManager/";
	private String updateFile = "AER-DecktopManager.jar";
	private String pathNewVer;

	private FTPClient ftpClient;

	public FTPUpload()
	{
		this.ftpClient = new FTPClient();
	}

	public boolean downloadNewVersion()
	{
		boolean isDownload = false;
		try
		{
			this.pathNewVer = "AER-DecktopManager_"+new SimpleDateFormat("dd-MM-yy").format(new Date())+".jar";
			File downloadFile2 = new File(pathNewVer);
			OutputStream outputStream2 = new BufferedOutputStream(
					new FileOutputStream(downloadFile2));
			InputStream inputStream = ftpClient
					.retrieveFileStream(pathActivFolder + updateFile);

			byte[] bytesArray = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytesArray)) != -1)
			{
				outputStream2.write(bytesArray, 0, bytesRead);
			}

			boolean success = ftpClient.completePendingCommand();
			if (success)
			{
				isDownload = true;
			}
			outputStream2.close();
			inputStream.close();
		} 
		catch (IOException ex)
		{
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
		return isDownload;
	}
	
	
	public static String checkSum(String path)
	{
		String checksum = null;
		try
		{
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(path);
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] buffer = new byte[8192];
			int numOfBytesRead;
			while ((numOfBytesRead = fis.read(buffer)) > 0)
			{
				md.update(buffer, 0, numOfBytesRead);
			}
			byte[] hash = md.digest();
			checksum = new BigInteger(1, hash).toString(16);
		} 
		catch (IOException ex){} 
		catch (NoSuchAlgorithmException ex){}
		
		return checksum;
	}

	public boolean isNewVersion()
	{
		boolean newVersion = false;
		try
		{
			FTPFile[] listFile = ftpClient.listFiles(pathActivFolder);
			
			for(FTPFile file : listFile)
			{
				String fileName = file.getName();
				if(fileName.equals(".") || fileName.equals(".."))
				{
					continue;
				}
				String typefile = fileName.substring(fileName.length()-3, fileName.length());
				
				if(typefile.equals("md5"))
				{
					String md5uploadFile = fileName.substring(0, fileName.length()-4);
					
					if(!md5uploadFile.equals(checkSum(updateFile)))
					{
						newVersion = true;
					}
					
				}
				
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return newVersion;
	}

	public void disconnectFTP()
	{
		try
		{
			if (ftpClient.isConnected())
			{
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void connectFTP()
	{
		try
		{
			ftpClient.connect(server, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		} 
		catch (IOException ex)
		{
			//System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public String getServer()
	{
		return server;
	}

	public int getPort()
	{
		return port;
	}

	public String getPathActivFolder()
	{
		return pathActivFolder;
	}

	public String getUpdateFile()
	{
		return updateFile;
	}

	public String getPathNewVer()
	{
		return pathNewVer;
	}

}
