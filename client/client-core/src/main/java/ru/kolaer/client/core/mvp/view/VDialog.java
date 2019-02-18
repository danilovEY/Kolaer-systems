package ru.kolaer.client.core.mvp.view;

public interface VDialog {
	void setTitle(String title);
	String getTitle();
	
	void setText(String text);
	String getText();
	
	void show(boolean isDialog);
	void close();
}
