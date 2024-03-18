package com.mohistmc.ai.bots.mcbots;

import java.security.SecureRandom;
import java.util.List;
import lombok.Setter;

public class NickGenerator {

    private static final int NICK_LEN = 16;
    private final SecureRandom random = new SecureRandom();

    private final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int charsLen = 62;

    private int linesSize;
    private List<String> lines;

    private int nickLen = NICK_LEN;

    /**
     * -- SETTER --
     *  Set nick is real from nick list
     *
     * @param real true to generate real nicknames
     */
    @Setter
    private boolean real = false;

    private String prefix = "";

    public String generateRandom(int len) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.append(CHARS.charAt(random.nextInt(charsLen)));
        }
        return result.toString();
    }
    
    public String nextRandom() {
        return prefix + generateRandom(nickLen);
    }

    public String nextReal() {
        String nick = prefix + lines.get(random.nextInt(linesSize));
        return nick.length() <= 16 ? nick : nick.substring(0, 15);

    }

    /**
     * Generate next nick
     * @return generated nick
     */
    public String nextNick() {
        return real ? nextReal() : nextRandom();
    }

    /**
     * Set prefix for generated nicks
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        nickLen = NICK_LEN - prefix.length();
        this.prefix = prefix;
    }

}
