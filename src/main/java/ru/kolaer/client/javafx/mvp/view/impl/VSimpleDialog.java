package ru.kolaer.client.javafx.mvp.view.impl;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.VDialog;
import ru.kolaer.client.javafx.tools.Tools;

import java.util.concurrent.CountDownLatch;

public class VSimpleDialog implements VDialog {
	private final Logger LOG = LoggerFactory.getLogger(VSimpleDialog.class);
	
	protected Alert dialog;
	
	public VSimpleDialog() {
            final CountDownLatch latch = new CountDownLatch(1);
            LOG.info("111");
            Tools.runOnThreadFX(() -> {
                LOG.info("222");
                this.dialog = new Alert(Alert.AlertType.NONE);
                LOG.info("333");
                latch.countDown();
                LOG.info("444");
            });
            LOG.info("555");
            try{
                latch.await();
            }catch(final InterruptedException e){
                LOG.error("Ошибка!", e);
                latch.countDown();
            }
        //} else {
           // this.dialog = new Alert(Alert.AlertType.NONE);
       // }

	}
	
	@Override
	public void show(boolean isDialog) {
		Tools.runOnThreadFX(() -> {
			if(isDialog) {
				this.dialog.showAndWait();
			} else {
				this.dialog.show();
			}
		});
	}

	@Override
	public void close() {
		Tools.runOnThreadFX(() -> {
			this.dialog.close();
		});
	}

	@Override
	public void setTitle(final String title) {
		Tools.runOnThreadFX(() -> {
			this.dialog.setTitle(title);
			this.dialog.setHeaderText(title);
		});
	}

	@Override
	public String getTitle() {
		return this.dialog.getTitle();
	}

	@Override
	public void setText(final String text) {
		Tools.runOnThreadFX(() -> {
			this.dialog.setContentText(text);
		});
	}

	@Override
	public String getText() {
		return this.dialog.getContentText();
	}
}