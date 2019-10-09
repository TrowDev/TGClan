package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Prefix {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 3){
			p.sendMessage(getMsg("Msg.Use_Prefix"));
			return;
		}
		Player p1=Bukkit.getPlayer(args[1]);
		if(p1==null){
			p.sendMessage(getMsg("Msg.Offline"));
			return;
		}
		String prefix=args[2].replace("&", "§");
		String prefixReplaced=prefix.replaceAll("§([0-9|a-f|r])", "");;
		int maxPrefix=cnf.getInt("Config.Max_Prefix_Chat_Clan");
		if(prefixReplaced.length()>maxPrefix){
			p.sendMessage(getMsg("Msg.Limite_Prefix_Max").replace("@max", maxPrefix+""));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		ClanPlayer cp2 = pl.getMembro(p1.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(!cp2.temClan()){
			p.sendMessage(getMsg("Msg.Player_Sem_Clan"));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		if(!cp.mesmoClan(cp2)){
			p.sendMessage(getMsg("Msg.Nao_Sao_Do_Mesmo_Clan"));
			return;
		}
		cp2.setTagChat(prefix);
		//utils.setTagChatPlayer(p1.getName(), prefix);
		p.sendMessage(getMsg("Msg.Prefix_Setado").replace("@player", p1.getName()).replace("@prefix", prefix));
	}
	
}
