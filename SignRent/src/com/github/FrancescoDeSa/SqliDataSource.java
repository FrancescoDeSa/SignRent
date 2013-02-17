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
		//plugin.getLogger().info("IL DB è: "+database);
		tableName = config.getTableName;
		columnName = config.getColumnName;
		columnDuration = config.getColumnDuration;
		columnLocation = config.getColumnLocation;
		columnPrice = config.getColumnPrice;
		columnRented = config.getColumnRented;
		columnExpire = config.getColumnExpire;
		//plugin.getLogger().info(columnName+", "+columnDuration+", "+columnLocation+", "+columnPrice+", "+columnRented+", "+columnExpire);
		this.plugin = plugin;
		connect();
		setup();
	}
	
	private synchronized void connect() throws ClassNotFoundException, SQLException {
		sqlite = new SQLite(plugin.getLogger(),plugin.getName(),plugin.getDataFolder().getAbsolutePath(),database);
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
			sqlite.query("CREATE TABLE " + tableName + "("
                    + "id INTEGER AUTO_INCREMENT,"
                    + columnName + " VARCHAR(255),"
                    + columnDuration + " INTEGER,"
                    + columnLocation + " VARCHAR(100),"
                    + columnPrice + " INTEGER,"
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
            pst = sqlite.prepare("SELECT * FROM "+tableName);
            rs = pst.executeQuery();
            while(rs.next()){
            	System.out.println("CARICO UNA RIGA!!!");
            	String player = rs.getString(columnName);
            	int duration = rs.getInt(columnDuration);
            	int price = rs.getInt(columnPrice);
            	boolean rented = rs.getBoolean(columnRented);
            	long expire = rs.getLong(columnExpire);
            	SerialBlock block = new SerialBlock(rs.getString(columnLocation));
            	Sign sign = new Sign(block,player,duration,price,rented,expire);
            	signs.add(sign);
            }
            plugin.getLogger().info("CARICAMENTO SIGNSESSION COMPLETATO!");
            return new SignSession(signs, this);
        } catch (SQLException ex) {
        	System.out.println("ERRORE NEL LOAD!!!");
        	plugin.getLogger().warning(ex.getMessage());
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
            		+"(" + columnName+","+columnDuration+","+columnLocation+","+columnPrice+","+columnRented+","+columnExpire+") "
            		+"VALUES (?,?,?,?,?,?);");
            pst.setString(1, sign.getProprietario());
            pst.setInt(2, sign.getDurata());
            pst.setString(3, sign.getCartello().toString());
            pst.setInt(4, sign.getPrezzo());
            pst.setBoolean(5, sign.isInuso());
            pst.setLong(6, sign.getScadenza());
            pst.executeUpdate();
            System.out.println("source: SAVE SU DB CON SUCCESSO!!!");
        } catch (SQLException ex) {
            System.out.println("source: SAVE SU DB FALLITO!!!");
        	System.out.println(ex.getMessage());
            return false;
        } finally {
        	close(pst);
        }
        return true;
	}

	public boolean update(Sign sign) {
		PreparedStatement pst = null;
        try {
            pst = sqlite.prepare(
            		"UPDATE "+tableName
            		+" SET "
            		+columnName+"=?,"//1
            		+columnDuration+"=?,"//2
            		+columnLocation+"=?,"//3
            		+columnPrice+"=?,"//4
            		+columnRented+"=?,"//5
            		+columnExpire+"=?"//6
            		+" WHERE "+columnLocation+"=?");//7
            pst.setString(1, sign.getProprietario());
            pst.setInt(2, sign.getDurata());
            pst.setString(3, sign.getCartello().toString());
            pst.setInt(4, sign.getPrezzo());
            pst.setBoolean(5, sign.isInuso());
            pst.setLong(6, sign.getScadenza());
            pst.setString(7, sign.getCartello().toString());
            pst.executeUpdate();
            System.out.println("source: UPDATE SU DB CON SUCCESSO!!!");
        } catch (SQLException ex) {
            System.out.println("source: UPDATE SU DB FALLITO!!!");
        	System.out.println(ex.getMessage());
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
		PreparedStatement pst = null;
        try {
            pst = sqlite.prepare(
            		"DELETE FROM "+tableName
            		+" WHERE "+columnLocation+"=?");//7
            pst.setString(1, sign.getCartello().toString());
            pst.executeUpdate();
            System.out.println("source: DELETE DAL DB CON SUCCESSO!!!");
        } catch (SQLException ex) {
            System.out.println("source: DELETE DAL DB FALLITO!!!");
        	System.out.println(ex.getMessage());
            return false;
        } finally {
        	close(pst);
        }
        return true;
	}

	
}
