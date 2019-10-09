package me.trow.tgclan.eventos;

import me.trow.tgclan.Main;
import me.trow.tgclan.cmds.Clans;
import me.trow.tgclan.cmds.Info;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.itemnbtapi.NBTItem;

public class EventosInventario implements Listener{
	
	private static Main pl=Main.pl;
	
	public String getMsg(String msg){
		return pl.getMsg(msg);
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player)e.getWhoClicked();
			ItemStack is = e.getCurrentItem();
			if(is==null||is.getType()==Material.AIR) return;
			NBTItem nb = new NBTItem(is);
			if(nb.hasKey("searchClan")){
				e.setCancelled(true);
				// add player in arraylist to check the tag that he'll put in chat. This tag will be used to see Info's of the clan.
				if(Info.searchingTag.contains(p.getName())){
					p.sendMessage(getMsg("Msg.Digite_A_Tag_No_Chat"));
					return;
				}
				p.closeInventory();
				Info.searchingTag.add(p.getName());
				Info.removeConvite(p.getName());
				p.sendMessage(getMsg("Msg.Digite_A_Tag_No_Chat"));
				return;
			}
			if(nb.hasKey("paginaSeguinteClan")||nb.hasKey("paginaAnteriorClan")){
				e.setCancelled(true);
				int i = nb.hasKey("paginaSeguinteClan")?nb.getInteger("paginaSeguinteClan"):nb.getInteger("paginaAnteriorClan");
				new Clans().showGUI(p, i);
			}
		}
	}
	
}
