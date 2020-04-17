package jogar.space.rankapi.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import jogar.space.rankapi.fake.FakeList;
import jogar.space.rankapi.objects.Permissao;
import net.haoshoku.nick.NickPlugin;
import net.haoshoku.nick.api.NickAPI;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class Fake implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!(arg0 instanceof Player)) {
			arg0.sendMessage("§cApenas jogadores podem efetuar essa ação.");
		}
		Player p = (Player)arg0;
		if(Permissao.temPermissaoCargo(p, "FAKE")) {
			if(arg3.length == 1) {
				String onff = arg3[0];
				if(onff.equalsIgnoreCase("on")) {
					String fake = FakeList.random();
					Connection con = jogar.space.rankapi.database.Connection.getConnection();
					try {
						if(temFake(p)) {
							
							p.sendMessage("§cVocê já está com um fake selecionado.");
							con.close();
							return false;
						}
						PreparedStatement st = con.prepareStatement("UPDATE `jogador` SET `fake_nick_jogador`='"+fake+"' WHERE  `nick_jogador`='"+p.getName()+"';");
						st.executeUpdate();
                        con.close();
                        p.kickPlayer("§cSeu fake foi definido com succeso!\n§7Nick novo: §7" + fake);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
				else if(onff.equalsIgnoreCase("off")) 
				{
					Connection con = jogar.space.rankapi.database.Connection.getConnection();
					try 
					{
						if(!temFake(p)) 
						{
								p.sendMessage("§cVocê não está com nenhum fake selecionado.");
								con.close();
								return false;
						}
						else
						{
								PreparedStatement st2 = con.prepareStatement("UPDATE `jogador` SET `fake_nick_jogador`='' WHERE  `nick_jogador`='"+p.getName()+"';");
								st2.executeUpdate();
								con.close();
								p.kickPlayer("§aSeu fake foi retirado com sucesso!");
						}
						
					} 
				    catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					p.sendMessage("§cUse /fake (on/off) para gerenciar seus fakes.");
				}
			}else{
				p.sendMessage("§cUse /fake (on/off) para gerenciar seus fakes.");
			}
	
		}else{
			p.sendMessage("§cVocê não tem permissão.");
		}


		
		return false;
}
	public static boolean temFake(Player p) {
		Connection con = jogar.space.rankapi.database.Connection.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("SELECT `fake_nick_jogador` FROM `jogador` WHERE `nick_jogador` = '"+p.getName()+"';");
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				if(rs.getString("fake_nick_jogador").equals("") || rs.getString("fake_nick_jogador") == null) {
					con.close();
					return false;
					
					
				}else{
					con.close();
					return true;
				}
			}

		}catch(SQLException e) {
			
		}
		return false;
}
	


	public static void disguise(Player player, String name) {
		NickPlugin.getPlugin().getAPI().setGameProfileName(player, name);
		NickPlugin.getPlugin().getAPI().setSkin(player, name);
		NickPlugin.getPlugin().getAPI().setSkinChangingForPlayer(true);
	}

}





