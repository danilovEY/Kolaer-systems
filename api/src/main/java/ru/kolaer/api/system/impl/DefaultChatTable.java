package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.ServerResponse;
import ru.kolaer.api.mvp.model.kolaerweb.kolchat.ChatGroupDto;
import ru.kolaer.api.system.network.ChatTable;

import java.util.List;

/**
 * Created by danilovey on 10.11.2017.
 */
public class DefaultChatTable implements ChatTable {
    @Override
    public ServerResponse<List<ChatGroupDto>> getActiveGroup() {
        return ServerResponse.createServerResponse();
    }
}
