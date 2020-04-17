package jogar.space.rankapi.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jogar.space.rankapi.objects.Permissao;
import jogar.space.rankapi.ranks.SpaceAPI;

public class Punir implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player) {
			Player p = (Player)arg0;
			if(!(Permissao.temPermissaoCargo(p, "PUNIR"))) {
				p.sendMessage("§cVocê não tem permissão.");
				return false;
			}
			if(arg3.length == 0) {
				p.sendMessage("§cUse /punir (jogador) (motivo) (prova) para efetuar uma punição.");
				return false;
			}

			if(arg3.length == 1) {
				if(Permissao.temPermissaoCargo(p, "PUNIR_SEM_MOTIVO")) {
					if(Bukkit.getPlayer(arg3[0]) == null) {
						OfflinePlayer t = Bukkit.getOfflinePlayer(arg3[0]);
						for(Player on : Bukkit.getOnlinePlayers()) {
							if(Permissao.temPermissaoCargo(on, "PUNIR")) {
								on.sendMessage("§c * " + SpaceAPI.getPrefixFromPlayer(t.getPlayer()) + t.getName() + "§c foi banido por " + SpaceAPI.getPrefixFromPlayer(p) + p.getName() + "§c.\n§c * Motivo: Não Informado\n §c* Duração: Permanente");
							}
						}
						Connection con = jogar.space.rankapi.database.Connection.getConnection();
						
						try {
							PreparedStatement st = con.prepareStatement("INSERT INTO `space`.`dump` (`info_dump`, `argumento_dump`, `player_dump`) VALUES ('PUNIR_BAN', 'Não informado', '"+t.getPlayer().getName()+"');");
							st.executeUpdate();
							PreparedStatement st2 = con.prepareStatement("INSERT INTO `space`.`punicao` (`datahora_punicao`, `nick_staff_punicao`, `nick_punido_punicao`, `id_motivopunicao`) VALUES ('2020-04-16 10:35:14', '"+p.getName()+"', '"+t.getName()+"', '3');");
							st2.executeUpdate();
							con.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						Player t = Bukkit.getPlayer(arg3[0]);
						for(Player on : Bukkit.getOnlinePlayers()) {
							if(Permissao.temPermissaoCargo(on, "PUNIR")) {
								on.sendMessage("\n§c * " + SpaceAPI.getPrefixFromPlayer(t) + t.getName() + "§c foi banido por " + SpaceAPI.getPrefixFromPlayer(p) + p.getName() + "§c.\n§c * Motivo: Não Informado\n §c* Duração: Permanente\n");
								
							}
							on.sendMessage("");
							on.sendMessage("\n§e * " + SpaceAPI.getPrefixFromPlayer(t) + t.getName() + " §efoi banido. Motivo: Não informado.\n§c");
							on.sendMessage("");
							Connection con = jogar.space.rankapi.database.Connection.getConnection();
							try {
								PreparedStatement st = con.prepareStatement("INSERT INTO `space`.`punicao` (`datahora_punicao`, `nick_staff_punicao`, `nick_punido_punicao`, `id_motivopunicao`) VALUES ('2020-04-16 10:35:14', '"+p.getName()+"', '"+t.getName()+"', '3');");
								st.executeUpdate();
								con.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							t.kickPlayer("§5§lSPACE\n\n§cVocê foi banido!\n§cMotivo: Não informado.\n§cAutor: " + SpaceAPI.getPrefixFromPlayer(p) + p.getName());
							
						}
						
					}
				}else{
					p.sendMessage("§cUse /punir (jogador) (motivo) (prova) para efetuar uma punição.");
					return false;
				}
			}else{
				
			}
		}
		return false;
	}

}
