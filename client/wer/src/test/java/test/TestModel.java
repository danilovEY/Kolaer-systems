package test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.WindowsEventModelCmdSecurity;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 15.03.2017.
 */
public class TestModel {
    private static final Logger log = LoggerFactory.getLogger(TestModel.class);

    @Test
    public void testXmlModelLastEvent() {
        final WindowsEventModelCmdSecurity windowsEventModelCmdSecurity = new WindowsEventModelCmdSecurity();
        final Optional<Event> event = windowsEventModelCmdSecurity.loadLastWindowsEvent();

        Assert.assertTrue(event.isPresent());

        event.ifPresent(e -> log.debug(e.toString()));
    }

    @Test
    public void testXmlModelAllEvent() {
        final WindowsEventModelCmdSecurity windowsEventModelCmdSecurity = new WindowsEventModelCmdSecurity();
        final List<Event> event = windowsEventModelCmdSecurity.loadAllWindowsEvent();

        Assert.assertFalse(event.isEmpty());

        event.forEach(e -> log.debug(e.toString()));
    }

}
