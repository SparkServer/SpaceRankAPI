package jogar.space.rankapi.events;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import jogar.space.rankapi.commands.Fake;
import jogar.space.rankapi.objects.TabList;
import jogar.space.rankapi.ranks.SpaceAPI;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;

public class StarterEvent implements Listener{
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		System.out.println("OnLogin");
		setOnline(e.getPlayer(), true);
		SpaceAPI.isPunido(e.getPlayer());
		
		
		
		try {
			e.setJoinMessage(null);
			e.setJoinMessage("§a[Relatório] §fBem-vindo, " + SpaceAPI.getPlayerName(e.getPlayer()));
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Connection con = jogar.space.rankapi.database.Connection.getConnection();
		PreparedStatement st;
		
		
		try {
			st = con.prepareStatement("SELECT * FROM jogador WHERE nick_jogador = '"+e.getPlayer().getName()+"'; ");
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				if(rs.getString("fake_nick_jogador") == null || rs.getString("fake_nick_jogador").equals("")) {
					con.close();
					e.getPlayer().setCustomName(e.getPlayer().getName());
					TabList.setPrefix(e.getPlayer());
					return;
				}
				String fake_nick = rs.getString("fake_nick_jogador");
//				SpaceAPI.setCustomName(e.getPlayer(), fake_nick);
				System.out.println(fake_nick);
				e.getPlayer().setCustomName(fake_nick);
				con.close();
				
				Fake.disguise(e.getPlayer(), fake_nick);
				TabList.setPrefix(e.getPlayer());
				return;
			}else{
				PreparedStatement st2 = con.prepareStatement("INSERT INTO `jogador` (`nick_jogador`, `prim_login_jogador`, `id_cargo`) VALUES ('"+e.getPlayer().getName()+"', '"+new Date().getYear()+"-"+new Date().getMonth()+"-"+new Date().getDay()+"', '1');");
				st2.executeUpdate();
				con.close();
				TabList.setPrefix(e.getPlayer());
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.getPlayer().setCustomName(e.getPlayer().getName());
		TabList.setPrefix(e.getPlayer());



		
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		setOnline(e.getPlayer(), false);
		e.setQuitMessage(null);
		TabList.unSetPrefix(e.getPlayer());
	}
	
	public static void setOnline(Player p, boolean online) {
		Connection con = jogar.space.rankapi.database.Connection.getConnection();
		try {
			if(online) {
				PreparedStatement st = con.prepareStatement("UPDATE `space`.`jogador` SET `online_jogador`='1' WHERE  `nick_jogador`='"+p.getName()+"';");
				st.executeUpdate();
			}else{
				PreparedStatement st = con.prepareStatement("UPDATE `space`.`jogador` SET `online_jogador`='0' WHERE  `nick_jogador`='"+p.getName()+"';");
				st.executeUpdate();
			}
			
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
