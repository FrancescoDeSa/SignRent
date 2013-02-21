package com.github.FrancescoDeSa;


public interface DataSource {
	public SignSession load();
	public boolean save(RSign sign);
	public boolean delete(RSign sign);
	public boolean update(RSign sign);
}
