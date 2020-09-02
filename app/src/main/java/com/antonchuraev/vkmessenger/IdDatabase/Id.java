package com.antonchuraev.vkmessenger.IdDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Id {

	@PrimaryKey
	public long id;

	public long number;

}
