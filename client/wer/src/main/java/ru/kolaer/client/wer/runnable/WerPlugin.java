package ru.kolaer.client.wer.runnable;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import ru.kolaer.api.plugins.UniformSystemPlugin;
import ru.kolaer.api.plugins.services.Service;
import ru.kolaer.api.system.UniformSystemEditorKit;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by danilovey on 14.03.2017.
 */
public class WerPlugin implements UniformSystemPlugin {
    private BorderPane mainPane;

    @Override
    public void setContent(Parent content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void initialization(UniformSystemEditorKit editorKit) throws Exception {

    }

    @Override
    public URL getIcon() {
        return null;
    }

    @Override
    public Collection<Service> getServices() {
        return null;
    }

    @Override
    public void start() throws Exception {
        final VBox vBox = new VBox();
        this.mainPane = new BorderPane(vBox);
        /*Advapi32Util.EventLogIterator iter = new Advapi32Util.EventLogIterator("Security");
        while(iter.hasNext()) {
            Advapi32Util.EventLogRecord record = iter.next();
            if(record.getType() != Advapi32Util.EventLogType.AuditFailure)
                continue;
            System.out.println(record.getRecordNumber()
                    + ": Event ID: " + record.getEventId()
                    + ", Event Type: " + record.getType()
                    + ", Event Source: " + record.getSource());

            String dateAsText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date(record.getRecord().TimeWritten.longValue() * 1000L));
            System.out.println(dateAsText);

            vBox.getChildren().add(new Label(dateAsText));*/
            /*for (String s : record.getStrings()) {
                System.out.println(s);
            }*/
    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void updatePluginObjects(String key, Object object) {

    }
}
