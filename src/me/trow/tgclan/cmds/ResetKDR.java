package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ResetKDR {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(CommandSender p,String[] args){
		if(args.length==2){
			if(!utils.hasPermissionUser(p, "resetkdr.outros")){
				p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", "resetkdr.outros"));
				return;
			}
			Player p1=Bukkit.getPlayer(args[1]);
			if(p1==null){
				ClanPlayer cp = pl.getMembro(args[1]);
				if(!cp.temInfo()){
					p.sendMessage(getMsg("Msg.Sem_Informacao"));
					return;
				}
				utils.resetKDR(args[1]);
				return;
			}
			ClanPlayer cp = pl.getMembro(p1.getName());
			if(!cp.temInfo()){
				p.sendMessage(getMsg("Msg.Sem_Informacao"));
				return;
			}
			utils.resetKDR(p1.getName());
			p.sendMessage(getMsg("Msg.KDR_Do_Player_Resetado").replace("@player", p1.getName()));
			return;
		}
		if(!(p instanceof Player)){
			p.sendMessage("§cUse este comando in-game.");
			return;
		}
		utils.resetKDR(p.getName());
		p.sendMessage(getMsg("Msg.Resetou_KDR"));
	}
	
}
