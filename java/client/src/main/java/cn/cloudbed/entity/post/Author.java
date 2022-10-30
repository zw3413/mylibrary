package cn.cloudbed.entity.post;

import lombok.Data;

import java.util.List;

@Data
public class Author {
    String accessibility;
    String bio;
    String cover_image;
    String created_at;
    String email;
    String facebook;
    String id;
    String last_seen;
    String location;
    String meta_description;
    String meta_title;
    String name;
    String profile_image;
    List<Role> roles;
    String slug;
    String status;
    String tour;
    String twitter;
    String updated_at;
    String url;
    String website;
}
