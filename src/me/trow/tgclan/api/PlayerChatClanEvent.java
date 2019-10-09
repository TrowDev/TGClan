package me.trow.tgclan.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChatClanEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private String player,tag,msg;
	private Player pl;
	private List<String> receivers=new ArrayList<>();
	private boolean cancelado;
	
	public PlayerChatClanEvent(Player play,String p,String t,String m,List<String> r){
		pl=play;
		player=p;tag=t;msg=m;
		receivers=r;
	}
	
	public void setCancelled(boolean b){
		cancelado=b;
	}
	
	public boolean isCancelled(){
		return cancelado;
	}
	
	public List<String> getReceivers(){
		return receivers;
	}
	
	public Player getPlayer(){
		return pl;
	}
	
	public String getTag(){
		return tag;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public void setMsg(String s){
		msg=s;
	}
	
	public void setTag(String s){
		tag=s;
	}
	
	public String getPlayerName(){
		return player;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}	
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
