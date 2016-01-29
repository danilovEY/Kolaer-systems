package ru.kolaer.client.javafx;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestIP {
	public static void main(String[] args) {
		String line;
        String[] cmd = {"powershell.exe","/c","gwmi win32_process | select ProcessID, CommandLine | format-list"};
        System.out.println("Hello, world!\n");
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader input =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception e) {
        }
        return ;
	}
}
