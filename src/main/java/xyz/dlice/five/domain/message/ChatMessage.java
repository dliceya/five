package xyz.dlice.five.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FiveMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends FiveMessage {

    private String messageType = "ChatMessage";

    private String message;

}
