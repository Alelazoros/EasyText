package com.antonchuraev.vkmessenger.IdDatabase;

import androidx.room.*;

import java.util.List;

@Dao
public interface IdDAO {

	@Query("SELECT * FROM id")
	List<Id> getAll();

	@Insert
	void insert(Id id);

	@Update
	void update(Id id);

	@Delete
	void delete(Id id);

}
