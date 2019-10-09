package me.trow.tgclan.api;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCreateClanEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private String tag,nome,tagReplaced;
	private boolean cancelado;
	
	public PlayerCreateClanEvent(String tag,String nome){
		this.tag=tag.replace("&", "§");
		tagReplaced=tag.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", "");
		this.nome=nome;
	}
	
	public void setTag(String s){
		tag=s.replace("&", "§");
		tagReplaced=tag.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", "");
	}
	
	public boolean isCancelled(){
		return cancelado;
	}
	
	public void setCancelled(boolean b){
		cancelado=b;
	}
	
	public String getTag(){
		return tag;
	}
	
	public String getTagReplaced(){
		return tagReplaced;
	}
	
	public String getNome(){
		return nome;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}	
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
