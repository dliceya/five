package xyz.dlice.five.domain.message.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonMessage extends BaseMessage {

    private String messageType = "CommonMessage";

    private String message;

    public CommonMessage(String targetUser, String message) {
        this.setTargetUser(targetUser);
        this.message = message;
    }

}
