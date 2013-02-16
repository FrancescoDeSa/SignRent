package com.github.FrancescoDeSa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import lib.PatPeter.SQLibrary.SQLite;

public class SqliDataSource implements DataSource{

    private String database;
    private String tableName;
    private String columnName;
    private String columnDuration;
    private String columnLocation;
    private String columnPrice;
    private String columnRented;
    private String columnExpire;
    
    private SQLite sqlite;
    private Connection con;
    private SignRent plugin;
	
    
	public SqliDataSource(SignRent plugin, Settings config) throws SQLException, ClassNotFoundException {
		database = config.getDatabase;
		tableName = config.getTableName;
		columnName = config.getColumnName;
		columnDuration = config.getColumnDuration;
		columnLocation = config.getColumnLocation;
		columnPrice = config.getColumnPrice;
		columnRented = config.getColumnRented;
		columnExpire = config.getColumnExpire;
		
		connect();
		setup();
	}
	
	private synchronized void connect() throws ClassNotFoundException, SQLException {
		sqlite = new SQLite(plugin.getLogger(),plugin.getName(),database,plugin.getDataFolder().getAbsolutePath());
		try{
			sqlite.open();
		}catch(Exception e){
			plugin.getLogger().warning(e.getMessage());
			plugin.getLogger().warning("Disabling SignRent");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
    }
	private synchronized void setup() throws SQLException {

		if(sqlite.isTable(tableName)){
			  return;
		}else{
			plugin.getLogger().info(tableName+" missing from database. Cration in process...");
			sqlite.query("CREATE TABLE" + tableName + " ("
                    + "id INTEGER AUTO_INCREMENT,"
                    + columnName + " VARCHAR(255),"
                    + columnDuration + " INT NOT NULL,"
                    + columnLocation + " VARCHAR(100)"
                    + columnPrice + " INT NOT NULL,"
                    + columnRented + " BOOLEAN,"
                    + columnExpire + " BIGINT,"
                    + "CONSTRAINT table_const_prim PRIMARY KEY (id));");
			plugin.getLogger().info(tableName+" created!");
		}
    }  
	@Override
	public SignSession load() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Sign> signs = new ArrayList<Sign>();
        try {
            pst = sqlite.prepare("SELECT * FROM"+tableName);
            rs = pst.executeQuery();
            while(rs.next()){
            	String player = rs.getString(columnName);
            	int duration = rs.getInt(columnDuration);
            	int price = rs.getInt(columnPrice);
            	boolean rented = rs.getBoolean(columnRented);
            	long expire = rs.getLong(columnExpire);
            	SerialBlock block = new SerialBlock(rs.getString(columnLocation));
            	Sign sign = new Sign(block,player,duration,price,rented,expire);
            	signs.add(sign);
            }
            return new SignSession(signs, this);
        } catch (SQLException ex) {
            return null;
        } finally {
        	close(pst);
        	close(rs);
        }
	}

	@Override
	public boolean save(Sign sign){
		PreparedStatement pst = null;
        try {
            pst = sqlite.prepare(
            		"INSERT INTO "+tableName
            		+"(" + columnName+","+columnDuration+","+columnLocation+","+columnPrice+","+columnRented+","+columnExpire+")"
            		+"VALUES (?,?,?,?,?);");
            pst.setString(1, sign.getProprietario());
            pst.setInt(2, sign.getDurata());
            pst.setString(3, sign.getCartello().toString());
            pst.setInt(4, sign.getPrezzo());
            pst.setBoolean(5, sign.isInuso());
            pst.setLong(6, sign.getScadenza());
            pst.executeUpdate();
        } catch (SQLException ex) {
            return false;
        } finally {
        	close(pst);
        }
        return true;
	}

      
    public synchronized void close() {
        try {
            con.close();
        } catch (SQLException ex) {
        }
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
            }
        }
    }

    @SuppressWarnings("unused")
	private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
            }
        }
    }

	@Override
	public boolean delete(Sign sign) {
		// TODO Auto-generated method stub
		return false;
	}
}
