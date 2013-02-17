package com.github.FrancescoDeSa;


public interface DataSource {
	public SignSession load();
	public boolean save(Sign sign);
	public boolean delete(Sign sign);
	public boolean update(Sign sign);
}
