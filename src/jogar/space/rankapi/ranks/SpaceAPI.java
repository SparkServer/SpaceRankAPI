package jogar.space.rankapi.ranks;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import com.mojang.authlib.GameProfile;

import jogar.space.rankapi.commands.Fake;
import jogar.space.rankapi.commands.Punir;
import jogar.space.rankapi.commands.Rank;
import jogar.space.rankapi.commands.Report;
import jogar.space.rankapi.commands.Reports;
import jogar.space.rankapi.commands.fakename;
import jogar.space.rankapi.events.ChatEvents;
import jogar.space.rankapi.events.StarterEvent;
import jogar.space.rankapi.fake.FakeList;
import jogar.space.rankapi.objects.TabList;
import jogar.space.rankapi.objects.Timer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

public class SpaceAPI extends JavaPlugin{
 public void onEnable() {
	 handleCommands();
	 handleEvents();
	 FakeList.populate();
	 Timer.runReGroupTimer();
	 Timer.dumpPunir();
 }
 public void onDisable() {
	 for(Team t : TabList.sc.getTeams()) {
		 Bukkit.getConsoleSender().sendMessage("§a[Relatório] §fDeletando time " + t.getName());
		 t.unregister();
	 }
	 
 }
 
 public void handleCommands() {
	 this.getCommand("fakename").setExecutor(new fakename());
	 this.getCommand("fake").setExecutor(new Fake());
	 this.getCommand("rank").setExecutor(new Rank());
	 this.getCommand("punir").setExecutor(new Punir());
	 this.getCommand("report").setExecutor(new Report());
	 this.getCommand("reports").setExecutor(new Reports());
 }
 public void handleEvents() {
	 Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
	 Bukkit.getPluginManager().registerEvents(new StarterEvent(), this);
	 Bukkit.getPluginManager().registerEvents(new Reports(), this);
 }
 
 public static String getPlayerName(Player p) throws SQLException {
	 Connection con = jogar.space.rankapi.database.Connection.getConnection();
	 	String ss = "SELECT * FROM jogador WHERE nick_jogador = '"+p.getName()+"'; ";
		PreparedStatement st = con.prepareStatement(ss);
//		Bukkit.getConsoleSender().sendMessage("§a§l[LOG] " + ss);
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			String fake = rs.getString("fake_nick_jogador");
			if(fake == null || fake.equals("")) {
				return p.getName();
			}else{
				String fake_nick = rs.getString("fake_nick_jogador");
				return fake_nick;
			}
		
			
		
			
		}
		return p.getName();

 }
 
 public static void setCustomName(Player player, String name) {
     for (Player online : Bukkit.getOnlinePlayers()) {
         if (!(online.equals(player))) {
             PacketPlayOutEntityDestroy despawn = new PacketPlayOutEntityDestroy(player.getEntityId());
             PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
             try {
                 Field b = spawn.getClass().getDeclaredField("b");
                 b.setAccessible(true);
                 b.set(spawn, player.getUniqueId());
             } catch (Exception e) {
                 e.printStackTrace();
             }

             ((CraftPlayer) online).getHandle().playerConnection.sendPacket(despawn);
             ((CraftPlayer) online).getHandle().playerConnection.sendPacket(spawn);
         }
     }
 }
 public static String getPrefixFromPlayer(Player p) {
	 Connection con = jogar.space.rankapi.database.Connection.getConnection();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT cargo.cor_cargo, cargo.nome_cargo FROM jogador, cargo WHERE nick_jogador = '"+p.getName()+"' AND cargo.id_cargo = jogador.id_cargo");
			ResultSet rs = st.executeQuery();
			String prefix = "";
			if(rs.next()) {
				if(rs.getString("nome_cargo").equalsIgnoreCase("membro")) {
					prefix = "§7";
				}else{
					prefix = rs.getString("cor_cargo") + "[" + rs.getString("nome_cargo") + "] ";
				}
				return prefix;
				
			} else {
				con.close();
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
 }
 public static String getPrefixFromPlayer(String p) {
	 Connection con = jogar.space.rankapi.database.Connection.getConnection();
		PreparedStatement st;
		try {
			st = con.prepareStatement("SELECT cargo.cor_cargo, cargo.nome_cargo FROM jogador, cargo WHERE nick_jogador = '"+p+"' AND cargo.id_cargo = jogador.id_cargo");
			ResultSet rs = st.executeQuery();
			String prefix = "";
			if(rs.next()) {
				if(rs.getString("nome_cargo").equalsIgnoreCase("membro")) {
					prefix = "§7";
				}else{
					prefix = rs.getString("cor_cargo") + "[" + rs.getString("nome_cargo") + "] ";
				}
				return prefix;
				
			} else {
				con.close();
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
 }
 public static String getNameWithPrefix(Player p) {
	 return getNameWithPrefix(p) + p.getName();
 }
 public static String getNameWithPrefix(String p) {
	 return getPrefixFromPlayer(p) + p;
 }
 
 public static boolean isPunido(Player p) {
	 Connection con = jogar.space.rankapi.database.Connection.getConnection();
	 try {
		 PreparedStatement st = con.prepareStatement("SELECT nome_motivopunicao as motivo, DATE_FORMAT(datahoravalidade_punicao, '%d/%m/%Y %T') as tempo, nick_staff_punicao as staff FROM punicao, motivopunicao WHERE nick_punido_punicao = '"+p.getName()+"' AND motivopunicao.id_motivopunicao = punicao.id_motivopunicao AND ( datahoravalidade_punicao > NOW() OR DATE_FORMAT(datahoravalidade_punicao, '%Y') = '0000')");
		 ResultSet rs = st.executeQuery();
		 if(rs.next()) {
			 String tempo = rs.getString("tempo");
			 if(rs.getString("tempo").equals("00/00/0000 00:00:00")) {
				 tempo = "Permanente";
			 }
			 Bukkit.getConsoleSender().sendMessage("§a[Relatório] §fAté: " + rs.getString("tempo") + " Motivo: " + rs.getString("motivo"));
			 Player staff = Bukkit.getOfflinePlayer(rs.getString("staff")).getPlayer();
			 String fn_staff = SpaceAPI.getPrefixFromPlayer(staff) + staff.getName();
			 p.kickPlayer("§5§lSPACE\n\n§cVocê foi banido!\n§cMotivo: "+rs.getString("motivo")+"\n§cAutor: " + fn_staff + "\n§cPrazo: " + tempo);
			 return true;
		 }
		con.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return false;
 }
 
}
