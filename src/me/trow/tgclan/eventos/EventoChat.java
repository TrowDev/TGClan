package me.trow.tgclan.eventos;

import me.trow.tgclan.Main;
import me.trow.tgclan.cmds.Info;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class EventoChat implements Listener{
	
	private static Main pl = Main.pl;
	
	@EventHandler
	public void tagChatClan(ChatMessageEvent e){
		if(e.getTags().contains("tgclan")){
			Player p = e.getSender();
			ClanPlayer cp = pl.getMembro(p.getName());
			if(cp.temClan()){
				e.setTagValue("tgclan", pl.getConfig().getString("Config.Tag_Clan_Chat").replace("&", "§").replace("@clan", cp.getColorTag()));
			}
		}
	}
	
	@EventHandler
	public void chatSearchClan(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if(msg.startsWith("/")) return;
		if(Info.searchingTag.contains(p.getName())){
			e.setCancelled(true);
			p.chat("/clan info "+msg);
			Info.searchingTag.remove(p.getName());
		}
	}
	
}
