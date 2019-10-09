package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Negar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private Convidar convite = new Convidar();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length <2){
			p.sendMessage(getMsg("Msg.Use_Negar"));
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
		//utils.addPlayerClan(p.getName(), clan, false);
		p.sendMessage(getMsg("Msg.Convite_Negado"));
		List<String> aviso=new ArrayList<>();
		for(String msg:cnf.getStringList("Msg.Negou_Convite")){
			msg=msg.replace("&", "§").replace("@player", p.getName());
			aviso.add(msg);
		}
		Clan c = pl.getClan(clan);
		c.sendMsgLideres(aviso,p.getName());
		convite.convite.remove(clan);
		//pl.avisarLideres(c.getID(), aviso);
	}
	
}
