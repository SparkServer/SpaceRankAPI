package jogar.space.rankapi.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jogar.space.rankapi.ranks.SpaceAPI;

public class Report implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg3.length != 1) {
			arg0.sendMessage("§cUse /report (jogador) para denunciar um hacker.");
			return false;
		}
		if(!(arg0 instanceof Player)) {
			arg0.sendMessage("§cSeja um jogador.");
		}
		Connection con = jogar.space.rankapi.database.Connection.getConnection();
		String ps = arg3[0];
		if(Bukkit.getPlayer(ps) == null) {
			arg0.sendMessage("§cEsse jogador não está mais online, tente denunciar depois.");
			return false;
		}
		Player t = Bukkit.getPlayer(ps);
		Player p = (Player)arg0;
		
		try {
			String tp = SpaceAPI.getPrefixFromPlayer(t) + t.getName();
			p.sendMessage("");
			p.sendMessage("§a * Você reportou o jogador " + tp + "§a." + " Um membro de nossa equipe foi notificado e o comportamento deste jogador será analisado em breve.");
			p.sendMessage("");
			p.sendMessage("§a * O uso abusivo deste comando poderá causar em punição.");
			p.sendMessage("");
			String imprimir =  "INSERT INTO report (nick_jogador, datahora_report, nick_reportou_jogador) VALUES ('"+t.getName()+"', NOW(), '"+p.getName()+"');";
			p.sendMessage(imprimir);
			PreparedStatement st = con.prepareStatement(imprimir);
			st.executeUpdate();
			con.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	

}
