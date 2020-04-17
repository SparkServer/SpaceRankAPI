package jogar.space.rankapi.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import jogar.space.rankapi.objects.Permissao;
import jogar.space.rankapi.ranks.SpaceAPI;

public class Reports implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!(arg0 instanceof Player)) {return false;}
	    Player p = (Player)arg0;
	    if(Permissao.temPermissaoCargo(p, "REPORTS")) {
	    	Connection con = jogar.space.rankapi.database.Connection.getConnection();
	    	try {

	    		PreparedStatement st = con.prepareStatement("SELECT nick_jogador FROM jogador WHERE online_jogador=1");
    				
   				ResultSet rs = st.executeQuery();
   				Inventory inv = Bukkit.createInventory(null, 6*9, "§7Denúncias");
   				ItemStack papel = new ItemStack(Material.PAPER);
    			ItemMeta papelmeta = papel.getItemMeta();
    			papelmeta.setDisplayName("§7Estatísticas");
    			papel.setItemMeta(papelmeta);
//    			inv.setItem(4, papel);
    			int slot = 10;
				while (rs.next()) {
					
					String jogador = rs.getString("nick_jogador");
		    		PreparedStatement st2 = con.prepareStatement("SELECT datahora_report, nick_verificou_jogador FROM report where nick_jogador = '"+jogador+"' ORDER BY datahora_report DESC limit 20;");					
		       		ResultSet rs2 = st2.executeQuery();
	    			int primeiravez = 1;
	    			
	    			
	    			int vezes = 0;
	    			
	    			while (rs2.next()) {
	    				
	    					    			
	    				vezes++;
	    			}
	    			if(vezes > 0) {
		    			ItemStack cabeca = new ItemStack(Material.SKULL_ITEM,vezes, (short)SkullType.PLAYER.ordinal());
		    			SkullMeta c_meta = (SkullMeta)cabeca.getItemMeta();
		    			c_meta.setOwner(jogador);
		    			c_meta.setDisplayName(SpaceAPI.getNameWithPrefix(jogador));
		    			ArrayList<String> lore = new ArrayList<String>();
		    			lore.add("");
		    			lore.add("§7Total de denúncias: " + vezes);
		    			lore.add("");
		    			lore.add("§aClique para ir até o jogador.");
		    			c_meta.setLore(lore);
		    			cabeca.setItemMeta(c_meta);
		    			inv.setItem(slot, cabeca);
		    			slot++;
	    			}

	    			
		    	}
										
				p.openInventory(inv);
				con.close();
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }else{
	    	p.sendMessage("§cVocê não tem permissão.");
	    }
		return false;
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("§7Denúncias")) {
			if(!e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1].equals("§7Estatísticas")) {
				String player = e.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1];
				Player p = (Player) e.getWhoClicked();
				p.chat("/btp " + player);
			}
			e.setCancelled(true);

		}
	}
	
	

}
