package com.mohistmc.ai.bots.mcbots;

import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.UnexpectedEncryptionException;
import com.github.steveice10.mc.protocol.data.game.ClientCommand;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerCombatKillPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundAcceptTeleportationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.github.steveice10.packetlib.ProxyInfo;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

public class Bot extends Thread {

    MinecraftProtocol protocol = null;

    @Getter
    private String nickname;
    private ProxyInfo proxy;
    private InetSocketAddress address;
    private Session client;
    private boolean hasMainListener;

    private double lastX, lastY, lastZ = -1;

    @Getter
    private boolean connected;

    private boolean manualDisconnecting = false;

    public Bot(String nickname, InetSocketAddress address, ProxyInfo proxy) {
        this.nickname = nickname;
        this.address = address;
        this.proxy = proxy;

        protocol = new MinecraftProtocol(nickname);
        client = new TcpClientSession(address.getHostString(), address.getPort(), protocol, proxy);
    }

    public Bot(AuthenticationService authService, InetSocketAddress address, ProxyInfo proxy) {
        this.nickname = authService.getUsername();
        this.address = address;
        this.proxy = proxy;

        protocol = new MinecraftProtocol(authService.getSelectedProfile(), authService.getAccessToken());

        client = new TcpClientSession(address.getHostString(), address.getPort(), protocol, proxy);

        SessionService sessionService = new SessionService();
        client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
    }

    @Override
    public void run() {

        if (!Main.isMinimal()) {
            client.addListener(new SessionAdapter() {

                @Override
                public void packetReceived(Session session, Packet packet) {
                    if (packet instanceof ClientboundLoginPacket) {
                        connected = true;
                    }
                    else if (packet instanceof ClientboundPlayerPositionPacket p) {

                        lastX = p.getX();
                        lastY = p.getY();
                        lastZ = p.getZ();

                        client.send(new ServerboundAcceptTeleportationPacket(p.getTeleportId()));
                    }
                    else if (packet instanceof ClientboundPlayerCombatKillPacket){
                        if (Main.autoRespawnDelay >= 0) {
                            new Timer().schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            client.send(new ServerboundClientCommandPacket(ClientCommand.RESPAWN));
                                        }
                                    },
                                    Main.autoRespawnDelay
                            );

                        }
                    }
                }

                @Override
                public void disconnected(DisconnectedEvent event) {
                    connected = false;

                    // Do not write disconnect reason if disconnected by command
                    if (!manualDisconnecting) {
                        // Fix broken reason string by finding the content with regex
                        Pattern pattern = Pattern.compile("content=\"(.*?)\"");
                        Matcher matcher = pattern.matcher(String.valueOf(event.getReason()));

                        StringBuilder reason = new StringBuilder();
                        while (matcher.find()) {
                            reason.append(matcher.group(1));
                        }

                        Log.info(" -> " + reason.toString());

                        if(event.getCause() != null) {
                            event.getCause().printStackTrace();

                            if (event.getCause() instanceof UnexpectedEncryptionException) {
                                Log.warn("Server is running in online (premium) mode. Please use the -o option to use online mode bot.");
                                System.exit(1);
                            }
                        }
                        Log.info();
                    }

                    Main.removeBot(Bot.this);

                    Thread.currentThread().interrupt();
                }
            });
        }
        client.connect();
    }

    public void registerMainListener() {
        hasMainListener = true;
        if (Main.isMinimal()) return;
        client.addListener(new MainListener());
    }

    public boolean hasMainListener() {
        return hasMainListener;
    }

    public void moveTo(double x, double y, double z)
    {
        client.send(new ServerboundMovePlayerPosPacket(true, x, y, z));
    }

    public void disconnect()
    {
        manualDisconnecting = true;
        client.disconnect("Leaving");
    }
}
