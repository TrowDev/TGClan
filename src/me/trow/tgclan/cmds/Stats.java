package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Stats {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils =pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public void execute(Player p,String[] args){
		if(args.length==1){
			// variaveis do player.
			ClanPlayer cp = pl.getMembro(p.getName());
			double kdr=cp.getKDR();
			int neutras=cp.getKillsNeutras();
			int civis=cp.getKillsCivis();
			int mortes=cp.getMorreu();
			int totalNeutrasCivis=neutras+civis;
			String clanTag=cp.getColorTag(); // http://prntscr.com/9ki3un
			String clan = cp.temClan() ? getMsg("Config.Stats.Seu_Clan").replace("@clan", clanTag):getMsg("Config.Stats.Sem_Clan");
			String status="";
			if(cp.temClan()){
				if(cp.isLider()){
					status=getMsg("Config.Stats.Stats_Lider");
				}else{
					status=getMsg("Config.Stats.Stats_Membro");
				}
			}else{
				status=getMsg("Config.Stats.Sem_Stats");
			}
			// display variaveis of player.
			for(String stats:cnf.getStringList("Msg.Stats")){
				stats=stats.replace("&", "§").replace("@clan", clan).replace("@kdr", kdr+"").replace("@mortes", mortes+"").replace("@totalkill", totalNeutrasCivis+"").replace("@killsC", civis+"").replace("@killsN", neutras+"").replace("@status", status);
				p.sendMessage(stats);
			}
			return;
		}
		if(args.length == 2){
			Player p1 = Bukkit.getPlayer(args[1]);
			if(p1 == null){
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
				ClanPlayer cp = pl.getMembro(op.getName());
				if(!cp.temInfo()){
					p.sendMessage(getMsg("Msg.Sem_Informacao"));
					return;
				}
				double kdr=cp.getKDR();
				int neutras=cp.getKillsNeutras();
				int civis=cp.getKillsCivis();
				int mortes=cp.getMorreu();
				int totalNeutrasCivis=neutras+civis;
				String clanTag=cp.getColorTag(); // http://prntscr.com/9ki3un
				String clan = cp.temClan() ? getMsg("Config.Stats.Seu_Clan").replace("@clan", clanTag):getMsg("Config.Stats.Sem_Clan");
				String status="";
				if(cp.temClan()){
					if(cp.isLider()){
						status=getMsg("Config.Stats.Stats_Lider");
					}else{
						status=getMsg("Config.Stats.Stats_Membro");
					}
				}else{
					status=getMsg("Config.Stats.Sem_Stats");
				}
				// display variaveis of player.
				for(String stats:cnf.getStringList("Msg.Stats_Outros")){
					stats=stats.replace("&", "§").replace("@player", op.getName()).replace("@clan", clan).replace("@kdr", kdr+"").replace("@mortes", mortes+"").replace("@totalkill", totalNeutrasCivis+"").replace("@killsC", civis+"").replace("@killsN", neutras+"").replace("@status", status);
					p.sendMessage(stats);
				}
				return;
			}
			ClanPlayer cp = pl.getMembro(p1.getName());
			if(!cp.temInfo()){
				p.sendMessage(getMsg("Msg.Sem_Informacao"));
				return;
			}
			double kdr=cp.getKDR();
			int neutras=cp.getKillsNeutras();
			int civis=cp.getKillsCivis();
			int mortes=cp.getMorreu();
			int totalNeutrasCivis=neutras+civis;
			String clanTag=cp.getColorTag(); // http://prntscr.com/9ki3un
			String clan = cp.temClan() ? getMsg("Config.Stats.Seu_Clan").replace("@clan", clanTag):getMsg("Config.Stats.Sem_Clan");
			String status="";
			if(cp.temClan()){
				if(cp.isLider()){
					status=getMsg("Config.Stats.Stats_Lider");
				}else{
					status=getMsg("Config.Stats.Stats_Membro");
				}
			}else{
				status=getMsg("Config.Stats.Sem_Stats");
			}
			// display variaveis of player.
			for(String stats:cnf.getStringList("Msg.Stats_Outros")){
				stats=stats.replace("&", "§").replace("@player", p1.getName()).replace("@clan", clan).replace("@kdr", kdr+"").replace("@mortes", mortes+"").replace("@totalkill", totalNeutrasCivis+"").replace("@killsC", civis+"").replace("@killsN", neutras+"").replace("@status", status);
				p.sendMessage(stats);
			}
		}
	}
	
}
