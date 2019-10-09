package me.trow.tgclan.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSetHomeEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Location l;
	private boolean cancelado = false;
	
	public PlayerSetHomeEvent(Player p){
		player=p;
		l=p.getLocation();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Location getLocation(){
		return l;
	}
	
	public boolean isCancelled(){
		return cancelado;
	}
	
	public void setCancelled(boolean b){
		cancelado=b;
	}
	
	public HandlerList getHandlers(){
		return handlers;
	}	
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
}
