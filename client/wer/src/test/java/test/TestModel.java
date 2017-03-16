package test;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.wer.mvp.model.CmdArguments;
import ru.kolaer.client.wer.mvp.model.Event;
import ru.kolaer.client.wer.mvp.model.WindowsEventModelCmd;
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
        final WindowsEventModelCmd windowsEventModelCmdSecurity = new WindowsEventModelCmdSecurity();
        final Optional<Event> event = windowsEventModelCmdSecurity.loadLastWindowsEvent();

        Assert.assertTrue(event.isPresent());

        event.ifPresent(e -> log.debug(e.toString()));
    }

    @Test
    public void testXmlModelAllEvent() {
        final WindowsEventModelCmd windowsEventModelCmdSecurity = new WindowsEventModelCmdSecurity();
        final List<Event> event = windowsEventModelCmdSecurity.loadAllWindowsEvent();

        Assert.assertFalse(event.isEmpty());

        event.forEach(e -> log.debug(e.toString()));
    }

    @Test
    @Ignore
    public void testXmlModelEventByCmd() {
        final CmdArguments cmdArguments = new CmdArguments("host", "username", "password", 50);

        final WindowsEventModelCmd windowsEventModelCmdSecurity = new WindowsEventModelCmdSecurity(cmdArguments);
        final List<Event> event = windowsEventModelCmdSecurity.loadWindowsEvent();

        Assert.assertFalse(event.isEmpty());
        Assert.assertEquals(event.size(), 50);

        event.forEach(e -> log.debug(e.toString()));
    }

}
