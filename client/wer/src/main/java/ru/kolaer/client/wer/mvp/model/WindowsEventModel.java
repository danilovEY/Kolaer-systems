package ru.kolaer.client.wer.mvp.model;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 14.03.2017.
 */
public interface WindowsEventModel {
    Optional<System> loadLastWindowsEvent();
    List<System> loadAllWindowsEvent();
}
