package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Remover {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(CommandSender p,String[] args){
		if(args.length<3){
			p.sendMessage(getMsg("Msg.Use_Remover"));
			return;
		}
		String clan=args[1];
		String mtv="";
		for (int i=2;i<args.length;i++){
			mtv=mtv+args[i]+" ";
		}
		if(!utils.clanExist(clan)){
			p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
			return;
		}
		Clan c = pl.getClan(clan);
		String colorTag=c.getColorTag();
		utils.removeClan(c.getID());
		Bukkit.broadcastMessage(getMsg("Msg.Clan_Deletado").replace("@player", p.getName()).replace("@clan", colorTag).replace("@motivo", mtv));
	}
	
}
