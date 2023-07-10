package xyz.dlice.five.domain;

import lombok.Data;

@Data
public class FiveMessage {

    private String messageType;

    private String sourceUser;

    private String targetUser;

}
