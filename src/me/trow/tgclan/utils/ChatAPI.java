package me.trow.tgclan.utils;

//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
//import org.bukkit.entity.Player;

import me.trow.tgclan.nms.v1_11;
import me.trow.tgclan.nms.v1_12;
import me.trow.tgclan.nms.v1_13;
import me.trow.tgclan.nms.v1_14;
import me.trow.tgclan.nms.v1_8;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class ChatAPI {
	
	public static TextComponent text;
	
	public ChatAPI(String message){
		text = new TextComponent(message);
	}
	
	public ChatAPI setColor(ChatColor color){
		text.setColor(color);
		return this;
	}
	
	public ChatAPI setBold(boolean bold){
		if (bold == true){
			text.setColor(ChatColor.BOLD);
		}
		return this;
	}
	
	public ChatAPI Action(ClickEvent.Action action, String implementation){
		text.setClickEvent(new ClickEvent(action, implementation));
		return this;
	}
	
	public ChatAPI Hover(HoverEvent.Action hover, String implementation){
		text.setHoverEvent(new HoverEvent(hover, new ComponentBuilder(implementation).create()));
		return this;
	}
	
	public ChatAPI sendPlayers(Player user){
		//user.sendMessage(text);
		String version = Item.version;
		if(Item.versionCompare(version, "1.8")==0){
			v1_8.sendMsg(user, text);
		}
		if(Item.versionCompare(version, "1.11")==0){
			v1_11.sendMsg(user, text);
		}
		if(Item.versionCompare(version, "1.12")==0){
			v1_12.sendMsg(user, text);
		}
		if(Item.versionCompare(version, "1.13")==0){
			v1_13.sendMsg(user, text);
		}
		if(Item.versionCompare(version, "1.14")==0){
			v1_14.sendMsg(user, text);
		}
		//((CraftPlayer)user).spigot().sendMessage(text);
		return this;
	}

}
