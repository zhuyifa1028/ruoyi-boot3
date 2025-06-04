package com.ruoyi.common.constant;

/**
 * 用户常量信息
 *
 * @author ruoyi
 */
public class UserConstants
{

    /** 正常状态 */
    public static final Character NORMAL = '0';

    /** 角色正常状态 */
    public static final String ROLE_NORMAL = "0";

    /** 角色封禁状态 */
    public static final String ROLE_DISABLE = "1";

    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";

    /** 部门停用状态 */
    public static final String DEPT_DISABLE = "1";

    /** 是否为系统默认（是） */
    public static final String YES = "Y";

    /** 是否菜单外链（是） */
    public static final Integer YES_FRAME = 0;

    /** 是否菜单外链（否） */
    public static final Integer NO_FRAME = 1;

    /** 菜单类型（目录） */
    public static final Character TYPE_DIR = 'M';

    /** 菜单类型（菜单） */
    public static final Character TYPE_MENU = 'C';

    /** Layout组件标识 */
    public final static String LAYOUT = "Layout";

    /** ParentView组件标识 */
    public final static String PARENT_VIEW = "ParentView";

    /** InnerLink组件标识 */
    public final static String INNER_LINK = "InnerLink";

    /** 校验是否唯一的返回标识 */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;
}
