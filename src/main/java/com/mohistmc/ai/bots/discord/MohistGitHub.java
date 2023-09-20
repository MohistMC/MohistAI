package com.mohistmc.ai.bots.discord;

import com.mohistmc.ai.Account;
import com.mohistmc.ai.MohistAI;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedAuthor;
import org.javacord.api.event.message.MessageCreateEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/14 15:12:59
 */
public class MohistGitHub {

    public static void onMessage(MessageCreateEvent event) {
        String message = event.getMessageContent();
        if (String.valueOf(event.getChannel().getId()).equals(Account.discordAnnonces)) {
            String sendMsg = "===有新的公告-来自Discord===" + "\n" + message;
            MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg);
        }

        if (!event.getMessage().getEmbeds().isEmpty()) {
            if (String.valueOf(event.getChannel().getId()).equals(Account.discordJenkins)) {
                for (Embed embeds : event.getMessage().getEmbeds()) {
                    Optional<String> Otitle = embeds.getTitle();
                    Optional<String> Odes = embeds.getDescription();
                    if (Otitle.isPresent() && Odes.isPresent()) {
                        String title = Otitle.get();
                        String des = Odes.get();
                        if (!des.contains("No changes") &&!des.contains("failure") && !title.contains("started")) {
                            String[] titles = title.split(" ");
                            String sendMsg = ("""
                                    ======Jenkins构建推送======
                                    项目: %s
                                    构建号: %s""")
                                    .formatted(titles[0], titles[1]);
                            MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg);
                        }
                    }
                }
            }
            if (String.valueOf(event.getChannel().getId()).equals(Account.discordGitHub)) {
                for (Embed embeds : event.getMessage().getEmbeds()) {
                    Optional<EmbedAuthor> Oauthor = embeds.getAuthor();
                    Optional<String> Otitle = embeds.getTitle();
                    Optional<String> Odes = embeds.getDescription();
                    if (Oauthor.isPresent() && Otitle.isPresent() && Odes.isPresent()) {
                        String author = Oauthor.get().getName();
                        String title = Otitle.get();
                        String des = Odes.get();

                        String[] strings = title.split(" ");
                        String[] strings1 = strings[0].replace("[", "").replace("]", "").split(":");

                        if (strings1[0].equals("maven")) {
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String sd = sdf.format(new Date(event.getMessage().getCreationTimestamp().toEpochMilli()));      // 时间戳转换成时间
                        String[] mm = des.split("/");
                        String TJ = des.replace(mm[6].substring(7, 40), "").replace(" - " + author, "");
                        String sendMsg = ("""
                                ======GitHub更新推送======
                                仓库: %s
                                分支: %s
                                提交时间: %s
                                提交信息: %s
                                """)
                                .formatted(strings1[0], strings1[1].replace("\\", ""), sd, TJ.replace(TJ.split(" ")[0], ""));
                        if (strings1[0].equals("MPS")) {
                            MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg);
                        } else {
                            MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg);
                        }
                    }
                }
            }
            if (String.valueOf(event.getChannel().getId()).equals("597470759799226409")) {
                if (!event.getMessage().getEmbeds().isEmpty()) {
                    for (Embed embeds : event.getMessage().getEmbeds()) {
                        Optional<EmbedAuthor> Oauthor = embeds.getAuthor();
                        Optional<String> Otitle = embeds.getTitle();
                        Optional<String> Odes = embeds.getDescription();

                        if (Oauthor.isPresent() && Otitle.isPresent() && Odes.isPresent()) {
                            String title = Otitle.get();
                            String author = Oauthor.get().getName();
                            String des = Odes.get();

                            if (author.equals("MohistBOT")) return;
                            String ck = title.split(" ")[0];
                            String titles = title.split(":")[1];

                            if (ck.equals("maven")) {
                                return;
                            }

                            String sendMsg = """
                                    ======GitHub issues======
                                    仓库: %s
                                    标题: %s
                                    序号: %s
                                    状态: %s
                                    笔者: %s
                                    内容: %s
                                    """;

                            String sendMsg0 = """
                                    ======GitHub issues======
                                    仓库: %s
                                    标题: %s
                                    序号: %s
                                    状态: %s
                                    执行者: %s
                                    """;


                            String issuesNa = title.split(":")[0];
                            String[] a = issuesNa.split(" ");
                            String b = a[a.length - 1];
                            if (b.startsWith("#")) {
                                sendMsg = sendMsg.formatted(ck, title.split(":")[1], b, "新回复", author, des);
                                MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg.replace("\\", ""));
                            }

                            String xhS = (title.split(":")[1]).split(" ")[1];
                            if (title.split(":")[0].endsWith("closed")) {
                                sendMsg0 = sendMsg0.formatted(ck, (title.split(":")[1]).replace(xhS + " ", ""), xhS, "关闭", author);
                                MohistAI.INSTANCE.QQ.getGroup(Account.mohistQQGGroup).sendMessage(sendMsg0.replace("\\", ""));
                            }

                            if (title.split(":")[0].endsWith("Issue opened")) {
                                sendMsg = sendMsg.formatted(ck, title.split(":")[1], b, "新鲜出炉的虫子", author, des);
                                sendMsgToGroup(sendMsg.replace("\\", ""));
                            }

                            if (title.split(":")[0].endsWith("Pull request opened")) {
                                sendMsg = sendMsg.formatted(ck, title.split(":")[1], b, "有新的小伙伴加入", author, des);
                                sendMsgToGroup(sendMsg.replace("\\", ""));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void sendMsgToGroup(final String msg) {
        MohistAI.sendMsgToGroup(Account.mohistQQGGroup, msg);
    }
}
