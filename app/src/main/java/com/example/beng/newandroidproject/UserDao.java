package com.example.beng.newandroidproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertUsers(User... users);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table where id IN (:userIds)")
    List<User> getUsersById(long[] userIds);

    @Query("UPDATE user_table SET total_correct = :totalCorrect WHERE id = :userId")
    void addCorrectCount(int userId, int totalCorrect);
}
