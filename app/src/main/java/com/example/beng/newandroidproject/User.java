package com.example.beng.newandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class User implements Serializable{

    @ColumnInfo(name = "nama")
    private String nama;

    @ColumnInfo(name = "is_locked")
    private boolean isLocked;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "is_answering")
    private boolean isAnswering;

    @ColumnInfo(name = "total_correct")
    private Integer totalCorrect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Integer getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(Integer totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public boolean isAnswering() {
        return isAnswering;
    }

    public void setAnswering(boolean answering) {
        isAnswering = answering;
    }

    @Override
    public String toString() {
        return "User{" +
                "nama='" + nama + '\'' +
                ", isLocked=" + isLocked +
                ", id=" + id +
                ", isAnswering=" + isAnswering +
                ", totalCorrect=" + totalCorrect +
                '}';
    }
}
