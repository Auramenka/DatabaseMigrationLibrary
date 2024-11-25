package com.innowise.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Migration {

    private int version;
    private String sql;
    private int checksum;

}
