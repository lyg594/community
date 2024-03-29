package com.nowcoder.community.util;

/**
 * @ author   lyg
 * @ version  1.0
 */
public interface CommunityConstant {
    //激活成功过
    int ACTIVATION_SUCCEED = 0;
    //重复激活
    int ACTIVATION_REPEAT = 1;
    //激活失败
    int ACTIVATION_FAILURE = 2;
    //默认状态的登录凭证的超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    //记住状态下的登录凭证超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
