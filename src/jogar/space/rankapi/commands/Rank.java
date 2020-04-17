package jogar.space.rankapi.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.Herbystar.TTA.TTA_Methods;
import jogar.space.rankapi.objects.Permissao;

public class Rank implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = (Player)arg0;
		if(Permissao.temPermissaoCargo(p, "SETGROUP")) {
//			String s = "/give @p written_book 1 0 {pages:["[\"\",{\"text\":\" \\u0020 \\u0020 \\u0020 \\u0020 SPACE\",\"bold\":true,\"color\":\"dark_purple\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\" Função interativa para definir grupos no servidor, clique no grupo desejado e seja feliz!\\n\\n\\u2022 \",\"color\":\"black\"},{\"text\":\"Ajudante\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/setgroup player Ajudante\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clique aqui para definir o grupo Ajudante ao jogador.\"}},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2022 \",\"color\":\"black\"},{\"text\":\"Moderador\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/setgroup player Moderador\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clique aqui para definir o grupo Moderador ao jogador.\"}},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2022 \",\"color\":\"black\"},{\"text\":\"Admin\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/setgroup player Admin\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Clique aqui para definir o grupo Admin ao jogador.\"}}]"],title:Ranks,author:"jogar.space"}´
			if(arg3.length != 2) {
				p.sendMessage("§cUse /rank (jogador) (cargo) para definir um cargo a um jogador específico.");
				return false;
			}
			if(Bukkit.getPlayer(arg3[0]) == null) {
				p.sendMessage("§cEsse jogador não está online, para definir um grupo é §lOBRIGATÓRIO §cfalar com o promovido antes.");
				return false;
			}
			Player t = Bukkit.getPlayer(arg3[0]);
			if (t == p) {
				p.sendMessage("§cO Jogador não pode ser você mesmo.");
				return false;
			}
			try {
				Connection con = jogar.space.rankapi.database.Connection.getConnection();
				PreparedStatement st = con.prepareStatement("SELECT `id_cargo` FROM cargo WHERE `nome_cargo` = '"+arg3[1].toLowerCase()+"'");
				ResultSet rs = st.executeQuery();
				int id_cargo = 1;
				if(rs.next()) {
					id_cargo = rs.getInt("id_cargo");
				}
				PreparedStatement st2 = con.prepareStatement("UPDATE `jogador` SET `id_cargo`='"+id_cargo+"' WHERE  `nick_jogador`='"+t.getName()+"';");
				st2.executeUpdate();
				
				
				
				PreparedStatement st3 = con.prepareStatement("SELECT cargo.cor_cargo, cargo.nome_cargo FROM jogador, cargo WHERE nick_jogador = '"+t.getName()+"' AND cargo.id_cargo = jogador.id_cargo");
				ResultSet rs3 = st3.executeQuery();
				if(rs3.next()) {
					String grupo = rs3.getString("nome_cargo");
					String cor = rs3.getString("cor_cargo");
					for(Player all : Bukkit.getOnlinePlayers()) {
						TTA_Methods.sendTitle(all, cor + t.getName(), 10, 60, 10, "§fTeve seu grupo alterado para " + cor + grupo, 10, 100, 10);
					}
					t.sendMessage("§fVocê teve seu grupo alterado para " + cor + grupo + "§f!");
					
					con.close();
				}

				

			}catch(SQLException e) {
				e.printStackTrace();
				p.sendMessage("§cCargo incorreto, ou erro no sistema.");
				return false;
			}
			
			return false;
		}else{
			p.sendMessage("§cVocê não tem permissão.");
		}
		return false;
		
	}

}
