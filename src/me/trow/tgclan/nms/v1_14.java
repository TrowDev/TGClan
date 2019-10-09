package me.trow.tgclan.nms;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_14 {
	
	public static void sendMsg(Player p,TextComponent message){
		((CraftPlayer)p).spigot().sendMessage(message);
	}
	
}
