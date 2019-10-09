package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Info {
	
	private static Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	public static List<String> searchingTag = new ArrayList<>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public void execute(Player p,String[] args){
		if(args.length == 1){
			ClanPlayer cp = pl.getMembro(p.getName());
			boolean pTemClan=cp.temClan();
			//String clanTag=pTemClan ? cp.getTagClan():getMsg("Config.Stats.Sem_Clan");
			String clanColorTag=pTemClan?cp.getColorTag():getMsg("Config.Stats.Sem_Clan");
			double kdr=cp.temClan()?cp.getClan().getKDR():0;
			int totalMembros=cp.temClan()?cp.getClan().getTotalMembros():0;
			String aliados=pTemClan? cp.getClan().getTagAliados():getMsg("Config.Stats.Sem_Clan");
			String rivais=pTemClan?cp.getClan().getTagRivais():getMsg("Config.Stats.Sem_Clan");
			String liders=pTemClan?cp.getClan().getNomeLideres():getMsg("Config.Stats.Sem_Clan");
			String money = pl.format(pTemClan?cp.getClan().getMoney():0, true);
			for(String msg:cnf.getStringList("Msg.Clan_Stats")){
				msg=msg.replace("&", "§").replace("@money", money).replace("@clan", clanColorTag).replace("@rivais", rivais).replace("@aliados", aliados).replace("@totalmembros", totalMembros+"").replace("@lideres", liders).replace("@kdr", kdr+"");
				p.sendMessage(msg);
			}
		}
		if(args.length == 2){
			if(!pl.hasPermissionUser(p, "info.outros")){
				p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", "info.outros"));
				return;
			}
			String clan = args[1];
			if(!utils.clanExist(clan)){
				sendMsg(p, getMsg("Msg.Clan_Nao_Existe"));
				return;
			}
			Clan c = pl.getClan(clan);
			String clanTag=clan;
			String clanColorTag=c.getColorTag();
			String money = pl.format(c.getMoney(), true);
			double kdr=c.getKDR();
			int totalMembros=c.getTotalMembros();
			String aliados=c.getTagAliados();
			String rivais=c.getTagRivais();
			String liders=c.getNomeLideres();
			for(String msg:cnf.getStringList("Msg.Clan_Stats_Outro")){
				msg=msg.replace("&", "§").replace("@money", money).replace("@clan", clanColorTag).replace("@rivais", rivais).replace("@aliados", aliados).replace("@totalmembros", totalMembros+"").replace("@lideres", liders).replace("@kdr", kdr+"");
				p.sendMessage(msg);
			}
		}
	}
	
	public static void removeConvite(final String s){
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000*pl.getConfig().getInt("Config.Tempo_Expirar.Pesquisa_Clan"));
					if(searchingTag.contains(s)){
						searchingTag.remove(s);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}
