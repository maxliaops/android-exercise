package com.rainsong.toutiao.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maxliaops on 17-3-22.
 */

public class GroupInfoEntity implements Serializable {

    /**
     * read_count : 1258358
     * video_id : 41cf9a762a544453b5064b19888070d2
     * media_name : 新华全媒头条
     * ban_comment : 0
     * abstract :
     * 重磅丨引领大国开放“开着门，世界能够走进中国，中国也才能走向世界。”习近平年初在达沃斯论坛上的生动描述，让全世界听懂了中国的全方位对外开放战略，给复苏乏力的世界经济带来融融暖意。党的十八大以来，中国以更加积极主动的姿态走向世界舞台中央，推动和引领全球开放合作新格局。
     * video_detail_info : {"show_pgc_subscribe":1,"video_preloading_flag":1,
     * "video_third_monitor_url":"","group_flags":32832,"direct_play":1,
     * "detail_video_large_image":{"url":"http://p3.pstatp.com/video1609/192200037bff05776412",
     * "width":580,"url_list":[{"url":"http://p3.pstatp.com/video1609/192200037bff05776412"},
     * {"url":"http://p6.pstatp.com/video1609/192200037bff05776412"},{"url":"http://p.pstatp
     * .com/video1609/192200037bff05776412"}],"uri":"video1609/192200037bff05776412",
     * "height":326},"video_id":"41cf9a762a544453b5064b19888070d2","video_watch_count":539677,
     * "video_type":0,"video_watching_count":0}
     * image_list : []
     * has_video : true
     * article_type : 0
     * tag : video_finance
     * has_m3u8_video : 0
     * keywords : 引领大国开放,大国,习近平,达沃斯论坛,微视频
     * video_duration : 388
     * label : 置顶
     * show_portrait_article : false
     * user_verified : 0
     * aggr_type : 1
     * cell_type : 0
     * article_sub_type : 0
     * group_flags : 32832
     * bury_count : 0
     * title : 重磅丨引领大国开放
     * ignore_web_transform : 1
     * source_icon_style : 5
     * tip : 0
     * hot : 0
     * share_url : http://toutiao.com/a6399726504889352450/?iid=6106322388&app=news_article
     * has_mp4_video : 0
     * source : 新华全媒头条
     * comment_count : 0
     * article_url : http://toutiao.com/group/6399726504889352450/
     * filter_words : []
     * share_count : 3322
     * stick_label : 置顶
     * rid : 20170322121947172017121008456082
     * publish_time : 1490096227
     * action_list : [{"action":1,"extra":{},"desc":""},{"action":3,"extra":{},"desc":""},
     * {"action":7,"extra":{},"desc":""},{"action":9,"extra":{},"desc":""}]
     * has_image : true
     * cell_layout_style : 1
     * tag_id : 6399726504889352000
     * video_style : 0
     * verified_content :
     * display_url : http://toutiao.com/group/6399726504889352450/
     * is_stick : true
     * large_image_list : [{"url":"http://p3.pstatp.com/video1609/192200037bff05776412",
     * "width":580,"url_list":[{"url":"http://p3.pstatp.com/video1609/192200037bff05776412"},
     * {"url":"http://p6.pstatp.com/video1609/192200037bff05776412"},{"url":"http://p.pstatp
     * .com/video1609/192200037bff05776412"}],"uri":"video1609/192200037bff05776412","height":326}]
     * item_id : 6399896640529892000
     * is_subject : false
     * stick_style : 1
     * show_portrait : false
     * repin_count : 7925
     * cell_flag : 11
     * user_info : {"verified_content":"","avatar_url":"http://p3.pstatp
     * .com/thumb/8928/8178406049","user_id":5592880512,"name":"新华全媒头条","follower_count":0,
     * "follow":false,"user_auth_info":"","user_verified":false,"description":""}
     * source_open_url : sslocal://profile?refer=video&uid=5592880512
     * level : 0
     * like_count : 2371
     * digg_count : 2371
     * behot_time : 1490156387
     * cursor : 1490156387999
     * url : http://toutiao.com/group/6399726504889352450/
     * preload_web : 0
     * user_repin : 0
     * label_style : 1
     * item_version : 0
     * media_info : {"user_id":5592880512,"verified_content":"","avatar_url":"http://p2.pstatp
     * .com/large/8928/8178406049","media_id":5592880512,"name":"新华全媒头条","recommend_type":0,
     * "follow":false,"recommend_reason":"","is_star_user":false,"user_verified":false}
     * group_id : 6399726504889352000
     * middle_image : {"url":"http://p2.pstatp.com/list/300x196/19220002cab2fade4124.webp","width":640,"url_list":[{"url":"http://p2.pstatp.com/list/300x196/19220002cab2fade4124.webp"},{"url":"http://p4.pstatp.com/list/300x196/19220002cab2fade4124.webp"},{"url":"http://p.pstatp.com/list/300x196/19220002cab2fade4124.webp"}],"uri":"list/19220002cab2fade4124","height":233}
     *
     *
     * gallary_image_count : 1
     */

    private int read_count;
    private String video_id;
    private String media_name;
    private int ban_comment;
    @SerializedName("abstract")
    private String content;
    private VideoDetailInfoEntity video_detail_info;
    private boolean has_video;
    private int article_type;
    private String tag;
    private int has_m3u8_video;
    private String keywords;
    private int video_duration;
    private String label;
    private boolean show_portrait_article;
    private int user_verified;
    private int aggr_type;
    private int cell_type;
    private int article_sub_type;
    private int group_flags;
    private int bury_count;
    private String title;
    private int ignore_web_transform;
    private int source_icon_style;
    private int tip;
    private int hot;
    private String share_url;
    private int has_mp4_video;
    private String source;
    private int comment_count;
    private String article_url;
    private int share_count;
    private String stick_label;
    private String rid;
    private long publish_time;
    private boolean has_image;
    private int cell_layout_style;
    private long tag_id;
    private int video_style;
    private String verified_content;
    private String display_url;
    private boolean is_stick;
    private long item_id;
    private boolean is_subject;
    private int stick_style;
    private boolean show_portrait;
    private int repin_count;
    private int cell_flag;
    private UserInfoEntity user_info;
    private String source_open_url;
    private int level;
    private int like_count;
    private int digg_count;
    private int behot_time;
    private long cursor;
    private String url;
    private int preload_web;
    private int user_repin;
    private int label_style;
    private int item_version;
    private MediaInfoEntity media_info;
    private long group_id;
    private Image middle_image;
    private int gallary_image_count;
    private List<Image> image_list;
    private List<?> filter_words;
    private List<MovieActionEntity> action_list;
    private List<Image> large_image_list;

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public int getBan_comment() {
        return ban_comment;
    }

    public void setBan_comment(int ban_comment) {
        this.ban_comment = ban_comment;
    }

    public String getContent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public VideoDetailInfoEntity getVideo_detail_info() {
        return video_detail_info;
    }

    public void setVideo_detail_info(VideoDetailInfoEntity video_detail_info) {
        this.video_detail_info = video_detail_info;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getHas_m3u8_video() {
        return has_m3u8_video;
    }

    public void setHas_m3u8_video(int has_m3u8_video) {
        this.has_m3u8_video = has_m3u8_video;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isShow_portrait_article() {
        return show_portrait_article;
    }

    public void setShow_portrait_article(boolean show_portrait_article) {
        this.show_portrait_article = show_portrait_article;
    }

    public int getUser_verified() {
        return user_verified;
    }

    public void setUser_verified(int user_verified) {
        this.user_verified = user_verified;
    }

    public int getAggr_type() {
        return aggr_type;
    }

    public void setAggr_type(int aggr_type) {
        this.aggr_type = aggr_type;
    }

    public int getCell_type() {
        return cell_type;
    }

    public void setCell_type(int cell_type) {
        this.cell_type = cell_type;
    }

    public int getArticle_sub_type() {
        return article_sub_type;
    }

    public void setArticle_sub_type(int article_sub_type) {
        this.article_sub_type = article_sub_type;
    }

    public int getGroup_flags() {
        return group_flags;
    }

    public void setGroup_flags(int group_flags) {
        this.group_flags = group_flags;
    }

    public int getBury_count() {
        return bury_count;
    }

    public void setBury_count(int bury_count) {
        this.bury_count = bury_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIgnore_web_transform() {
        return ignore_web_transform;
    }

    public void setIgnore_web_transform(int ignore_web_transform) {
        this.ignore_web_transform = ignore_web_transform;
    }

    public int getSource_icon_style() {
        return source_icon_style;
    }

    public void setSource_icon_style(int source_icon_style) {
        this.source_icon_style = source_icon_style;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getHas_mp4_video() {
        return has_mp4_video;
    }

    public void setHas_mp4_video(int has_mp4_video) {
        this.has_mp4_video = has_mp4_video;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public String getStick_label() {
        return stick_label;
    }

    public void setStick_label(String stick_label) {
        this.stick_label = stick_label;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public long getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(long publish_time) {
        this.publish_time = publish_time;
    }

    public boolean isHas_image() {
        return has_image;
    }

    public void setHas_image(boolean has_image) {
        this.has_image = has_image;
    }

    public int getCell_layout_style() {
        return cell_layout_style;
    }

    public void setCell_layout_style(int cell_layout_style) {
        this.cell_layout_style = cell_layout_style;
    }

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    public int getVideo_style() {
        return video_style;
    }

    public void setVideo_style(int video_style) {
        this.video_style = video_style;
    }

    public String getVerified_content() {
        return verified_content;
    }

    public void setVerified_content(String verified_content) {
        this.verified_content = verified_content;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public boolean isIs_stick() {
        return is_stick;
    }

    public void setIs_stick(boolean is_stick) {
        this.is_stick = is_stick;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public boolean isIs_subject() {
        return is_subject;
    }

    public void setIs_subject(boolean is_subject) {
        this.is_subject = is_subject;
    }

    public int getStick_style() {
        return stick_style;
    }

    public void setStick_style(int stick_style) {
        this.stick_style = stick_style;
    }

    public boolean isShow_portrait() {
        return show_portrait;
    }

    public void setShow_portrait(boolean show_portrait) {
        this.show_portrait = show_portrait;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    public int getCell_flag() {
        return cell_flag;
    }

    public void setCell_flag(int cell_flag) {
        this.cell_flag = cell_flag;
    }

    public UserInfoEntity getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoEntity user_info) {
        this.user_info = user_info;
    }

    public String getSource_open_url() {
        return source_open_url;
    }

    public void setSource_open_url(String source_open_url) {
        this.source_open_url = source_open_url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public int getBehot_time() {
        return behot_time;
    }

    public void setBehot_time(int behot_time) {
        this.behot_time = behot_time;
    }

    public long getCursor() {
        return cursor;
    }

    public void setCursor(long cursor) {
        this.cursor = cursor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPreload_web() {
        return preload_web;
    }

    public void setPreload_web(int preload_web) {
        this.preload_web = preload_web;
    }

    public int getUser_repin() {
        return user_repin;
    }

    public void setUser_repin(int user_repin) {
        this.user_repin = user_repin;
    }

    public int getLabel_style() {
        return label_style;
    }

    public void setLabel_style(int label_style) {
        this.label_style = label_style;
    }

    public int getItem_version() {
        return item_version;
    }

    public void setItem_version(int item_version) {
        this.item_version = item_version;
    }

    public MediaInfoEntity getMedia_info() {
        return media_info;
    }

    public void setMedia_info(MediaInfoEntity media_info) {
        this.media_info = media_info;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public Image getMiddle_image() {
        return middle_image;
    }

    public void setMiddle_image(Image middle_image) {
        this.middle_image = middle_image;
    }

    public int getGallary_image_count() {
        return gallary_image_count;
    }

    public void setGallary_image_count(int gallary_image_count) {
        this.gallary_image_count = gallary_image_count;
    }

    public List<Image> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<Image> image_list) {
        this.image_list = image_list;
    }

    public List<?> getFilter_words() {
        return filter_words;
    }

    public void setFilter_words(List<?> filter_words) {
        this.filter_words = filter_words;
    }

    public List<MovieActionEntity> getAction_list() {
        return action_list;
    }

    public void setAction_list(List<MovieActionEntity> action_list) {
        this.action_list = action_list;
    }

    public List<Image> getLarge_image_list() {
        return large_image_list;
    }

    public void setLarge_image_list(List<Image> large_image_list) {
        this.large_image_list = large_image_list;
    }

    public static class VideoDetailInfoEntity {
        /**
         * show_pgc_subscribe : 1
         * video_preloading_flag : 1
         * video_third_monitor_url :
         * group_flags : 32832
         * direct_play : 1
         * detail_video_large_image : {"url":"http://p3.pstatp
         * .com/video1609/192200037bff05776412","width":580,"url_list":[{"url":"http://p3.pstatp
         * .com/video1609/192200037bff05776412"},{"url":"http://p6.pstatp
         * .com/video1609/192200037bff05776412"},{"url":"http://p.pstatp
         * .com/video1609/192200037bff05776412"}],"uri":"video1609/192200037bff05776412",
         * "height":326}
         * video_id : 41cf9a762a544453b5064b19888070d2
         * video_watch_count : 539677
         * video_type : 0
         * video_watching_count : 0
         */

        private int show_pgc_subscribe;
        private int video_preloading_flag;
        private String video_third_monitor_url;
        private int group_flags;
        private int direct_play;
        private Image detail_video_large_image;
        private String video_id;
        private int video_watch_count;
        private int video_type;
        private int video_watching_count;

        public int getShow_pgc_subscribe() {
            return show_pgc_subscribe;
        }

        public void setShow_pgc_subscribe(int show_pgc_subscribe) {
            this.show_pgc_subscribe = show_pgc_subscribe;
        }

        public int getVideo_preloading_flag() {
            return video_preloading_flag;
        }

        public void setVideo_preloading_flag(int video_preloading_flag) {
            this.video_preloading_flag = video_preloading_flag;
        }

        public String getVideo_third_monitor_url() {
            return video_third_monitor_url;
        }

        public void setVideo_third_monitor_url(String video_third_monitor_url) {
            this.video_third_monitor_url = video_third_monitor_url;
        }

        public int getGroup_flags() {
            return group_flags;
        }

        public void setGroup_flags(int group_flags) {
            this.group_flags = group_flags;
        }

        public int getDirect_play() {
            return direct_play;
        }

        public void setDirect_play(int direct_play) {
            this.direct_play = direct_play;
        }

        public Image getDetail_video_large_image() {
            return detail_video_large_image;
        }

        public void setDetail_video_large_image(Image
                                                        detail_video_large_image) {
            this.detail_video_large_image = detail_video_large_image;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public int getVideo_watch_count() {
            return video_watch_count;
        }

        public void setVideo_watch_count(int video_watch_count) {
            this.video_watch_count = video_watch_count;
        }

        public int getVideo_type() {
            return video_type;
        }

        public void setVideo_type(int video_type) {
            this.video_type = video_type;
        }

        public int getVideo_watching_count() {
            return video_watching_count;
        }

        public void setVideo_watching_count(int video_watching_count) {
            this.video_watching_count = video_watching_count;
        }

    }

    public static class UserInfoEntity {
        /**
         * verified_content :
         * avatar_url : http://p3.pstatp.com/thumb/8928/8178406049
         * user_id : 5592880512
         * name : 新华全媒头条
         * follower_count : 0
         * follow : false
         * user_auth_info :
         * user_verified : false
         * description :
         */

        private String verified_content;
        private String avatar_url;
        private long user_id;
        private String name;
        private int follower_count;
        private boolean follow;
        private String user_auth_info;
        private boolean user_verified;
        private String description;

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getFollower_count() {
            return follower_count;
        }

        public void setFollower_count(int follower_count) {
            this.follower_count = follower_count;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public String getUser_auth_info() {
            return user_auth_info;
        }

        public void setUser_auth_info(String user_auth_info) {
            this.user_auth_info = user_auth_info;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class MediaInfoEntity {
        /**
         * user_id : 5592880512
         * verified_content :
         * avatar_url : http://p2.pstatp.com/large/8928/8178406049
         * media_id : 5592880512
         * name : 新华全媒头条
         * recommend_type : 0
         * follow : false
         * recommend_reason :
         * is_star_user : false
         * user_verified : false
         */

        private long user_id;
        private String verified_content;
        private String avatar_url;
        private long media_id;
        private String name;
        private int recommend_type;
        private boolean follow;
        private String recommend_reason;
        private boolean is_star_user;
        private boolean user_verified;

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public String getVerified_content() {
            return verified_content;
        }

        public void setVerified_content(String verified_content) {
            this.verified_content = verified_content;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public long getMedia_id() {
            return media_id;
        }

        public void setMedia_id(long media_id) {
            this.media_id = media_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRecommend_type() {
            return recommend_type;
        }

        public void setRecommend_type(int recommend_type) {
            this.recommend_type = recommend_type;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public String getRecommend_reason() {
            return recommend_reason;
        }

        public void setRecommend_reason(String recommend_reason) {
            this.recommend_reason = recommend_reason;
        }

        public boolean isIs_star_user() {
            return is_star_user;
        }

        public void setIs_star_user(boolean is_star_user) {
            this.is_star_user = is_star_user;
        }

        public boolean isUser_verified() {
            return user_verified;
        }

        public void setUser_verified(boolean user_verified) {
            this.user_verified = user_verified;
        }
    }

    public static class MovieActionEntity {
        /**
         * action : 1
         * extra : {}
         * desc :
         */

        private int action;
        private ExtraEntity extra;
        private String desc;

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public ExtraEntity getExtra() {
            return extra;
        }

        public void setExtra(ExtraEntity extra) {
            this.extra = extra;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static class ExtraEntity {
        }
    }

    public static class Image {
        /**
         * url : http://p3.pstatp.com/video1609/192200037bff05776412
         * width : 580
         * url_list : [{"url":"http://p3.pstatp.com/video1609/192200037bff05776412"},{"url":"http://p6.pstatp.com/video1609/192200037bff05776412"},{"url":"http://p.pstatp.com/video1609/192200037bff05776412"}]
         * uri : video1609/192200037bff05776412
         * height : 326
         */

        private String url;
        private int width;
        private String uri;
        private int height;
        private List<UrlItem> url_list;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public List<UrlItem> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<UrlItem> url_list) {
            this.url_list = url_list;
        }

        public static class UrlItem {
            /**
             * url : http://p3.pstatp.com/video1609/192200037bff05776412
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
