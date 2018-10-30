package com.example.beng.newandroidproject;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {User.class}, version = 5, exportSchema = false)
public abstract class UserRoomDatabase extends RoomDatabase{
    public abstract UserDao userDao();
    private static UserRoomDatabase INSTANCE;

    public static UserRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (UserRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserRoomDatabase.class, "user_table")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final UserDao userDao;

        public PopulateDbAsync(UserRoomDatabase db){
            this.userDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            userDao.deleteAll();
//            User user = new User("haha");
//            userDao.insert(user);
//            User myUser = new User("hoho");
//            userDao.insert(myUser);
            return null;
        }
    }
}
