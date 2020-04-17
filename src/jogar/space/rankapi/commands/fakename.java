package jogar.space.rankapi.commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jogar.space.rankapi.objects.FakeTablist;
import jogar.space.rankapi.ranks.SpaceAPI;

public class fakename implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0 instanceof Player) {
			Player p = (Player)arg0;
			boolean usandoFake = false;
			try {
				if(p.getName().equalsIgnoreCase(SpaceAPI.getPlayerName(p))) {
				
				}else{
					usandoFake=true;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String uf = usandoFake + "";
			uf = uf.toUpperCase();
			try {
				new FakeTablist().addToTab("NickAleatorio");
				p.sendMessage("§a[Relatório] §fUsando fake: " +uf + ", nick real: " + p.getName() + ", nick falso: " + SpaceAPI.getPlayerName(p));
			} catch (SQLException e) {

			}
		}
		return false;
	}

}
