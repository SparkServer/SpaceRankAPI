package jogar.space.rankapi.database;
import java.sql.*;

import org.bukkit.Bukkit;

import com.mysql.jdbc.Driver;

public class Connection {
	public static java.sql.Connection getConnection() {
		java.sql.Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/space", "root", "");
			return con;
		} catch (SQLException e) {
			System.out.println("Erro no MySql.");
			Bukkit.getServer().shutdown();
			e.printStackTrace();
			return null;
		}
		
	}
}
 	