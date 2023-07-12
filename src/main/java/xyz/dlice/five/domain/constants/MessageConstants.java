package xyz.dlice.five.domain.constants;

import lombok.Getter;
import xyz.dlice.five.domain.message.battle.BattleMessage;
import xyz.dlice.five.domain.message.chat.ChatMessage;
import xyz.dlice.five.domain.message.battle.RequestFightMessage;

public class MessageConstants {

    @Getter
    public enum MessageType {

        BattleMessage("对战消息", BattleMessage.class),
        ChatMessage("聊天消息", ChatMessage.class),
        RequestFightMessage("请求对战消息", RequestFightMessage.class);

        private final String type;

        private final Class<?> parseClass;

        MessageType(String type, Class<?> parseClass) {
            this.type = type;
            this.parseClass = parseClass;
        }
    }

}
