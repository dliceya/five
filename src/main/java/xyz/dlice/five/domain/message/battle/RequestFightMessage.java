package xyz.dlice.five.domain.message.battle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RequestFightMessage extends BaseMessage {

    private String messageType = "RequestFightMessage";

    private String requestId;

    private Boolean ifTargetAgree;

}
