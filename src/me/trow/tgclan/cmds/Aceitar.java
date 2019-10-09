package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Aceitar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private Convidar convite = new Convidar();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 2){
			p.sendMessage(getMsg("Msg.Use_Aceitar"));
			return;
		}
		String clan = args[1];
		if(!convite.convite.containsKey(clan)){
			p.sendMessage(getMsg("Msg.Clan_Nao_Lhe_Convidou"));
			return;
		}
		if(!convite.convite.get(clan).equalsIgnoreCase(p.getName())){
			p.sendMessage(getMsg("Msg.Clan_Nao_Lhe_Convidou"));
			return;
		}
		Clan c = pl.getClan(clan);
		if(c==null){
			p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
			return;
		}
		ClanPlayer cp=pl.getMembro(p.getName());
		c.getMembros().add(cp);
		cp.setClan(c);
		utils.addPlayerClan(cp, c, false);
		convite.convite.remove(clan);
		Bukkit.broadcastMessage(getMsg("Msg.Entrou_Pro_Clan").replace("@player", p.getName()).replace("@clan", c.getColorTag()));
	}
	
}
