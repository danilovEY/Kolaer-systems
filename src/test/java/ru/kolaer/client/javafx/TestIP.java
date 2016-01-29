package ru.kolaer.client.javafx;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TestIP {
	public static void main(String[] args) throws UnknownHostException, SocketException {
		try {
			  InetAddress inet = InetAddress.getLocalHost();
			  InetAddress[] ips = InetAddress.getAllByName(inet.getHostName());
			  if (ips  != null ) {
				  System.out.println(inet.getHostAddress());
			  }
			} catch (UnknownHostException e) {

			}
	}
}
