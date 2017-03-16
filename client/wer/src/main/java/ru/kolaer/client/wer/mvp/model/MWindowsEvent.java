package ru.kolaer.client.wer.mvp.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 14.03.2017.
 */
public interface MWindowsEvent {
    MWindowsEvent EMPTY = new MWindowsEvent() {
        @Override
        public Optional<Event> loadLastWindowsEvent() {
            return Optional.empty();
        }

        @Override
        public List<Event> loadAllWindowsEvent() {
            return Collections.emptyList();
        }

        @Override
        public List<Event> loadWindowsEvent() {
            return Collections.emptyList();
        }
    };

    Optional<Event> loadLastWindowsEvent();
    List<Event> loadAllWindowsEvent();
    List<Event> loadWindowsEvent();
}
