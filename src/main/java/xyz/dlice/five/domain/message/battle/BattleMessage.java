package xyz.dlice.five.domain.message.battle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

/**
 * 对战消息
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BattleMessage extends BaseMessage {

    private String messageType = "BattleMessage";

    private Integer idx;

    private Integer idy;

}
