package ru.kolaer.client.wer.mvp.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 14.03.2017.
 */
public class WindowsEventModelCmdSecurity implements WindowsEventModelCmd {
    private static final Logger log = LoggerFactory.getLogger(WindowsEventModelCmdSecurity.class);
    private static final String DEFAULT_CMD_COMMAND = "wevtutil qe Security /rd:true /f:xml";

    private CmdArguments cmdArguments;

    public WindowsEventModelCmdSecurity() {
        final CmdArguments cmdArguments = new CmdArguments();
    }

    public WindowsEventModelCmdSecurity(CmdArguments cmdArguments) {
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
    public Optional<System> loadLastWindowsEvent() {
        try {
            final Runtime runtime = Runtime.getRuntime();
            final Process exec = runtime.exec(DEFAULT_CMD_COMMAND + " " + this.cmdArguments);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<System> loadAllWindowsEvent() {
        return null;
    }

    private List<System> parceWindowsEvent(String text) {

        return Collections.emptyList();
    }
}
