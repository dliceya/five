package xyz.dlice.five.domain.message.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonMessage extends BaseMessage {

    private String messageType = "CommonMessage";

    private String msgCode;

    private String message;

    public CommonMessage(String targetUser, String message) {
        this.setTargetUser(targetUser);
        this.message = message;
    }

    public CommonMessage(String targetUser, CommonMessageCode code) {
        this.setTargetUser(targetUser);
        this.msgCode = code.getCode();
        this.message = code.message;
    }

    @Getter
    public enum CommonMessageCode {

        ConnectionSucceeded("ConnectionSucceeded", "ConnectionSucceeded", "您已成功加入服务器，选择玩家开始对战吧"),
        NameRepeat("NameRepeat", "ConnectionSucceeded", "当前用户名已存在，请换一个吧"),

        Default("", "", "");

        CommonMessageCode(String code, String desc, String message) {
            this.code = code;
            this.desc = desc;
            this.message = message;
        }

        private final String code;
        private final String desc;
        private final String message;
    }

}
