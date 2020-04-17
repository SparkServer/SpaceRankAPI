package jogar.space.rankapi.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import jogar.space.rankapi.ranks.SpaceAPI;

public class Timer {
 @SuppressWarnings("deprecation")
public static void runReGroupTimer() {
	 System.out.println("Caiu aqui :D");
     BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
     scheduler.scheduleAsyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("SpaceAPI"), new Runnable() {
         @Override
         public void run() {
//        	 System.out.println("Teste");
             for(Player p : Bukkit.getOnlinePlayers()) {
            	 TabList.sc.getTeam(p.getName() + "").setPrefix(SpaceAPI.getPrefixFromPlayer(p));
//            	 Bukkit.getConsoleSender().sendMessage("§a[Relatório] Atualizando grupos dos jogadores.");
             }
         }
     }, 200L, 200L);
 }
 @SuppressWarnings("deprecation")
public static void dumpPunir() {
	 BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	 scheduler.scheduleAsyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("SpaceAPI"), new Runnable() {
		
		@Override
		public void run() {
			Connection con = jogar.space.rankapi.database.Connection.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("SELECT `player_dump`,`argumento_dump` from dump WHERE `info_dump`='PUNIR_BAN';");
				ResultSet rs = st.executeQuery();
				if(rs.next()) {
					String player = rs.getString("player_dump");
					String arg = rs.getString("argumento_dump");
					if(Bukkit.getPlayer(player) != null) {
						for(Player on : Bukkit.getOnlinePlayers()) {
							Player t = Bukkit.getPlayer(player);
							on.sendMessage("");
							on.sendMessage("§e * " + SpaceAPI.getPrefixFromPlayer(t) + t.getName() + " §efoi banido. Motivo: "+arg+"");
						    on.sendMessage("");
							t.kickPlayer("§5§lSPACE\n\n§cVocê foi banido!\n§cMotivo: Não informado.\n");
						}
						Connection con2 = jogar.space.rankapi.database.Connection.getConnection();
						con2.close();
						PreparedStatement st2 = con2.prepareStatement("DELETE FROM `space`.`dump` WHERE  `player_dump`='Plugner' LIMIT 1;");
						st2.executeUpdate();
					}
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}, 100L, 100L);
	 
 }
@SuppressWarnings("deprecation")
public static void dumpOnline() {
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	scheduler.scheduleAsyncRepeatingTask(Bukkit.getServer().getPluginManager().getPlugin("SpaceAPI"), new Runnable() {
		@Override
		public void run() {
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				// Continuar
			}
			
		}
	},200L,200L);
}
}
