package xyz.dlice.five.domain.message.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FightRoom;
import xyz.dlice.five.domain.message.BaseMessage;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoomListUpdateMessage extends BaseMessage {

    private String messageType = "RoomListUpdateMessage";

    private List<FightRoom> roomList;

}
