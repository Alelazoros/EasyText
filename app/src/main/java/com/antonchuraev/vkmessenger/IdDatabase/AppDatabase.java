package com.antonchuraev.vkmessenger.IdDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Id.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
	public abstract IdDAO idDAO();
}
