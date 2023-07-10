package xyz.dlice.five.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FightRoom {

    /**
     * 对战发起方
     */
    private String sourceUser;

    /**
     * 对战目标
     */
    private String targetUser;

    /**
     * 对战创建时间
     */
    private Long createTime;

    /**
     * 总对局次数
     */
    private Integer times;

    /**
     * 对局发起方获胜次数
     */
    private Integer sourceWinTimes;

    /**
     * 对局接受者获胜次数
     */
    private Integer targetWinTimes;
}
