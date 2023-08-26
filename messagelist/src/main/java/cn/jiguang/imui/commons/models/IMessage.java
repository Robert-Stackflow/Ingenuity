package cn.jiguang.imui.commons.models;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public interface IMessage extends Serializable {

    /**
     * Message id.
     *
     * @return unique
     */
    String getMsgId();

    /**
     * Get user info of message.
     *
     * @return UserInfo of message
     */
    IUser getFromUser();

    /**
     * Time of message.
     *
     * @return Date
     */
    Date getTime();

    /**
     * Time string that display in message list.
     *
     * @return Time string
     */
    String getTimeString();

    /**
     * Type of message
     *
     * @return integer
     */
    MessageType getType();

    MessageStatus getMessageStatus();

    /**
     * Text of message.
     *
     * @return text
     */
    String getText();

    /**
     * If message type is photo, voice, video or file,
     * get file path through this method.
     *
     * @return file path
     */
    String getFilePath();

    /**
     * If message type is photo, voice, video or file,
     * get file name through this method.
     *
     * @return file name
     */
    String getFileName();

    /**
     * If message type is photo, voice, video or file,
     * get file size through this method.
     *
     * @return file size
     */
    long getFileSize();

    /**
     * If message type is photo, voice, video or file,
     * get file type through this method.
     *
     * @return file type
     */
    String getFileType();

    /**
     * If message type is voice or video, get duration through this method.
     *
     * @return duration of audio or video, TimeUnit: SECONDS.
     */
    long getDuration();

    String getProgress();

    /**
     * Return extra key value of message
     *
     * @return {@link HashMap<>}
     */
    HashMap<String, String> getExtras();

    String getLocationName();

    String getLocationScreenShot();

    String getLocationDetail();

    double getLocationLatitude();

    double getLocationLongtitude();

    IUser getNameCardUser();

    /**
     * Type of Message
     */
    enum MessageType {
        EVENT, SEND_TEXT, RECEIVE_TEXT,

        SEND_IMAGE, RECEIVE_IMAGE,

        SEND_VOICE, RECEIVE_VOICE,

        SEND_VIDEO, RECEIVE_VIDEO,

        SEND_LOCATION, RECEIVE_LOCATION,

        SEND_FILE, RECEIVE_FILE,

        SEND_NAMECARD, RECEIVE_NAMECARD;

        MessageType() {
        }
    }

    /**
     * Status of message, enum.
     */
    enum MessageStatus {
        CREATED, SEND_GOING, SEND_SUCCEED, SEND_FAILED, SEND_DRAFT, RECEIVE_GOING, RECEIVE_SUCCEED, RECEIVE_FAILED
    }
}
