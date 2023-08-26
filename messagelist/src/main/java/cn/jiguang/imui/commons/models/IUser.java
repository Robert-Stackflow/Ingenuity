package cn.jiguang.imui.commons.models;


import java.io.Serializable;

public interface IUser extends Serializable {

    /**
     * User id.
     *
     * @return user id, unique
     */
    String getId();

    /**
     * Display name of user
     *
     * @return display name
     */
    String getDisplayName();

    String getDescribe();

    /**
     * Get user avatar file path.
     *
     * @return avatar file path
     */
    String getAvatarFilePath();

}
