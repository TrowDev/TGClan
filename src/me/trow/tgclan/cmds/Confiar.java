package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Confiar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public void execute(Player p,String[] args){
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			sendMsg(p, getMsg("Msg.Sem_Clan"));
			return;
		}
		if(args.length <3){
			sendMsg(p, getMsg("Msg.Use_Confiar"));
			return;
		}
		Player p1 = Bukkit.getPlayer(args[1]);
		if(p1 == null){
			sendMsg(p, getMsg("Msg.Offline"));
			return;
		}
		ClanPlayer cp2 = pl.getMembro(p1.getName());
		if(!cp2.temClan()){
			sendMsg(p, getMsg("Msg.Player_Sem_Clan"));
			return;
		}
		if(!cp.mesmoClan(cp2)){
			sendMsg(p, getMsg("Msg.Nao_Sao_Do_Mesmo_Clan"));
			return;
		}
		if(!cp.isLider()){
			sendMsg(p, getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		if(cp2.isConfiavel()){
			sendMsg(p, getMsg("Msg.E_Confiavel"));
			return;
		}
		boolean b = args[2].equalsIgnoreCase("sim")?true:false;
		//utils.addPlayerClan(p1.getName(), utils.getTagForPlayer(p.getName()), true);
		//utils.mudarLeder(p1.getName(), true);
		cp2.setConfiavel(b);
		sendMsg(p, getMsg("Msg.Status_Confiavel").replace("@player", p1.getName()).replace("@status", args[2]));
		sendMsg(p1, getMsg("Msg.Atualizou_Confiavel").replace("@player", p.getName()).replace("@status", args[2]));
	}
	
}
