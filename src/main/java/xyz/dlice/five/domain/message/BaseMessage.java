package xyz.dlice.five.domain.message;

import lombok.Data;

@Data
public class BaseMessage {

    private String messageType;

    private String sourceUser;

    private String targetUser;

}
