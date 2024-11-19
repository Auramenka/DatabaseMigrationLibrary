package com.innowise.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MigrationResultRecords {

    private int version;
    private boolean isSuccess;
    private String message;
}
