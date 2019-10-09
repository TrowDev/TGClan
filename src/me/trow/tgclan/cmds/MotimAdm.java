package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MotimAdm {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(CommandSender p,String[] args){
		if(args.length<3){
			// /clan motimadm <clan> <Sim/Nao>
			p.sendMessage(getMsg("Msg.Use_MotimAdm"));
			return;
		}
		if(!p.hasPermission("tgclan.staff")&&!p.hasPermission("tgclan.usar.*")){
			p.sendMessage(getMsg("Msg.Sem_Permissao"));
			return;
		}
		String clan = args[1];
		if(!utils.clanExist(clan)){
			p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
			return;
		}
		Clan c = utils.getClan(clan);
		String sn = args[2];
		if(!sn.equalsIgnoreCase("SIM")&&!sn.equalsIgnoreCase("NAO")){
			p.sendMessage(getMsg("Msg.Use_MotimAdm"));
			return;
		}
		c.setCanMotim(sn.equalsIgnoreCase("Sim"));
		p.sendMessage(getMsg("Msg.Clan_Motim_Editado").replace("@clan", args[1]).replace("@sn", sn));
	}
}
