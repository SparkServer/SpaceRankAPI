package jogar.space.rankapi.events;

import java.sql.SQLException;
import java.util.IllegalFormatException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import jogar.space.rankapi.ranks.SpaceAPI;

public class ChatEvents implements Listener{
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(SpaceAPI.getPrefixFromPlayer(e.getPlayer()).length() == 2) {
			try {
				e.setFormat(SpaceAPI.getPrefixFromPlayer(e.getPlayer()) + SpaceAPI.getPlayerName(e.getPlayer()) + "§7: " + e.getMessage());
			} catch (IllegalFormatException | NullPointerException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			try {
				e.setFormat(SpaceAPI.getPrefixFromPlayer(e.getPlayer()) + SpaceAPI.getPlayerName(e.getPlayer()) + "§7: §f" + e.getMessage().replace("&", "§"));
			} catch (IllegalFormatException | NullPointerException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}
