package test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.client.wer.mvp.model.XmlWindowsEvents;

import java.io.*;

/**
 * Created by danilovey on 15.03.2017.
 */

public class TestXmlLoad {
    private final Logger log = LoggerFactory.getLogger(TestXmlLoad.class);

    private String xmlText = "<Events></Events>";

    @Before
    public void loadXml() {

        try {
            final Process runtime = Runtime.getRuntime().exec("wevtutil qe Security /c:3 /rd:true /f:xml");

            try(final InputStream inputStream = runtime.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<Events>");
                bufferedReader.lines().forEach(stringBuilder::append);
                stringBuilder.append("</Events>");
                xmlText = stringBuilder.toString();
                log.info("LOAD XML: {}", this.xmlText);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void parseXml() throws IOException {
        Assert.assertFalse(xmlText.isEmpty());

        final XmlMapper xmlMapper = new XmlMapper();
        final XmlWindowsEvents xmlWindowsEvents = xmlMapper.readValue(this.xmlText, XmlWindowsEvents.class);
        Assert.assertNotNull(xmlWindowsEvents);
        Assert.assertEquals(xmlWindowsEvents.getEvents().length, 3);
    }
}
