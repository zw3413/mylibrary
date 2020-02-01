package cn.cloudbed.entity.post;

import lombok.Data;

@Data
public class Pagination {
    Integer limit;
    Integer next;
    Integer page;
    Integer pages;
    Integer prev;
    Integer total;
}
