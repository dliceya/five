package xyz.dlice.five.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FiveMessage;

/**
 * 对战消息
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BattleMessage extends FiveMessage {

    private String messageType = "BattleMessage";

    private Integer x;

    private Integer y;

    private String fromUser;

    private String toUser;
}
