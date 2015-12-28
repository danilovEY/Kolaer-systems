package ru.kolaer.client.javafx;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.User32;
import ru.kolaer.client.javafx.tools.User32.MSG;


public class Test {
    //private static volatile int idSeq = 0;
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
    private boolean listen;
    private Boolean reset = false;
    private final Object lock = new Object();
    private Thread thread;
    Robot robot;
    Map<Integer, KeyWithName> map;
	 public Test() {
		 map = getListCode();
		 Runnable runnable = new Runnable() {
	            public void run() {
	            	try {
	            		robot = new Robot();
					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                MSG msg = new MSG();
	               /* KeyStroke key = KeyStroke.getKeyStroke("A");
	                HotKey hotKey = new HotKey(key, null);
	                int code = KeyMap.getCode(hotKey);*/
	                
	                for(int i = 8; i <= 222; i++){
	                	User32.RegisterHotKey(null, i, 0, map.get(i).code);               		
	                	User32.RegisterHotKey(null, i+1000, 0x0001, map.get(i).code);
	                	User32.RegisterHotKey(null, i+2000, 0x0002, map.get(i).code);
	                	User32.RegisterHotKey(null, i+3000, 0x0004, map.get(i).code);
	                	User32.RegisterHotKey(null, i+4000, 0x0008, map.get(i).code);
	                }
	                //RegisterHotKey(null, 16, 0x0004, 16);
	                
	                listen = true;
	                
	                while (listen) {
	                    while (User32.PeekMessage(msg, null, 0,0, User32.PM_REMOVE)) {
	                        if (msg.message == User32.WM_HOTKEY) {
	                            int id = msg.wParam.intValue();

	                            if(id>4000)
	                            	id -= 4000;
	                            else if(id>3000)
	                            	id -= 3000;
	                            else if(id>2000)
	                            	id -= 2000;
	                            if(id>1000)
	                            	id -= 1000;
	                             
	                           //System.out.println("Locale: " + (char)(id + 985));
	                           
	                           System.out.println("ID: " + map.get(id).code);
	                           
	                           User32.UnregisterHotKey(null, id);
	                           User32.UnregisterHotKey(null, id+1000);
	                           User32.UnregisterHotKey(null, id+2000);
	                           User32.UnregisterHotKey(null, id+3000);
	                           User32.UnregisterHotKey(null, id+4000);
	                           try {
	                        	   restTemplate.postForLocation(new StringBuilder("http://localhost:8080/kolaer/system/user/").append(username).append("/key/").append((char)map.get(id).code).toString(),null);
                        		   robot.keyPress(map.get(id).event);
	                           } catch (IllegalArgumentException ex) {
	                        	   System.out.println("Error!");
	                           }
	                           User32.RegisterHotKey(null, id, 0, map.get(id).code);
	                           User32.RegisterHotKey(null, id+1000, 0x0001, map.get(id).code);
	                           User32.RegisterHotKey(null, id+2000, 0x0002, map.get(id).code);
	                           User32.RegisterHotKey(null, id+3000, 0x0004, map.get(id).code);
	                           User32.RegisterHotKey(null, id+4000, 0x0008, map.get(id).code);
	                        }
	                    }
	                }
	            }
	        };

	        thread = new Thread(runnable);
	        thread.start();
	}
	 
	public static void main(String[] args) {
		new Test();
		
	}
	
	
	public static Map<Integer, KeyWithName> getListCode() {
		final Map<Integer, KeyWithName> map = new HashMap<Integer, KeyWithName>();
		
		for(int i = 8; i <= 222; i++){
			KeyWithName key = new KeyWithName();
			key.code = i;
			key.event = getKeyEvent(i);
			map.put(i, key);
		}

		return map;
	}

	public static int getKeyEvent(int code) {
		switch(code) {
			case 13 : return KeyEvent.VK_ENTER;
			case 91 :
			case 92 : return KeyEvent.VK_WINDOWS;
			case 93 : return KeyEvent.VK_NONCONVERT;
			case 157 : return KeyEvent.VK_NONCONVERT;
			case 186 : return KeyEvent.VK_SEMICOLON;
			case 187 : return KeyEvent.VK_EQUALS;
			case 188 : return KeyEvent.VK_COMMA;
			case 189 : return KeyEvent.VK_MINUS;
			case 190 : return KeyEvent.VK_PERIOD;
			case 191 : return KeyEvent.VK_SLASH;
			case 192 : return KeyEvent.VK_BACK_QUOTE;
			case 219 : return KeyEvent.VK_OPEN_BRACKET;
			case 220 : return KeyEvent.VK_BACK_SLASH;
			case 221 : return KeyEvent.VK_CLOSE_BRACKET;
			default : return code;
		}
	}
	
}



class KeyWithName {
	public KeyWithName() {
		// TODO Auto-generated constructor stub
	}
	public int event;
	public int code;
}
