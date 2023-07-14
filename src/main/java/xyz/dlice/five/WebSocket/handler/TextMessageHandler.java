package xyz.dlice.five.WebSocket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xyz.dlice.five.domain.FightRoom;
import xyz.dlice.five.domain.constants.MessageConstants;
import xyz.dlice.five.domain.entity.UserInfo;
import xyz.dlice.five.domain.message.*;
import xyz.dlice.five.domain.message.battle.BattleMessage;
import xyz.dlice.five.domain.message.battle.RequestFightMessage;
import xyz.dlice.five.domain.message.chat.ChatMessage;
import xyz.dlice.five.domain.message.sys.CommonMessage;
import xyz.dlice.five.domain.message.sys.RoomListUpdateMessage;
import xyz.dlice.five.domain.message.sys.UserListUpdateMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TextMessageHandler extends TextWebSocketHandler {

    private final Integer MAX_ONLINE_NUM = 32;

    // 用于存放所有建立连接的用户
    private final Map<String, WebSocketSession> allClients = new ConcurrentHashMap<>(MAX_ONLINE_NUM);

    // 当前未在对局的在线用户
    private final Map<String, UserInfo> freeUserMap = new HashMap<>(MAX_ONLINE_NUM);
    private final Map<String, UserInfo> allUserMap = new HashMap<>(MAX_ONLINE_NUM);

    // 正在进行对局的房间
    private final List<FightRoom> allRooms = new ArrayList<>(MAX_ONLINE_NUM);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        JSONObject jsonObject = JSON.parseObject(new String(message.asBytes()));
        String messageType = jsonObject.getString("messageType");
        switch (MessageConstants.MessageType.valueOf(messageType)) {
            case RequestFightMessage:
                RequestFightMessage requestFightMessage = JSON.parseObject(message.getPayload(), (Type) MessageConstants.MessageType.RequestFightMessage.getParseClass());

                // 为空表示发起请求放，将请求消息转发给目标用户
                if (Strings.isBlank(requestFightMessage.getRequestId())) {
                    requestFightMessage.setRequestId(UUID.randomUUID().toString());
                    if (this.checkTargetUserNotFree(requestFightMessage.getTargetUser())) {
                        this.sendMessage(new CommonMessage(requestFightMessage.getSourceUser(), "请求对战的用户已下线或正在对局，请稍后重试"));
                        return;
                    }

                    this.sendMessage(requestFightMessage);
                } else {
                    if (!requestFightMessage.getIfTargetAgree()) {
                        this.sendMessage(new CommonMessage(requestFightMessage.getSourceUser(), "对方拒绝了您的对战请求"));
                        return;
                    }

                    FightRoom fightRoom = new FightRoom();
                    fightRoom.setSourceUser(requestFightMessage.getSourceUser());
                    fightRoom.setTargetUser(requestFightMessage.getTargetUser());
                    fightRoom.setCreateTime(System.currentTimeMillis() / 1000L);
                    fightRoom.setTimes(0);
                    fightRoom.setSourceWinTimes(0);
                    fightRoom.setTargetWinTimes(0);
                    allRooms.add(fightRoom);
                    this.updateRoomList();

                    freeUserMap.remove(requestFightMessage.getTargetUser());
                    freeUserMap.remove(requestFightMessage.getSourceUser());
                    this.updateFreeUserList();
                }
                break;
            case BattleMessage:
                BattleMessage battleMessage = JSON.parseObject(message.getPayload(), (Type) MessageConstants.MessageType.BattleMessage.getParseClass());
                if (this.checkTargetUserNotFree(battleMessage.getTargetUser())) {
                    this.sendMessage(new CommonMessage(battleMessage.getSourceUser(), "对方已下线"));
                    return;
                }
                this.sendMessage(battleMessage);
                break;
            case ChatMessage:
                ChatMessage chatMessage = JSON.parseObject(message.getPayload(), (Type) MessageConstants.MessageType.ChatMessage.getParseClass());

                this.sendMessage(chatMessage);
                break;
        }
    }

    private void updateFreeUserList() {
        allClients.forEach((user, session) -> {
            UserListUpdateMessage message = new UserListUpdateMessage();
            message.setUserList(new ArrayList<>(freeUserMap.values()));
            this.sendMessage(session, message);
        });
    }

    private void updateRoomList() {

        allClients.forEach((user, session) -> {
            RoomListUpdateMessage message = new RoomListUpdateMessage();
            message.setRoomList(allRooms);
            this.sendMessage(session, message);
        });
    }

    private boolean checkTargetUserNotFree(String targetUser) {

        return !allClients.containsKey(targetUser) || !freeUserMap.containsKey(targetUser);
    }

    private void sendMessage(BaseMessage message) {

        WebSocketSession session = allClients.get(message.getTargetUser());

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(JSON.toJSONString(message)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(WebSocketSession session, BaseMessage message) {

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(JSON.toJSONString(message)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接初始化，同步空闲用户数据
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // 获取到拦截器中设置的name
        String name = (String) session.getAttributes().get("name");
        String qqNumber = (String) session.getAttributes().get("qqNumber");

        if (Strings.isBlank(name)) {
            this.sendMessage(session, new CommonMessage(name, "请输入用户名后再连接"));
            session.close();
        }

        if (allClients.size() > MAX_ONLINE_NUM){
            this.sendMessage(session, new CommonMessage(name, "当前同时在线人数过多，请稍后尝试"));
            session.close();
        }

        if (allClients.containsKey(name)) {
            this.sendMessage(session, new CommonMessage(name, CommonMessage.CommonMessageCode.NameRepeat));
            session.close();
        }

        allClients.put(name, session);
        freeUserMap.put(name, new UserInfo(name, qqNumber));
        allUserMap.put(name, new UserInfo(name, qqNumber));

        this.updateFreeUserList();
        this.updateRoomList();

        this.sendMessage(session, new CommonMessage(name, CommonMessage.CommonMessageCode.ConnectionSucceeded));
        log.info("用户：{} 已成功连接", name);

        // 定时任务，新连接建立后30分钟关闭该连接。
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    session.close();
                    allClients.remove(name);
                    freeUserMap.remove(name);
                    allUserMap.remove(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 60 * 60 * 1000);
    }

    /**
     * 当连接关闭的时候，释放连接资源，同步空闲用户数据
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String name = (String) session.getAttributes().get("name");

        allClients.remove(name);
        freeUserMap.remove(name);
        allUserMap.remove(name);

        this.destroyRoomByUser(name);

        this.updateFreeUserList();
        this.updateRoomList();

        super.afterConnectionClosed(session, status);

        log.info("用户：{} 已断开连接", name);
    }

    private void destroyRoomByUser(String name) {

        Optional<FightRoom> currentFightRoom = allRooms.stream().filter(p -> Objects.equals(p.getSourceUser(), name) || Objects.equals(p.getTargetUser(), name)).findFirst();
        if (currentFightRoom.isPresent()) {
            FightRoom fightRoom = currentFightRoom.get();
            String antherUser = Objects.equals(fightRoom.getSourceUser(), name) ? fightRoom.getTargetUser() : fightRoom.getSourceUser();
            this.sendMessage(new CommonMessage(antherUser, "对方已离线, 您可以留在此页面稍后重新选择在线用户对战"));
            freeUserMap.put(antherUser, allUserMap.get(antherUser));
            allRooms.remove(fightRoom);
        }
    }
}

