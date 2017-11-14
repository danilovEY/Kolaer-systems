package ru.kolaer.api.system.network;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public interface ChatTable {
    ServerResponse<List<ChatGroupDto>> getActiveGroup();
}
