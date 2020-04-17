package jogar.space.rankapi.setgroup;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.BaseComponent;

public class BookCreator implements Listener{
	@EventHandler
 public void RankSet(PlayerCommandPreprocessEvent e) {
	 if(e.getMessage().contains("/setrank")) {
		 Player p = e.getPlayer();
		 ItemStack book = new ItemStack(Material.WRITTEN_BOOK);	 
		 BookMeta bookmeta = (BookMeta)book.getItemMeta();
		 bookmeta.setAuthor("SPACE");
		 bookmeta.setTitle("§5§lSPACE");
		 bookmeta.setPage(1, "Sei lá");
		 
		 e.setCancelled(true);
	 }
	 
 }
}
