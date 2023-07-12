package cc.yiueil.enums;

import lombok.Getter;

/**
 * UserStateEnum 用户状态实体
 *
 * @author 弋孓 YIueil@163.com
 * @version 1.0
 * @date 2023/6/22 16:24
 */
@Getter
public enum UserStateEnum {
    /**
     * normal: 正常
     * suspend: 挂起
     */
    normal("正常"), suspend("挂起");
    private final String state;

    UserStateEnum(String state) {
        this.state = state;
    }
}
