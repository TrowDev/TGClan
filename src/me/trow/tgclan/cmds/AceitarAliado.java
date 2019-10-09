package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AceitarAliado {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private Aliado aliado=new Aliado();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 2){
			p.sendMessage(getMsg("Msg.Use_Aceitar_Aliado"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		String seuClan=cp.getTagClan();
		String clan=args[1];
		Clan c = pl.getClan(clan);
		//p.sendMessage("§b> "+aliado.ally.toString());
		if(!aliado.ally.containsKey(clan)){
			p.sendMessage(getMsg("Msg.Nao_Pediu_Alianca"));
			return;
		}
		if(aliado.ally.containsKey(clan)&&!aliado.ally.containsValue(seuClan)){
			p.sendMessage(getMsg("Msg.Nao_Pediu_Alianca"));
			return;
		}
		if(cp.isAliado(c)){
			p.sendMessage(getMsg("Msg.Clan_Ja_E_Aliado"));
			return;
		}
		if(cp.isRival(c)){
			utils.removeRival(cp.getClan().getID(), clan);
		}
		utils.addAliado(cp.getClan().getID(), clan);
		utils.addAliado(c.getID(), seuClan);
		List<String> msg=new ArrayList<>();
		List<String> msg1=new ArrayList<>();
		String c1=cp.getColorTag();
		String c2=c.getColorTag();
		for(String a:cnf.getStringList("Msg.Clan_Aliado")){
			msg.add(a.replace("@clan", c1));
			msg1.add(a.replace("@clan", c2));
		}
		c.sendMsgLideres(msg,c1);
		cp.getClan().sendMsgLideres(msg1,c2);
		aliado.ally.remove(clan);
		//utils.avisarLideres(c.getID(), msg1);
		//utils.avisarLideres(cp.getClan().getID(), msg);
	}
}
