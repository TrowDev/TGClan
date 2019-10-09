package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.api.PlayerChatClanEvent;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Chat {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length ==0){
			p.sendMessage("§cUse: §a/. §6<Msg>");
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		String msg="";
		for(int i=0;i<args.length;i++){
			msg=msg+args[i]+" ";
		}
		List<String> receivers=new ArrayList<>();
		String clanTag=cp.getTagClan();
		for(ClanPlayer cp2:cp.getClan().getMembros()){
			if(Bukkit.getPlayer(cp2.getPlayer())==null)continue;
			receivers.add(cp2.getPlayer());
		}
		String prefix=cp.getTagClanChat();
		PlayerChatClanEvent pchatce = new PlayerChatClanEvent(p, p.getName(), prefix, msg, receivers);
		Bukkit.getPluginManager().callEvent(pchatce);
		if(pchatce.isCancelled()) return;
		//String formato="{prefix} §a{nome}§f: §a{msg}".replace("{prefix}", prefix).replace("{msg}", msg.replace("&", "§")).replace("{nome}", p.getName());
		String formato=getMsg("Config.Formato_Chat_Clan").replace("@prefix", pchatce.getTag()).replace("@msg", pchatce.getMsg().replace("&", "§")).replace("@nome", p.getName());
		for(String member:receivers){
			Player membro=Bukkit.getPlayer(member);
			if(membro==null)continue;
			membro.sendMessage(formato);
		}
	}
}
