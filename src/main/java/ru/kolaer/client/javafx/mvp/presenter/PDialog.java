package ru.kolaer.client.javafx.mvp.presenter;

import ru.kolaer.client.javafx.mvp.view.VDialog;

public interface PDialog {	
	
	void setView(VDialog view);
	VDialog getDialog();
	
	void setTitle(String title);
	String getTitle();
	
	void setText(String text);
	String getText();
	
	void show();
	void showDialog();
	void close();
}
