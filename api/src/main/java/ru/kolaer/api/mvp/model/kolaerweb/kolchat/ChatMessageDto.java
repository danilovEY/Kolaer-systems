package ru.kolaer.api.mvp.model.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.AccountDto;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

import java.util.Date;

@Data
public class ChatMessageDto implements BaseDto {
	private Long id;
	private AccountDto fromAccount;
	private Date createMessage;
	private String message;
	private String room;
}
