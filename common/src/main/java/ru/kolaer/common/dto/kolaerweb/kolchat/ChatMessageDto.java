package ru.kolaer.common.dto.kolaerweb.kolchat;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.util.Date;

@Data
public class ChatMessageDto implements BaseDto {
	private Long id;
	private ChatUserDto fromAccount;
	private Date createMessage;
	private String message;
	private Long roomId;
	private ChatMessageType type;
	private boolean read;
	private boolean hide;
}
