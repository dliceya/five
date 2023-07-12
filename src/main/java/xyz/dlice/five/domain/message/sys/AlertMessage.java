package xyz.dlice.five.domain.message.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AlertMessage extends BaseMessage {

    private String messageType = "AlertMessage";

    private String message;

    public AlertMessage(String targetUser, String message) {
        this.setTargetUser(targetUser);
        this.message = message;
    }

}
