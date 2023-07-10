package xyz.dlice.five.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FiveMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonMessage extends FiveMessage {

    private String messageType = "CommonMessage";

    private String message;

    public CommonMessage() {}

    public CommonMessage(String targetUser, String message) {
        this.setTargetUser(targetUser);
        this.message = message;
    }

}
