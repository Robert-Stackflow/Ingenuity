package com.cloudchewie.ingenuity.util.basic;

import android.content.Context;
import android.content.res.AssetManager;

import com.cloudchewie.ingenuity.entity.Activity;
import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.entity.Chatter;
import com.cloudchewie.ingenuity.entity.Comment;
import com.cloudchewie.ingenuity.entity.Draft;
import com.cloudchewie.ingenuity.entity.Message;
import com.cloudchewie.ingenuity.entity.Notice;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.entity.RequestAttraction;
import com.cloudchewie.ingenuity.entity.RequestTopic;
import com.cloudchewie.ingenuity.entity.Topic;
import com.cloudchewie.ingenuity.entity.TrackPoint;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;
import com.cloudchewie.util.basic.DateFormatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomainUtil {

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
    private static String[] attractionFileNameList = {"北京.csv", "成都.csv", "大连.csv", "东莞.csv", "广州.csv", "合肥.csv", "昆明.csv", "南京.csv", "宁波.csv", "青岛.csv", "厦门.csv", "上海.csv", "深圳.csv", "沈阳.csv", "石家庄.csv", "苏州.csv", "太原.csv", "天津.csv", "乌鲁木齐.csv", "无锡.csv", "武汉.csv", "西安.csv", "长沙.csv", "郑州.csv", "重庆.csv"};

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getPhone() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    public static Date getDate() {
        try {
            SimpleDateFormat simpleDateFormat = DateFormatUtil.getSimpleDateFormat(DateFormatUtil.YMD_FORMAT_WITH_BAR);
            Date endTime = simpleDateFormat.parse("2020-05-21 00:00:00");
            long endSeconds = endTime.getTime();
            Date startTime = new Date(System.currentTimeMillis());
            long startSeconds = startTime.getTime();
            long apartSeconds = endSeconds - startSeconds;
            long timeSeconds = (long) (apartSeconds * Math.random());
            long realSeconds = startSeconds + timeSeconds;
            String realTime = simpleDateFormat.format(realSeconds);
            return simpleDateFormat.parse(realTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static List<User> getUserList(Context context) {
        List<User> userList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("username.txt"), StandardCharsets.UTF_8));
            String line;
            int count = ((int) (Math.random() * 1000)) % 3;
            int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");
                for (String name : words) {
                    if (--randomJumpLineCount < 0) {
                        if (--count < 0) break;
                        User user = new User(((int) (Math.random() * 1000)), name, getPhone(), getContent(), '男', getDate(), getCity());
                        userList.add(user);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static Topic getTopic(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("topic.txt"), StandardCharsets.UTF_8));
            String line;
            int count = ((int) (Math.random() * 1000)) % 3;
            int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");
                for (String name : words) {
                    if (--randomJumpLineCount < 0) {
                        if (--count < 0) break;
                        return new Topic(name, getContent(), ((int) (Math.random() * 100000)), ((int) (Math.random() * 100000)), Topic.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Topic.FOLLOW_TYPE.values().length]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Topic("生活圈", getContent(), ((int) (Math.random() * 100000)), ((int) (Math.random() * 100000)), Topic.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Topic.FOLLOW_TYPE.values().length]);
    }

    public static List<Topic> getTopicList(Context context) {
        List<Topic> topicList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("topic.txt"), StandardCharsets.UTF_8));
            String line;
            int count = ((int) (Math.random() * 1000)) % 3;
            int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");
                for (String name : words) {
                    if (--randomJumpLineCount < 0) {
                        if (--count < 0) break;
                        Topic topic = new Topic(name, getContent(), ((int) (Math.random() * 100000)), ((int) (Math.random() * 100000)), Topic.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Topic.FOLLOW_TYPE.values().length]);
                        topicList.add(topic);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topicList;
    }

    public static List<String> getTagList(Context context) {
        List<String> tagList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("topic.txt"), StandardCharsets.UTF_8));
            String line;
            int count = ((int) (Math.random() * 1000)) % 5 + 3;
            int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");
                for (String name : words) {
                    if (--randomJumpLineCount < 0) {
                        if (--count < 0) break;
                        tagList.add(name);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    public static String getContent() {
        return "占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符占位符";
    }

    public static User getUser(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("username.txt"), StandardCharsets.UTF_8));
            String line;
            int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(",");
                for (String name : words) {
                    if (--randomJumpLineCount < 0) {
                        return new User(((int) (Math.random() * 1000)), name, getPhone(), getContent(), '男', getDate(), getCity());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new User(((int) (Math.random() * 1000)), "山风", getPhone(), getContent(), '男', getDate(), getCity());
    }

    public static String getCity() {
        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        return "";
    }

    public static List<Attraction> getAttractionList(Context context) {
        List<Attraction> attractionList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            assetManager.list("");
            for (String fileName : attractionFileNameList) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName), StandardCharsets.UTF_8));
                bufferedReader.readLine();
                String line;
                int count = ((int) (Math.random() * 1000)) % 3;
                int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
                while (--randomJumpLineCount > 0) bufferedReader.readLine();
                while ((line = bufferedReader.readLine()) != null && --count > 0) {
                    String[] words = line.split(",");
                    Attraction attraction = new Attraction(words[0], words[1] + "省" + words[2] + "市" + words[3] + words[6] + words[7] + words[0], getContent(), ((int) (Math.random() * 1000)), Double.parseDouble(words[10]), Double.parseDouble(words[11]), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getTagList(context), Attraction.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Attraction.FOLLOW_TYPE.values().length]);
                    attractionList.add(attraction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attractionList;
    }

    public static Attraction getAttraction(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            assetManager.list("");
            for (String fileName : attractionFileNameList) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName), StandardCharsets.UTF_8));
                bufferedReader.readLine();
                int count = ((int) (Math.random() * 1000)) % 3;
                int randomJumpLineCount = ((int) (Math.random() * 1000)) % 50;
                while (--randomJumpLineCount > 0) bufferedReader.readLine();
                String[] words = bufferedReader.readLine().split(",");
                return new Attraction(words[0], words[1] + "省" + words[2] + "市" + words[3] + words[6] + words[7] + words[0], getContent(), ((int) (Math.random() * 1000)), Double.parseDouble(words[10]), Double.parseDouble(words[11]), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getTagList(context), Attraction.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Attraction.FOLLOW_TYPE.values().length]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Attraction("东湖", "湖北省武汉市洪山区东湖", getContent(), ((int) (Math.random() * 1000)), 114.383034, 30.581009, ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getTagList(context), Attraction.FOLLOW_TYPE.values()[((int) (Math.random() * 100)) % Attraction.FOLLOW_TYPE.values().length]);
    }

    public static List<Message> getMessageList(Context context) {
        List<Message> messageList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        while (--count > 0)
            messageList.add(new Message(((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getDate(), getContent()));
        return messageList;
    }

    public static List<Chatter> getChatterList(Context context) {
        List<Chatter> chatterList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        while (--count > 0)
            chatterList.add(new Chatter(getUser(context), Chatter.CHATTER_STATE.values()[((int) (Math.random() * 1000)) % Chatter.CHATTER_STATE.values().length], Chatter.CHATTER_TYPE.values()[((int) (Math.random() * 1000)) % Chatter.CHATTER_TYPE.values().length], getMessageList(context)));
        return chatterList;
    }

    public static List<TrackPoint> getTrackPointList(Context context) {
        List<TrackPoint> trackPointList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        while (--count > 0) {
            Attraction attraction = getAttraction(context);
            trackPointList.add(new TrackPoint(attraction.getLatitude(), attraction.getLongtitude(), attraction.getName(), getDate()));
        }
        return trackPointList;
    }

    public static List<Activity> getActivityList(Context context) {
        List<Activity> activityList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        while (--count > 0)
            activityList.add(new Activity(1, getTopic(context).getName(), Activity.ACTIVITY_TYPE.values()[((int) (Math.random() * 1000)) % Activity.ACTIVITY_TYPE.values().length], getTopic(context).getName(), ((int) (Math.random() * 1000)), getTrackPointList(context)));
        return activityList;
    }

    public static Post getPost(Context context) {
        return new Post(getUser(context), getDate(), getContent(), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getAttraction(context), getTopic(context));
    }

    public static Article getArticle(Context context) {
        List<String> strings = getTagList(context);
        return new Article(getUser(context), getDate(), strings.size() > 0 ? strings.get(0) : "你好", getContent(), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getAttraction(context), getTopic(context));
    }

    public static List<Post> getPostList(Context context) {
        List<Post> postList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 30;
        while (--count > 0)
            postList.add(new Post(getUser(context), getDate(), getContent(), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getAttraction(context), getTopic(context)));
        return postList;
    }

    public static List<Post> getPostList(Context context, Object obj) {
        if (obj instanceof Topic) {
            List<Post> postList = new ArrayList<>();
            int count = ((int) (Math.random() * 1000)) % 15 + 3;
            while (--count > 0)
                postList.add(new Post(getUser(context), getDate(), getContent(), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), getAttraction(context), (Topic) obj));
            return postList;
        } else if (obj instanceof Attraction) {
            List<Post> postList = new ArrayList<>();
            int count = ((int) (Math.random() * 1000)) % 15 + 3;
            while (--count > 0)
                postList.add(new Post(getUser(context), getDate(), getContent(), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), ((int) (Math.random() * 1000)), (Attraction) obj, getTopic(context)));
            return postList;
        } else {
            return getPostList(context);
        }
    }

    public static Comment getComment(Context context, int level) {
        return new Comment(getUser(context), getDate(), getContent(), ImageUrlUtil.getUrls(3), ((int) (Math.random() * 1000)), Comment.COMMENT_TYPE.values()[((int) (Math.random() * 1000)) % Comment.COMMENT_TYPE.values().length], getCommentList(context, --level));
    }

    public static List<Comment> getCommentList(Context context, int level) {
        List<Comment> commentList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 10;
        while (--count > 0 && level > 0)
            commentList.add(new Comment(getUser(context), getDate(), getContent(), ImageUrlUtil.getUrls(3), ((int) (Math.random() * 1000)), Comment.COMMENT_TYPE.values()[((int) (Math.random() * 1000)) % Comment.COMMENT_TYPE.values().length], getCommentList(context, --level)));
        return commentList;
    }

    public static List<Notice> getNoticeList(Context context, Notice.NOTICE_TYPE noticeType) {
        List<Notice> noticeList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        while (--count > 0)
            noticeList.add(new Notice(getUser(context), getDate(), noticeType, getPost(context), getComment(context, 3)));
        return noticeList;
    }

    public static List<Draft> getDraftList(Context context, Draft.DRAFT_TYPE draftType) {
        List<Draft> noticeList = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % 15 + 3;
        switch (draftType) {
            case POST:
                while (--count > 0)
                    noticeList.add(new Draft(getPost(context), getDate(), getDate(), draftType));
                break;
            case ARTICLE:
                while (--count > 0)
                    noticeList.add(new Draft(getArticle(context), getDate(), getDate(), draftType));
                break;
            case TOPIC:
                while (--count > 0)
                    noticeList.add(new Draft(new RequestTopic(getTopic(context), getContent(), ImageUrlUtil.getUrls(5)), getDate(), getDate(), draftType));
                break;
            case ATTRACTION:
                while (--count > 0)
                    noticeList.add(new Draft(new RequestAttraction(getAttraction(context), getContent(), ImageUrlUtil.getUrls(5)), getDate(), getDate(), draftType));
                break;
        }
        return noticeList;
    }
}
