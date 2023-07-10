package xyz.dlice.five.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FiveMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RequestFightMessage extends FiveMessage {

    private String messageType = "RequestFightMessage";

    private String requestId;

    private Boolean ifTargetAgree;

}
