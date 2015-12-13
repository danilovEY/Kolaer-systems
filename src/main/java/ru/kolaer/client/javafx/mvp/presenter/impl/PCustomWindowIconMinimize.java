package ru.kolaer.client.javafx.mvp.presenter.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import jfxtras.labs.scene.control.window.WindowIcon;
import ru.kolaer.client.javafx.mvp.presenter.PCustomWindow;

/**
 *
 * @author Danilov
 * @version 0.1
 */
public class PCustomWindowIconMinimize extends WindowIcon {
	public static final String DEFAULT_STYLE_CLASS = "window-custom-minimize-icon";
	
	/**
	 * {@linkplain PCustomWindowIconMaximize}
	 */
	public PCustomWindowIconMinimize(PCustomWindow window) {
		this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

              /* w.setMinimized(!w.isMinimized());
               
               if (w.isSelected()) {
                   minimizeSelectedWindows();
               }*/
            }
        });
    }
    
    // TODO move from skin to behavior class (a lot of other stuff here too)
    private void minimizeSelectedWindows() {
       /* for (SelectableNode sN : WindowUtil.
                getDefaultClipboard().getSelectedItems()) {

            if (sN == w
                    || !(sN instanceof Window)) {
                continue;
            }

            Window selectedWindow = (Window) sN;

            if (w.getParent().
                    equals(selectedWindow.getParent())) {
                
                selectedWindow.setMinimized(!selectedWindow.isMinimized());
            }
        } */
    }
}
