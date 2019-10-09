package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Rival {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils =pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 3){
			p.sendMessage(getMsg("Msg.Use_Rival"));
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
		String tipo=args[1];
		String clan=args[2];
		if(!utils.clanExist(clan)){
			p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
			return;
		}
		Clan c = pl.getClan(clan);
		if(cp.isRival(c)){
			p.sendMessage(getMsg("Msg.Clan_Ja_E_Rival"));
			return;
		}
		if(tipo.equalsIgnoreCase("add")){
			if(cp.isAliado(c)){
				utils.removeAliado(cp.getClan().getID(), clan);
			}
			utils.addRival(cp.getClan().getID(), clan);
			p.sendMessage(getMsg("Msg.Clan_Agora_E_Rival").replace("@clan", c.getColorTag()));
			return;
		}
		if(tipo.equalsIgnoreCase("remove")){
			if(cp.isRival(c)){
				p.sendMessage(getMsg("Msg.Clan_Ja_E_Rival"));
				return;
			}
			utils.removeRival(cp.getClan().getID(), clan);
			p.sendMessage(getMsg("Msg.Clan_Deixou_De_Ser_Rival"));
			return;
		}
	}
}
