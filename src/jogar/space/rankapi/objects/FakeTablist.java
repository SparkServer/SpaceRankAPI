package jogar.space.rankapi.objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class FakeTablist {
 
 public void addToTab(String fake) {
	 GameProfile profile = new GameProfile(UUID.randomUUID(), fake);
	 MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
	 WorldServer world = server.getWorldServer(0);
	 PlayerInteractManager manager = new PlayerInteractManager(world);
	 EntityPlayer player = new EntityPlayer(server, world, profile, manager);
	 PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, player);
	 Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinEvent(Bukkit.getPlayer(player.getName()), null));
	 
	 for(Player online : Bukkit.getOnlinePlayers()) {
		    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
		}
 }
 
}
