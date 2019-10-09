package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ModTag {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		// clan modtag <newtag>
		if(args.length<2){
			p.sendMessage(getMsg("Msg.Use_ModTag"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		
		String tagColor=args[1];
		String tag = cp.getTagClan();
		
		String tagColorReplaced=tagColor.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", "");
		
		
		if(!tag.equalsIgnoreCase(tagColorReplaced)){
			p.sendMessage(getMsg("Msg.Nao_E_Possivel_Alterar_A_Sigla_Do_Clan").replace("@sigla", tag));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		cp.getClan().setColorTag(tagColor.replace("&", "§"));
		//pl.editClanTag(tagColor, cp.getClan().getID());
		p.sendMessage(getMsg("Msg.Tag_Clan_Modificada").replace("@tag", tagColor.replace("&", "§")));
	}
	
}
