package cn.cloudbed.entity.post;

import lombok.Data;

import java.util.List;

@Data
public class Post {
    List<Author> authors;
    String canonical_url;
    String codeinjection_foot;
    String codeinjection_head;
    String comment_id;
    String created_at;
    String custom_excerpt;
    String custom_template;
    String excerpt;
    String feature_image;
    Boolean featured;
    String id;
    String meta_description;
    String meta_title;
    String mobiledoc;
    String html;
    String og_description;
    String og_image;
    String og_title;
    Author primary_author;
    Tag primary_tag;
    String published_at;
    String slug;
    String status;
    List<Tag> tags;
    String title;
    String twitter_description;
    String twitter_image;
    String twitter_title;
    String udpated_at;
    String url;
    String uuid;
    String updated_at;
}
