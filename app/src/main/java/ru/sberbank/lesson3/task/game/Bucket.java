package ru.sberbank.lesson3.task.game;

import java.io.Serializable;

import lombok.Data;

@Data
public class Bucket implements Serializable {
    private int bucketId;
    private boolean isVisible;

    public static Bucket of(int bucketId) {
        return new Bucket(bucketId);
    }

    public Bucket(int bucketId) {
        this.bucketId = bucketId;
    }
}
