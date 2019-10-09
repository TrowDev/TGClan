package me.trow.tgclan.cmds.slots;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Slots {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(CommandSender p,String[] args){
		if(args.length<3){
			// /clan slots <set/add/ver> <clan> <#>
			p.sendMessage(getMsg("Msg.Use_Slots"));
			return;
		}
		if(args.length==1){
			p.sendMessage("§cUse: §a/clan slots <set/add/ver> <Clan> <#slots>§c.");
			return;
		}
		if(args[1].equalsIgnoreCase("ver")){
			if(!p.hasPermission("tgclan.staff")&&!p.hasPermission("tgclan.usar.*")){
				p.sendMessage(getMsg("Msg.Sem_Permissao"));
				return;
			}
			String clan = args[2];
			if(!utils.clanExist(clan)){
				p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
				return;
			}
			Clan c = utils.getClan(clan);
			p.sendMessage("§b[Clan] §bEste clan tem §a"+c.getSlots()+"§b slots liberados e esta usando: §a"+(c.getTotalMembros()));
		}
		if(args[1].equalsIgnoreCase("add")){
			if(!p.hasPermission("tgclan.staff")&&!p.hasPermission("tgclan.usar.*")){
				p.sendMessage(getMsg("Msg.Sem_Permissao"));
				return;
			}
			String clan = args[2];
			if(!utils.clanExist(clan)){
				p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
				return;
			}
			Clan c = utils.getClan(clan);
			int slots=c.getSlots();
			slots+=1;
			c.setSlots(slots);
			p.sendMessage(getMsg("Msg.Clan_Slots_Modificado").replace("@clan", clan).replace("@sn", (slots)+""));
		}
		if(args[1].equalsIgnoreCase("set")){
			if(!p.hasPermission("tgclan.staff")&&!p.hasPermission("tgclan.usar.*")){
				p.sendMessage(getMsg("Msg.Sem_Permissao"));
				return;
			}
			String clan = args[2];
			if(!utils.clanExist(clan)){
				p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
				return;
			}
			Clan c = utils.getClan(clan);
			String sn = args[3];
			boolean b=false;
			try{
				Integer.parseInt(sn);
				b=true;
			}catch (Exception e){}
			if(!b){
				p.sendMessage(getMsg("Msg.Nao_E_Numero"));
				return;
			}
			c.setSlots(Integer.parseInt(sn));
			p.sendMessage(getMsg("Msg.Clan_Slots_Modificado").replace("@clan", clan).replace("@sn", sn));
		}
	}
}
