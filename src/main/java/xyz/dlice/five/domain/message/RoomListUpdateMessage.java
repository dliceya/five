package xyz.dlice.five.domain.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import xyz.dlice.five.domain.FightRoom;
import xyz.dlice.five.domain.FiveMessage;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoomListUpdateMessage extends FiveMessage {

    private String messageType = "RoomListUpdateMessage";

    private List<FightRoom> roomList;

}
