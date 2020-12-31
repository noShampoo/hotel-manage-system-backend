package com.xust.hotel.common.security;

import java.util.Random;

/**
 * @author bhj
 */
public class CodingUtil {

    private static final char[] CODING_ROLE = new char[]{
            'Q', 'W', 'E', '8', 'A', 'S', '2', 'D', 'Z', 'X', '9',
            '7', 'P', '5', 'I', 'K', '3', 'M', 'J', 'U', 'F', 'R',
            'V', 'Y', 'L', 'T', 'N', '6', 'B', 'G', 'H', 'C', '4'
    };

    private static final char REPEAT_ROLE = '0';

    private static final int BIN_LENGTH = CODING_ROLE.length;

    private static final int LIST_MIN_LENGTH = 6;

    private static final String ORDER_NO_START = "XUST7202-";

    private static final String SPLIT_CHAR = "-";

    /**
     * 生成user
     * @param id id
     * @return user
     */
    public static String generateUser(long id) {
        char[] buffer = new char[32];
        int charPos = 32;
        while ((id / BIN_LENGTH) > 0) {
            int ind = (int) (id % BIN_LENGTH);
            buffer[--charPos] = CODING_ROLE[ind];
            id /= BIN_LENGTH;
        }
        buffer[--charPos] = CODING_ROLE[(int) (id % BIN_LENGTH)];
        String str = new String(buffer, charPos, (32 - charPos));
        if (str.length() < LIST_MIN_LENGTH) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(REPEAT_ROLE);
            Random random = new Random();
            for (int i = 1; i < LIST_MIN_LENGTH - str.length(); i++) {
                stringBuilder.append(CODING_ROLE[random.nextInt(BIN_LENGTH)]);
            }
            str += stringBuilder.toString();
        }
        return str;
    }


    /**
     * 生成订单编号
     * @return order_no
     */
    public static String generateOrderNo() {
        String randomStr = String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
        String nowTime = String.valueOf(System.currentTimeMillis() / 10);
        return ORDER_NO_START + randomStr + nowTime;
    }

    /**
     * coding to id
     * @param code user
     * @return id
     */
    public static long getSourceByUser(String code) {
        char[] chars = code.toCharArray();
        long res = 0L;
        for (int i = 0; i < chars.length; i++) {
            int ind = 0;
            for (int j = 0; j < BIN_LENGTH; j++) {
                if (chars[i] == CODING_ROLE[j]) {
                    ind = j;
                    break;
                }
            }
            if (chars[i] == REPEAT_ROLE) {
                break;
            }
            if (i > 0) {
                res = res * BIN_LENGTH + ind;
            } else {
                res = ind;
            }
        }
        return res;
    }


}
