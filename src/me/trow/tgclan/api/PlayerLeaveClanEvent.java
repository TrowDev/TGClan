package me.trow.tgclan.api;

import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveClanEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private ClanPlayer cp;
	private boolean cancelado;
	
	public PlayerLeaveClanEvent(ClanPlayer cp){
		this.cp=cp;
	}
	
	public void setCancelled(boolean b){
		cancelado=b;
	}
	
	public boolean isCancelled(){
		return cancelado;
	}
	
	public ClanPlayer getCP(){
		return cp;
	}
	
	public Clan getClan(){
		return cp.getClan();
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}	
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

}
