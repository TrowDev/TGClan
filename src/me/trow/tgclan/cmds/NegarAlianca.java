package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class NegarAlianca {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private Aliado aliado = new Aliado();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length <2){
			p.sendMessage(getMsg("Msg.Use_Negar"));
			return;
		}
		String clan = args[1];
		if(!aliado.ally.containsKey(clan)){
			p.sendMessage(getMsg("Msg.Clan_Nao_Lhe_Convidou"));
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
		if(!aliado.ally.get(clan).equalsIgnoreCase(cp.getTagClan())){
			p.sendMessage(getMsg("Msg.Clan_Nao_Lhe_Convidou"));
			return;
		}
		//utils.addPlayerClan(p.getName(), clan, false);
		p.sendMessage(getMsg("Msg.Convite_Negado"));
		List<String> aviso=new ArrayList<>();
		for(String msg:cnf.getStringList("Msg.Negou_Convite_Aliado")){
			msg=msg.replace("&", "§").replace("@clan", cp.getColorTag());
			aviso.add(msg);
		}
		Clan c = pl.getClan(clan);
		c.sendMsgLideres(aviso,cp.getTagClan());
		aliado.ally.remove(clan);
		p.sendMessage(getMsg("Msg.Negou_Aliado"));
		//pl.avisarLideres(c.getID(), aviso);
	}
	
}
