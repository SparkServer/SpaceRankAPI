package jogar.space.rankapi.objects;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolManager;
import com.factions.atlasfactions.AtlasAPI;

import jogar.space.rankapi.ranks.SpaceAPI;



public class TabList {
	public static Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
	
	public static void setPrefix(Player p) {
		Connection con = jogar.space.rankapi.database.Connection.getConnection();
		
		try {
			PreparedStatement st = con.prepareStatement("SELECT cargo.cor_cargo, cargo.nome_cargo FROM jogador, cargo WHERE nick_jogador = '"+p.getName()+"' AND cargo.id_cargo = jogador.id_cargo");
			ResultSet rs = st.executeQuery();
			String prefix = "";
			if(rs.next()) {
				if(rs.getString("nome_cargo").equalsIgnoreCase("membro")) {
					prefix = "§7";
				}else{
					prefix = rs.getString("cor_cargo") + "[" + rs.getString("nome_cargo") + "] ";
				}
				
			} else {
				con.close();
				return;
			}
			
			Team t_jogador = sc.registerNewTeam(p.getCustomName() + "");
			if(!p.getCustomName().equals(p.getName())) {
				
				 p.setPlayerListName("§b[MVP§6+§b] " + p.getCustomName());
				
				t_jogador.addPlayer(p);
				con.close();
				return;
			}else{
				if(Bukkit.getPluginManager().getPlugin("AtlasFactions") !=null) {
					if(AtlasAPI.getFactionGalaxyUser(t_jogador.getName()).getFaction() == null) {
						
					}else{
						t_jogador.setPrefix(prefix + " §7[" + AtlasAPI.getFactionGalaxyUser(t_jogador.getName()).getFaction().getTag());
						t_jogador.addPlayer(p);
						con.close();
						return;
					}
					
				}
				
				t_jogador.setPrefix(prefix);
				t_jogador.addPlayer(p);
				con.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void unSetPrefix(Player p) {
		sc.getTeam(p.getName() + "").unregister();
	}

}
