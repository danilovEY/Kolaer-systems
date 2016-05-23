package ru.kolaer.admin.bid.mvp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidModelManager {
	private final Logger LOG = LoggerFactory.getLogger(BidModelManager.class);
	
	private final String DB_URL = "jdbc:mysql://mailkolaer:3306/zayavki?useUnicode=true&autoReconnect=false&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	private final String USER = "root";
	private final String PASS = "PEs68gGE8";
	private boolean isConnect = false;
	private Connection conn;
	
	public BidModelManager() {
		
	}
	
	public boolean connectToDB() {
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");

			LOG.info("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			LOG.info("Connecting TRUE");
			this.isConnect = true;
		}catch(Exception e){
			LOG.error("Ошибка при подключении к БД!", e);
			if(this.conn != null) {
				try{
					this.conn.close();
				}catch(SQLException e1){
					LOG.error("Не удалось закрыть коннект!", e1);
				}
			}
		}
		
		return this.isConnect;
	}
	
	public List<MBid> updateModel() {
		final List<MBid> bidList = new ArrayList<>();
		
		LOG.info("Creating statement...");
		try(final Statement stmt = conn.createStatement()) {
			
			final String sql = "SELECT * FROM ticket";
			ResultSet rs = stmt.executeQuery(sql);
	
			while(rs.next()){
				final MBid bid = new MBid();
				bid.setNumber(rs.getInt(1));
				bid.setInitials(rs.getString(3));
				bid.setDescription(rs.getString(5));
				bid.setStatus(this.getStatusById(rs.getInt(9)));
				bid.setExecutor(this.getUserNameById(rs.getInt(10)));
				
				final Timestamp timestampStart = rs.getTimestamp(11);
				if(timestampStart != null)
					bid.setDateStart(new Date(timestampStart.getTime()));
				
				final Timestamp timestampEnd = rs.getTimestamp(12);
				if(timestampEnd != null)
					bid.setDateEnd(new Date(timestampEnd.getTime()));

				bidList.add(bid);
			}
			rs.close();
		} catch(SQLException sqlEx) {
			LOG.error("Ошибка при получении данных с БД!", sqlEx);
		}
		
		return bidList;
	}

	public String getStatusById(int id) {
		switch(id) {
			case 1 : return "Рассмотрение";
			case 2 : return "Назначена";
			case 3 : return "В работе";
			case 4 : return "Закрыта";
			case 5 : return "Отложена";
			case 6 : return "Удалена";
			default : return "";
		}
	}
	
	public String getUserNameById(int id) {
		switch(id) {
			case 1 : return "Manager";
			case 2 : return "ДаниловЮВ";
			case 3 : return "ПавленкоСА";
			case 4 : return "МоскаленкоВО";
			case 5 : return "ПавленкоСВ";
			case 6 : return "КонстантиноваАС";
			case 7 : return "ЧерновИС";
			case 10 : return "ДаниловЕЮ";
			default : return "";
		}
	}
	
	public boolean isConnect() {
		return isConnect;
	}
}
