package com.sug.core.util;

import java.util.regex.Pattern;

/**
 * 正则工具类
 * 提供验证邮箱、手机号、电话号码、身份证号码、数字等方法
 */
public final class RegexUtils {

    public static final String REGEX_IDCARD = "((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}((((19|20)?\\d{2})(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(((19|20)?\\d{2})(0[13578]|1[02])31)|(((19|20)?\\d{2})02(0[1-9]|1\\d|2[0-8]))|((((19|20)?([13579][26]|[2468][048]|0[48]))|(2000))0229))\\d{3}(\\d|X|x)?";
    public static final String REGEX_EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    public static final String REGEX_MOBILE = "^(\\+\\d{2})?1[3-8]{1}\\d{9}$";
    public static final String REGEX_PHONE = "^(0[0-9]{2,3}-)([2-9][0-9]{6,7})$|^(\\+\\d{2})?1[3-8]{1}\\d{9}$";
    public static final String REGEX_PHONE_LAMDLINE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$|^0\\d{2,3}-?\\d{7,8}$";

    public static final String REGEX_CHINESE = "^[\\u4E00-\\u9FA5]+$";
    public static final String BRANK_NO = "^(\\d{16}|\\d{19})$";
    public static final String REGEX_ENGNNUM = "^[A-Za-z0-9]+$";
    public static final String REGEX_ENG = "^[A-Za-z]+$";
    public static final String REGEX_DATE_CODE = "20\\d{6}";
    public static final String TWO_DIGIT_NUMBER = "^([1-9]\\d*(\\.[0-9]*[1-9])?|0\\.[0-9]*[1-9]|0)$";
    public static final String REGEX_PASSWORD = "^[A-Za-z0-9]{6,12}$";
    public static final String REGEX_CHINESE_ENG_NUM = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
    public static final String REGEX_NUM = "^[0-9]*$";

    public static final String REGEX_NUM_MESSAGE = "must be a number";
    public static final String REGEX_MOBILE_MESSAGE = "must be mobile phone";
    public static final String REGEX_ENG_MESSAGE = "must be letter";
    public static final String REGEX_PHONE_LAMDLINE_MESSAGE = "Must be a mobile phone or landline";
    public static final String REGEX_ENGNNUM_MESSAGE = "must be letter or number";
    public static final String REGEX_PASSWORD_MESSAGE = "must be letter or number,size between 6 and 12";
    public static final String REGEX_CHINESE_MESSAGE = "must be Chinese";
    public static final String REGEX_CHINESE_ENG_NUM_MESSAGE = "must be Chinese or letter or number";
    /**
     * 验证英文与数字
     * @param string 英文与数字20位，格式：ZXCVBASDFG1234567890
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEngnNum(String string) {

        return Pattern.matches(REGEX_ENGNNUM, string);
    }

    /**
     * 验证Email
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {

        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 验证身份证号码
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {

        return Pattern.matches(REGEX_IDCARD,idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {

        return Pattern.matches(REGEX_MOBILE,mobile);
    }

    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *  数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex,decimals);
    }

    /**
     * 验证空白字符
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex,blankSpace);
    }

    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {

        return Pattern.matches(REGEX_CHINESE,chinese);
    }

    /**
     * 验证日期（年月日）
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex,birthday);
    }

    /**
     * 验证URL地址
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 匹配中国邮政编码
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 营业执照注册号(15位)
     * @param id id
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEnterpriseId(String id) {
        String regex = "[0-9]{15}";
        return Pattern.matches(regex,id);
    }
}