package com.mohistmc.ai.teamspeak3;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.mohistmc.ai.sdk.qq.QQ;

public class TS3 {

    public static TS3Api api;

    public static void init() {
        final TS3Config config = new TS3Config();
        config.setHost("0.0.0.0");
        //config.setQueryPort(9987);
        config.setEnableCommunicationsLogging(true);

        final TS3Query query = new TS3Query(config);
        try {
            query.connect();
        } catch (Exception e) {
            return;
        }

        api = query.getApi();
        api.login("serveradmin", "tPATEwFI");
        api.selectVirtualServerById(1);
        api.setNickname("鱼酱-TS3");
        api.registerAllEvents();
        api.addTS3Listeners(new TS3Listener() {

            @Override
            public void onTextMessage(TextMessageEvent e) {
                System.out.printf("[TS][%s]: %s%n", e.getInvokerName(), e.getMessage().replace("ts ", ""));
                if (e.getMessage().startsWith("ts ")) {
                    QQ.sendToFishGroup("[TS][%s]: %s".formatted(e.getInvokerName(), e.getMessage().replace("ts ", "")));
                }
            }

            @Override
            public void onServerEdit(ServerEditedEvent e) {
                System.out.println("Server edited by " + e.getInvokerName());
            }

            @Override
            public void onClientMoved(ClientMovedEvent e) {
                System.out.println("Client has been moved " + e.getClientId());
            }

            @Override
            public void onClientLeave(ClientLeaveEvent e) {
                System.out.println(e.getInvokerName() + " 退出了TS服务器");
            }

            @Override
            public void onClientJoin(ClientJoinEvent e) {
                System.out.println(e.getClientNickname() + " 进入了TS服务器");
            }

            @Override
            public void onChannelEdit(ChannelEditedEvent e) {
                // ...
            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
                // ...
            }

            @Override
            public void onChannelCreate(ChannelCreateEvent e) {
                // ...
            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent e) {
                // ...
            }

            @Override
            public void onChannelMoved(ChannelMovedEvent e) {
                // ...
            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
                // ...
            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
                // ...
            }
        });
    }
}
