package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Kick {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length<2){
			p.sendMessage(getMsg("Msg.Use_Kick"));
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
		Player p1=Bukkit.getPlayer(args[1]);
		if(p1==null){
			OfflinePlayer op=Bukkit.getOfflinePlayer(args[1]);
			ClanPlayer cp2 = pl.getMembro(op.getName());
			if(!cp2.temClan()){
				p.sendMessage(getMsg("Msg.Player_Sem_Clan"));
				return;
			}
			if(!cp.mesmoClan(cp2)){
				p.sendMessage(getMsg("Msg.Nao_Sao_Do_Mesmo_Clan"));
				return;
			}
			if(utils.inMotim(cp2.getClan())){
				p.sendMessage(getMsg("Msg.Aguarde_O_Motim_Acabar"));
				return;
			}
			utils.removePlayerClan(cp2);
			p.sendMessage(getMsg("Msg.Player_Removido").replace("@player", op.getName()));
			return;
		}
		ClanPlayer cp2 = pl.getMembro(p1.getName());
		if(!cp2.temClan()){
			p.sendMessage(getMsg("Msg.Player_Sem_Clan"));
			return;
		}
		if(!cp.mesmoClan(cp2)){
			p.sendMessage(getMsg("Msg.Nao_Sao_Do_Mesmo_Clan"));
			return;
		}
		if(utils.inMotim(cp2.getClan())){
			p.sendMessage(getMsg("Msg.Aguarde_O_Motim_Acabar"));
			return;
		}
		utils.removePlayerClan(cp2);
		p.sendMessage(getMsg("Msg.Player_Removido").replace("@player", p1.getName()));
	}
	
}
