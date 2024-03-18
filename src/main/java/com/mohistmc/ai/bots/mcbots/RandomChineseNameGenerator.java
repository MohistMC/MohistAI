package com.mohistmc.ai.bots.mcbots;

import java.util.Random;

public class RandomChineseNameGenerator {

    // 常见的中文姓氏
    private static final String[] COMMON_FAMILY_NAMES = {
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
            // ... 更多姓氏...
    };

    // 常见的中文单字名
    private static final String[] COMMON_FIRST_NAMES = {
            "伟", "芳", "秀", "丽", "强", "明", "磊", "娜", "静", "涛", "宇", "杰", "梅", "兰", "竹", "菊",
            // ... 更多名字...
    };

    public static void main(String[] args) {
        System.out.println(generateRandomName());
    }

    /**
     * 随机生成一个中文姓名
     * @return 一个随机的中文姓名
     */
    public static String generateRandomName() {
        Random random = new Random();

        // 选择一个随机的姓氏
        String familyName = COMMON_FAMILY_NAMES[random.nextInt(COMMON_FAMILY_NAMES.length)];

        // 选择两个随机的名字（单字名）
        String firstName = COMMON_FIRST_NAMES[random.nextInt(COMMON_FIRST_NAMES.length)];
        String secondName = COMMON_FIRST_NAMES[random.nextInt(COMMON_FIRST_NAMES.length)];

        return familyName + firstName + secondName;
    }
}
