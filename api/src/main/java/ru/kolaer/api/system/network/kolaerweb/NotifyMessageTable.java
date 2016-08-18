package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.NotifyMessage;

/**
 * Created by danilovey on 18.08.2016.
 */
public interface NotifyMessageTable {
    NotifyMessage getLastNotifyMessage() throws ServerException;
}
