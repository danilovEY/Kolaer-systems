package ru.kolaer.asmc.tools;

import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alee.extended.window.WebProgressDialog;

/**
 * Класс для работы с FTP.
 * 
 * @author Danilov E.Y.
 *
 */
public class FTPUpload
{
	
	private static final Logger log = LoggerFactory.getLogger(FTPUpload.class);	
	
	private String server = "zhekichan.webuda.com";
	private int port = 21;
	private String userName = "a9004178";
	private String password = "h8z7cGkbCMXxZu6";
	
	private String pathActivFolder = "/public_html/AER-DecktopManager/";
	private String updateMainFile = "Utilites/AER-DecktopManager.jar";
	private String updateLauncher = "AER-DM-WindowsLauncher.exe";
	private String updateWebBrowser = "Utilites/WebBrowser.exe";
	private String pathNewVer;

	private FTPClient ftpClient;
	
	private WebProgressDialog progress;
	
	private int maxByte;

	public FTPUpload()
	{
		this.ftpClient = new FTPClient();
	}

	/**
	 * Скачивание лаунчера.
	 * 
	 * @return true - если скачался.
	 */
	private boolean downloadWebBrowser()
	{
		log.info("Загрузка браузера...");
		
		boolean isDownloadWebBrowser = false;
		
		maxByte = 0;
		
		//Размер файла основной программы
		FTPFile[] listFile;
		try
		{
			listFile = this.ftpClient.listFiles(pathActivFolder + "Utilites");
		} 
		catch (IOException e)
		{
			log.error("Ошибка при чтении "+pathActivFolder + "Utilites"+" каталога на FTP.");
			log.error(e.getMessage());
			return isDownloadWebBrowser;
		}
		
		for(FTPFile file : listFile)
		{
			String fileName = file.getName();
			if (fileName.equals(".") || fileName.equals(".."))
			{
				continue;
			}

			if(fileName.equals("WebBrowser.exe"))
			{
				maxByte = (int)(file.getSize() / 1024);
				break;
			}
		}
		
		//Поток для чтения
		InputStream inputStream = null;
		try
		{
			inputStream = ftpClient.retrieveFileStream(pathActivFolder
					+ updateMainFile);
		} 
		catch (IOException e)
		{
			log.error("Ошибка при получении файла: "+pathActivFolder + updateWebBrowser+" на FTP.");
			log.error(e.getMessage());
			return isDownloadWebBrowser;
		}
		

		//=====Process Bar=====
		 progress.setText ( "Идет загрузка \"WebBrowser.exe\" ("+"0 из " + maxByte+ ")..." );
		 progress.setMaximum(maxByte);
		 progress.pack();
		 progress.setProgress(0);
		
		//Поток для записи
		OutputStream outputStreamNewWebBrowser = null;
		try
		{
			outputStreamNewWebBrowser = new BufferedOutputStream(
					new FileOutputStream(new File(updateWebBrowser)));
		} 
		catch (FileNotFoundException e)
		{
			log.error("Ошибка при создании файла для записи: \""+updateWebBrowser+"\".");
			log.error(e.getMessage());
			
			try
			{
				if(inputStream!= null)
					inputStream.close();
			} 
			catch (IOException e1){}
			
			return isDownloadWebBrowser;
		}
		
		int value = progress.getProgress();
		
		byte[] bytesArray = new byte[2048];
		int bytesRead = -1;
		
		try
		{
			log.info("Скачивание браузера...");
			while ((bytesRead = inputStream.read(bytesArray)) != -1)
			{
				if (!progress.isVisible())
				{
					log.warn("Прерывание скачивание файла: \""+updateWebBrowser+"\".");
					inputStream.close();
					outputStreamNewWebBrowser.close();
					return false;
				}

				outputStreamNewWebBrowser.write(bytesArray, 0, bytesRead);

				value += bytesRead / 1024;
				progress.setProgress(value);
				progress.setText("Идет загрузка \"WebBrowser.exe\" ("
						+ value + " из " + maxByte + ")...");
			}
		} 
		catch (IOException e)
		{
			log.error("Ошибка при скачивании файла: \""+updateWebBrowser+"\".");
			log.error(e.getMessage());			
			return isDownloadWebBrowser;
		} 
		finally
		{
			try
			{
				outputStreamNewWebBrowser.close();
				inputStream.close();
			} catch (IOException e){}
		}
		
		
		
		boolean success = false;
		try
		{
			success = ftpClient.completePendingCommand();
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());			
			return isDownloadWebBrowser;
		}
		if (success)
		{
			isDownloadWebBrowser = true;
		}
		
		return isDownloadWebBrowser;
	}
	
	/**
	 * Скачивание лаунчера.
	 * 
	 * @return true - если скачался.
	 */
	private boolean downloadLauncher()
	{
		log.info("Загрузка лаунчера...");
		
		boolean isDownloadLauncher = false;
		
		maxByte = 0;
		
		//Получение размера файла
		FTPFile[] listFile = null;
		try
		{
			listFile = this.ftpClient.listFiles(pathActivFolder);
		} 
		catch (IOException e)
		{
			log.error("Ошибка при чтении корневого каталога на FTP.");
			log.error(e.getMessage());
			return isDownloadLauncher;
		}
		
		for(FTPFile file : listFile)
		{
			String fileName = file.getName();
			if (fileName.equals(".") || fileName.equals(".."))
			{
				continue;
			}

			if(fileName.equals(updateLauncher))
			{
				maxByte = (int)(file.getSize() / 1024);
				break;
			}
		}
		
		log.info("Размер файла: \""+updateLauncher+"\" = "+maxByte + "КБ");
		
		//======Progress Bar======
		progress.setText ( "Идет загрузка \""+updateLauncher+"\" ("+"0 из " + maxByte+ ")..." );
        progress.setMaximum(maxByte);
        progress.setProgress(0);
		
		InputStream inputStreamLauncher = null;
		
		try
		{
			//Получение файла с FTP.
			inputStreamLauncher = ftpClient.retrieveFileStream(pathActivFolder
					+ updateLauncher);
		} 
		catch (IOException e)
		{
			log.error("Ошибка при получении файла: \""+pathActivFolder + updateLauncher+"\" на FTP.");
			log.error(e.getMessage());			
			return isDownloadLauncher;
			
		}
		
		byte[] bytesArray = new byte[2048];
		int bytesRead = -1;
		

		//=================== Launcher ====================		
		OutputStream outputStreamNewLauncher = null;
		try
		{
			//Поток для записи файла с FTP.
			outputStreamNewLauncher = new BufferedOutputStream(
					new FileOutputStream(new File(updateLauncher)));
		} catch (FileNotFoundException e)
		{
			log.error("Ошибка при создании файла для записи: \""+updateLauncher+"\".");
			log.error(e.getMessage());
			
			try
			{
				if(inputStreamLauncher!= null)
					inputStreamLauncher.close();
			} 
			catch (IOException e1){}
			
			return isDownloadLauncher;
		}
		
		int value = progress.getProgress();
		try
		{
			log.info("Скачивание лаунчера...");
			while ((bytesRead = inputStreamLauncher.read(bytesArray)) != -1)
			{
				if (!progress.isVisible())
				{
					log.warn("Прерывание скачивание файла: \""+updateLauncher+"\".");
					outputStreamNewLauncher.close();
					inputStreamLauncher.close();
					return false;
				}
				outputStreamNewLauncher.write(bytesArray, 0, bytesRead);

				value += bytesRead / 1024;
				progress.setProgress(value);
				progress.setText("Идет загрузка \"" + updateLauncher + "\" ("
						+ value + " из " + maxByte + ")...");
			}
		} catch (IOException e)
		{
			log.error("Ошибка при скачивании файла: \""+updateLauncher+"\".");
			log.error(e.getMessage());			
			return isDownloadLauncher;
		}
		finally
		{
			try
			{
				outputStreamNewLauncher.close();
				inputStreamLauncher.close();
			}catch (IOException e){}
		}

		
		boolean success= false;
		try
		{
			success = ftpClient.completePendingCommand();
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());			
			return isDownloadLauncher;
		}
		
		if (success)
		{
			isDownloadLauncher = true;
		}
		
		return isDownloadLauncher;
	}
	
	/**
	 * Скачивание основной программы.
	 * @return true - если скачался.
	 */
	private boolean downloadProgramm()
	{
		log.info("Загрузка программы...");
		
		boolean isDownloadProg = false;
		
		maxByte = 0;
		
		//Размер файла основной программы
		FTPFile[] listFile;
		try
		{
			listFile = this.ftpClient.listFiles(pathActivFolder + "Utilites");
		} 
		catch (IOException e)
		{
			log.error("Ошибка при чтении "+pathActivFolder + "Utilites"+" каталога на FTP.");
			log.error(e.getMessage());
			return isDownloadProg;
		}
		
		for(FTPFile file : listFile)
		{
			String fileName = file.getName();
			if (fileName.equals(".") || fileName.equals(".."))
			{
				continue;
			}

			if(fileName.equals("AER-DecktopManager.jar"))
			{
				maxByte = (int)(file.getSize() / 1024);
				break;
			}
		}
		
		//Поток для чтения
		InputStream inputStream = null;
		try
		{
			inputStream = ftpClient.retrieveFileStream(pathActivFolder
					+ updateMainFile);
		} 
		catch (IOException e)
		{
			log.error("Ошибка при получении файла: "+pathActivFolder + updateMainFile+" на FTP.");
			log.error(e.getMessage());
			return isDownloadProg;
		}
		

		//=====Process Bar=====
		 progress.setText ( "Идет загрузка \"AER-DecktopManager.jar\" ("+"0 из " + maxByte+ ")..." );
		 progress.setMaximum(maxByte);
		 progress.pack();
		 progress.setProgress(0);
		
		this.pathNewVer = "Utilites/AER-DecktopManager_"
				+ new SimpleDateFormat("dd-MM-yy").format(new Date()) + ".jar";
		
		//Поток для записи
		OutputStream outputStreamNewVer = null;
		try
		{
			outputStreamNewVer = new BufferedOutputStream(
					new FileOutputStream(new File(pathNewVer)));
		} 
		catch (FileNotFoundException e)
		{
			log.error("Ошибка при создании файла для записи: \""+pathNewVer+"\".");
			log.error(e.getMessage());
			
			try
			{
				if(inputStream!= null)
					inputStream.close();
			} 
			catch (IOException e1){}
			
			return isDownloadProg;
		}
		
		int value = progress.getProgress();
		
		byte[] bytesArray = new byte[2048];
		int bytesRead = -1;
		
		try
		{
			log.info("Скачивание основной программы...");
			while ((bytesRead = inputStream.read(bytesArray)) != -1)
			{
				if (!progress.isVisible())
				{
					log.warn("Прерывание скачивание файла: \""+pathNewVer+"\".");
					inputStream.close();
					outputStreamNewVer.close();
					return false;
				}

				outputStreamNewVer.write(bytesArray, 0, bytesRead);

				value += bytesRead / 1024;
				progress.setProgress(value);
				progress.setText("Идет загрузка \"AER-DecktopManager.jar\" ("
						+ value + " из " + maxByte + ")...");
			}
		} 
		catch (IOException e)
		{
			log.error("Ошибка при скачивании файла: \""+pathNewVer+"\".");
			log.error(e.getMessage());			
			return isDownloadProg;
		} 
		finally
		{
			try
			{
				outputStreamNewVer.close();
				inputStream.close();
			} catch (IOException e){}
		}
		
		
		
		boolean success = false;
		try
		{
			success = ftpClient.completePendingCommand();
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());			
			return isDownloadProg;
		}
		if (success)
		{
			isDownloadProg = true;
		}
		
		return isDownloadProg;
	}
	
	/**
	 * Скачивание файлов с FTP.
	 * @return true - если скачалось все.
	 * @throws IOException
	 */
	public boolean downloadNewVersion()
	{
		log.info("Загрузка обновлений...");
	
		
		Thread downloadLauncherThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				progress = new WebProgressDialog ( null, "Загрузка обновлений" );
		        progress.setText ( "Идет загрузка..." );
		        progress.setModal ( true );
		        progress.setPreferredSize(new Dimension(500, 100));
		        progress.pack();
		        progress.setVisible ( true );		        
			}
		});
		
		downloadLauncherThread.setDaemon(true);
		downloadLauncherThread.start();
		

		try{Thread.sleep(100);} catch (InterruptedException e){}
		
		//Скачивание браузера
		//boolean isDownloadWebBrowser = this.downloadWebBrowser();
				
		//Скачивание лаунчера
		boolean isDownloadLauncher = this.downloadLauncher();
		
		//Скачивание основной программы
		boolean isDownloadProg = this.downloadProgramm();				
		
		progress.setVisible(false);
				
		
		if(isDownloadLauncher && isDownloadProg)
			return true;
		else
			return false;
	}
	
	/**
	 * Расчет хэш-суммы у файла.
	 * @param path Путь к файлу.
	 * @return хэш-суммму в виде String.
	 */
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
			fis.close();
		} 
		catch (IOException ex)
		{
			log.error("Файл: \""+path+"\" не найден.");
		} 
		catch (NoSuchAlgorithmException ex){}
		
		return checksum;
	}

	/**
	 * Проверка на существование новой версии на FTP-сервере.
	 * @return - true -если есть новая версия.
	 * @throws IOException
	 */
	public boolean isNewVersion() throws IOException
	{
		log.info("Проверка обновлений...");
		
		boolean newVersion = false;

		//Получаем список всех файлов в корневом каталоге на FTP
		FTPFile[] listFile = ftpClient.listFiles(pathActivFolder);

		for (FTPFile file : listFile)
		{
			String fileName = file.getName();
			//Пропускаем не папки и файлы
			if (fileName.equals(".") || fileName.equals(".."))
			{
				continue;
			}
			
			//Смотрим на тип файла
			String typefile = fileName.substring(fileName.length() - 3,
					fileName.length());

			//Если это хэш-сумма...
			if (typefile.equals("md5"))
			{
				//то берем имя (т.е. хэш-сумму)
				String md5uploadFile = fileName.substring(0,
						fileName.length() - 4);

				log.info("Сравнение хэш-сумм...");
				
				//и если она не равна, то значит на сервере лежит новый файл
				
				String mainFileMD5 = checkSum(updateMainFile);
				
				if (!md5uploadFile.equals(mainFileMD5))
				{
					log.info(" СЕРВЕР || ЛОКАЛЬНЫЙ ");
					log.info(md5uploadFile+" || "+mainFileMD5);
					newVersion = true;
				}

			}
		}
		
		ftpClient.abort();
		
		if(!newVersion)
		{
			//log.error("Ошибка в FTP");
		}
		
		return newVersion;
	}

	/**
	 * Отключение от FTP-сервера.
	 */
	public void disconnectFTP()
	{
		
		try
		{
			if (ftpClient.isConnected())
			{
				log.info("Отключение от FPT...");
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException ex)
		{
			log.error(ex.getMessage());
		}
	}

	/**
	 * Подключен ли к FTP.
	 * @return true - если да
	 */
	public boolean isConnected()
	{
		return ftpClient.isConnected();
	}
	
	public synchronized void connectFTP() throws SocketException, IOException
	{
		log.info("Подключение к FPT...");
		
		try
		{
			ftpClient.connect(server, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} 
		catch (SocketException e)
		{
			log.error(e.getMessage());
			throw e;
		} 
		catch (IOException e)
		{
			log.error(e.getMessage());
			throw e;
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
		return updateMainFile;
	}

	public String getPathNewVer()
	{
		return pathNewVer;
	}

}
