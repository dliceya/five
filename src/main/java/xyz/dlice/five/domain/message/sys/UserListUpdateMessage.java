package xyz.dlice.five.domain.message.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.message.BaseMessage;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserListUpdateMessage extends BaseMessage {

    private String messageType = "UserListUpdateMessage";

    private List<String> userList;

}
