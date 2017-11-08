package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageDto implements BaseDto{
	private Long id;
	private AccountDto fromAccount;
	private Date createMessage;
	private String message;
	private String room;
}
