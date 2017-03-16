package ru.kolaer.client.wer.mvp.model;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 14.03.2017.
 */
public class MWindowsEventCmdSecurity implements MWindowsEventCmd {
    private static final Logger log = LoggerFactory.getLogger(MWindowsEventCmdSecurity.class);
    private static final String DEFAULT_CMD_COMMAND = "wevtutil qe Security /rd:true /f:xml";
    private final XmlMapper xmlMapper;

    private CmdArguments cmdArguments;

    public MWindowsEventCmdSecurity() {
        this(new CmdArguments());
    }

    public MWindowsEventCmdSecurity(CmdArguments cmdArguments) {
        this.xmlMapper = new XmlMapper();
        this.setModel(cmdArguments);
    }

    @Override
    public CmdArguments getModel() {
        return this.cmdArguments;
    }

    @Override
    public void setModel(CmdArguments model) {
        this.cmdArguments = model == null ? CmdArguments.EMPTY : model;
    }

    @Override
    public Optional<Event> loadLastWindowsEvent() {
        final CmdArguments cmdArguments = new CmdArguments(this.cmdArguments.getHost(),
                this.cmdArguments.getUsername(), this.cmdArguments.getPassword(), 1);
        final List<Event> events = this.loadWindowsEvent(cmdArguments);

        return events.isEmpty() ? Optional.empty() : events.stream().findFirst();
    }

    @Override
    public List<Event> loadAllWindowsEvent() {
        final CmdArguments cmdArguments = new CmdArguments(this.cmdArguments.getHost(),
                this.cmdArguments.getUsername(), this.cmdArguments.getPassword(), 20);

        return this.loadWindowsEvent(cmdArguments);
    }

    @Override
    public List<Event> loadWindowsEvent() {
        return this.loadWindowsEvent(this.cmdArguments);
    }

    private List<Event> loadWindowsEvent(CmdArguments cmdArguments) {
        try {
            final Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec(DEFAULT_CMD_COMMAND + " " + cmdArguments);

            try(final InputStream inputStream = process.getInputStream();
                final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<Events>");
                bufferedReader.lines().forEach(stringBuilder::append);
                stringBuilder.append("</Events>");

                return this.parseWindowsEvent(stringBuilder.toString());
            } catch (Exception e) {
                log.error("Невозможно загрузить данные!", e);
            }

        } catch (IOException e) {
            log.error("Невозможно выполнить комманду!", e);
        }

        return Collections.emptyList();
    }

    private List<Event> parseWindowsEvent(String text) throws IOException {
        final XmlWindowsEvents xmlWindowsEvents = this.xmlMapper.readValue(text, XmlWindowsEvents.class);
        Optional.ofNullable(xmlWindowsEvents).ifPresent(wE ->
                log.debug("Count load events: {}", wE.getEvents().length)
        );

        return xmlWindowsEvents != null
                ? Arrays.asList(xmlWindowsEvents.getEvents())
                : Collections.emptyList();
    }
}
