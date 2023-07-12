package xyz.dlice.five.domain.message.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseMessage {

    private String messageType = "ChatMessage";

    private String message;

}
