package me.trow.tgclan.api;

import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEntryClanEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private ClanPlayer cp;
	private Clan c;
	private boolean cancelado,leader;
	
	public PlayerEntryClanEvent(ClanPlayer p,Clan c,boolean l){
		cp=p;
		this.c=c;
		leader=l;
	}
	
	public void setCancelled(boolean b){
		cancelado=b;
	}
	
	public boolean isCancelled(){
		return cancelado;
	}
	
	public void setLeader(boolean b){
		leader=b;
	}
	
	public boolean isLeader(){
		return leader;
	}
	
	public ClanPlayer getCP(){
		return cp;
	}
	
	public Clan getClan(){
		return c;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}	
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

}
