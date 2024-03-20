package com.mohistmc.ai.bots.mcbots;

import com.github.steveice10.mc.auth.exception.request.AuthPendingException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.auth.service.MsaAuthenticationService;
import com.github.steveice10.mc.auth.util.HTTP;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.packetlib.ProxyInfo;
import com.mohistmc.ai.log.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import lombok.Getter;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.Type;


public class Main {

    private static final SecureRandom random = new SecureRandom();
    private static final ArrayList<InetSocketAddress> proxies = new ArrayList<>();
    private static final String CLIENT_ID = "8bef943e-5a63-429e-a93a-96391d2e32a9";
    private static final HashSet<Bot> controlledBots = new HashSet<>();
    public static int autoRespawnDelay = 100;
    static ArrayList<Bot> bots = new ArrayList<>();
    private static int triedToConnect;
    private static int botCount = 100;
    private static boolean isMainListenerMissing = true;
    private static int delayMin = 100;
    private static int delayMax = 500;
    @Getter
    private static boolean minimal = false;
    private static boolean mostMinimal = false;
    private static boolean useProxies = false;
    private static int proxyIndex = 0;
    private static int proxyCount = 0;
    private static ProxyInfo.Type proxyType;

    public static void main(String[] args) throws Exception {
        main(false, false, false);
    }

    public static void main(boolean online, boolean proxy, boolean gravity) throws Exception {

        if (proxy) {
            String typeStr = "SOCKS4"; // SOCKS4 or SOCKS5

            //get proxy type
            try {
                proxyType = ProxyInfo.Type.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                Log.error("Inavlid proxy type, use SOCKS4 or SOCKS5.");
                System.exit(1);
            }

            String proxyPath = "http://api.89ip.cn/tqdl.html?api=1&num=100&port=&address=&isp=";

            //read proxy list file
            try {

                try {
                    //try to read specified path as URL
                    URL url = new URL(proxyPath);

                    BufferedReader read = new BufferedReader(
                            new InputStreamReader(url.openStream()));

                    Log.info("Reading proxies from URL");
                    String line;
                    while ((line = read.readLine()) != null) {
                        try {
                            String[] parts = line.trim().split(":");
                            if (parts.length == 2) {
                                int port = Integer.parseInt(parts[1]);
                                proxies.add(new InetSocketAddress(parts[0], port));
                                proxyCount++;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    read.close();

                } catch (MalformedURLException e) {
                    Log.info("Specified proxy file is not a URL, trying to read file");

                    Scanner scanner = new Scanner(new File(proxyPath));
                    while (scanner.hasNextLine()) {
                        try {
                            String[] parts = scanner.nextLine().trim().split(":");
                            if (parts.length == 2) {
                                int port = Integer.parseInt(parts[1]);
                                proxies.add(new InetSocketAddress(parts[0], port));
                                proxyCount++;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    scanner.close();
                }
            } catch (FileNotFoundException e) {
                Log.error("Invalid proxy list file path.");
                System.exit(1);
            }

            if (proxyCount > 0) {
                useProxies = true;
                Log.info("Loaded " + proxyCount + " valid proxies");
            } else {
                Log.error("No valid proxies loaded");
                System.exit(1);
            }

        }

        String address = "0.0.0.0";

        int port = 25565;
        if (address.contains(":")) {
            String[] split = address.split(":", 2);
            address = split[0];
            port = Integer.parseInt(split[1]);
        } else {
            Record[] records = new Lookup("_minecraft._tcp." + address, Type.SRV).run();
            if (records != null) {
                for (Record record : records) {
                    SRVRecord srv = (SRVRecord) record;
                    address = srv.getTarget().toString().replaceFirst("\\.$", "");
                    port = srv.getPort();
                }
            }
        }

        NickGenerator nickGen = new NickGenerator();
        nickGen.setReal(false);
        nickGen.setPrefix("");

        InetSocketAddress inetAddr = new InetSocketAddress(
                InetAddress.getByName(address).getHostAddress(),
                port
        );

        //print info
        Log.info("IP:", inetAddr.getHostString());
        Log.info("Port: " + inetAddr.getPort());
        Log.info("Bot count: " + botCount);

        //get and print server info
        ServerInfo serverInfo = new ServerInfo(inetAddr);
        serverInfo.requestInfo();
        ServerStatusInfo statusInfo = serverInfo.getStatusInfo();
        if (statusInfo != null) {
            Log.info(
                    "Server version: "
                            + statusInfo.getVersionInfo().getVersionName()
                            + " (" + statusInfo.getVersionInfo().getProtocolVersion()
                            + ")"
            );
            Log.info("Player Count: " + statusInfo.getPlayerInfo().getOnlinePlayers()
                    + " / " + statusInfo.getPlayerInfo().getMaxPlayers());
            Log.info();
        } else {
            Log.warn("There was an error retrieving server status information. The server may be offline or running on a different version.");
        }

        AuthenticationService authService = null;
        if (online) {
            Log.warn("Online mode enabled. The bot count will be set to 1.");
            botCount = 1;

            // Create request parameters map
            Map<String, String> params = new HashMap<>();
            params.put("client_id", CLIENT_ID);
            params.put("scope", "XboxLive.signin");

            // Send request to Microsoft OAuth api
            // https://learn.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-device-code
            URI endpointURI = URI.create("https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode");
            MsaAuthenticationService.MsCodeResponse response = HTTP.makeRequestForm(
                    Proxy.NO_PROXY, endpointURI, params, MsaAuthenticationService.MsCodeResponse.class
            );
            try {
                Log.info("Please go to " + response.verification_uri.toURL() + " to authenticate your account - Code: " + response.user_code);
            } catch (MalformedURLException e) {
                Log.error("Error while trying to get the url of the authentication page");
            }

            authService = new MsaAuthenticationService(CLIENT_ID, response.device_code);

            // Wait for user to login on microsoft page
            int retryMax = 20;
            while (true) {
                try {
                    authService.login();
                    break;
                } catch (Exception e) {
                    if (e instanceof AuthPendingException) {
                        Log.info("Authentication is pending, waiting for user to authenticate...");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ignored) {
                        }
                        if (retryMax == 0)
                            throw e;
                        retryMax--;
                    } else {
                        throw e;
                    }

                }
            }
        }

        AuthenticationService finalAuthService = authService;
        new Thread(() -> {
            for (int i = 0; i < botCount; i++) {
                try {
                    ProxyInfo proxyInfo = null;
                    if (useProxies) {
                        InetSocketAddress proxySocket = proxies.get(proxyIndex);

                        if (!minimal) {
                            Log.info(
                                    "Using proxy: (" + proxyIndex + ")",
                                    proxySocket.getHostString() + ":" + proxySocket.getPort()
                            );
                        }

                        proxyInfo = new ProxyInfo(
                                proxyType,
                                proxySocket
                        );

                        //increment or reset current proxy index
                        if (proxyIndex < (proxyCount - 1)) {
                            proxyIndex++;
                        } else {
                            proxyIndex = 0;
                        }

                    }

                    Bot bot = null;
                    if (finalAuthService != null) {
                        bot = new Bot(
                                finalAuthService,
                                inetAddr,
                                proxyInfo
                        );
                    } else {
                        bot = new Bot(RandomChineseNameGenerator.generateRandomName(), inetAddr, proxyInfo
                        );
                    }

                    bot.start();

                    if (!mostMinimal) bots.add(bot);

                    triedToConnect++;

                    if (isMainListenerMissing && !isMinimal()) {
                        isMainListenerMissing = false;
                        bot.registerMainListener();
                    }

                    if (i < botCount - 1) {
                        long delay = getRandomDelay();
                        Thread.sleep(delay);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Log.info(bots.size() + "个Bot已接入完毕!");
        }).start();

    }

    public static synchronized void renewMainListener() {
        bots.getFirst().registerMainListener();
    }

    public static synchronized void removeBot(Bot bot) {
        bots.remove(bot);
        controlledBots.remove(bot);
        if (bot.hasMainListener()) {
            Log.info("Bot with MainListener removed");
            isMainListenerMissing = true;
        }
        if (!bots.isEmpty()) {
            if (isMainListenerMissing && !isMinimal()) {
                Log.info("Renewing MainListener");
                renewMainListener();
                isMainListenerMissing = false;
            }
        } else {
            if (triedToConnect == botCount) {
                Log.error("All bots disconnected, exiting");
                System.exit(0);
            }
        }
        bot = null;
    }

    public static long getRandomDelay() {
        return random.nextInt(delayMax - delayMin) + delayMin;
    }

}
