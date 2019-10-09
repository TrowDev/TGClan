package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TirarLider {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private static HashMap<String, Clan> votaram = new HashMap<>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public void execute(Player p,String[] args){
		if(args.length <2){
			sendMsg(p, getMsg("Msg.Use_Tirar_Lider"));
			return;
		}
		Player p1 = Bukkit.getPlayer(args[1]);
		if(p1 == null){
			sendMsg(p,getMsg("Msg.Offline"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		ClanPlayer cp2 = pl.getMembro(p1.getName());
		if(p.getName().equalsIgnoreCase(p1.getName())){
			sendMsg(p, getMsg("Msg.Nao_Pode_Tirar_Seu_Lider"));
			return;
		}
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
		if(!cp2.isLider()){
			sendMsg(p, getMsg("Msg.Nao_E_Lider"));
			return;
		}
		if(cp.getClan().getLideres().size()>2){
			if(args.length <3){
				sendMsg(p, getMsg("Msg.Use_Tirar_Lider"));
				return;
			}
			Clan c = cp.getClan();
			if(votaram.containsKey(c.getTag())){
				String voto = args[2];
				if(!voto.equalsIgnoreCase("sim")&&!voto.equalsIgnoreCase("nao")){
					p.sendMessage(getMsg("Msg.Use_Sim_Nao"));
					return;
				}
				c = votaram.get(cp.getTagClan());
				if(!c.getSugestaoTirarLider().equalsIgnoreCase(p1.getName())){
					p.sendMessage(getMsg("Msg.Candidato_Incorreto").replace("@player", c.getSugestaoTirarLider()));
					return;
				}
				if(c.getVotaramProTirarLider().contains(p.getName())){
					p.sendMessage(getMsg("Msg.Voce_Ja_Votou").replace("@voto", "Sim"));
					return ;
				}
				if(c.getSugestaoTirarLider().equalsIgnoreCase(p.getName())){
					p.sendMessage(getMsg("Msg.Voce_Nao_Pode_Votar_Sobre_Si_Mesmo"));
					return;
				}
				if(voto.equalsIgnoreCase("sim")){
					c.getVotaramProTirarLider().add(p.getName());
					votaram.put(c.getTag(), c);
					if((c.getLideres().size()-1)>c.getVotaramProDeletar().size()){
						List<String> msg = new ArrayList<String>();
						String lideresVotaram = getLideresVotaram(c.getVotaramProTirarLider());
						for(String v:pl.getConfig().getStringList("Msg.Falta_Lider_Votar_Tirar_Lider")){
							v=v.replace("&", "§").replace("@jaVotaram", lideresVotaram);
							v=v.replace("@player", p1.getName());
							msg.add(v);
						}
						c.sendMsgLideres(msg, p1.getName());
					}else{
						List<String> msg = new ArrayList<String>();
						String lideresVotaram = getLideresVotaram(c.getVotaramProTirarLider());
						for(String v:pl.getConfig().getStringList("Msg.Tirou_Lider_Democraticamente")){
							v=v.replace("&", "§").replace("@votaram", lideresVotaram);
							v=v.replace("@player", p1.getName());
							msg.add(v);
						}
						c.sendMsgLideres(msg, c.getTag());
						votaram.remove(c.getTag());
						utils.mudarLider(p1.getName(), false);
					}
				}else{
					List<String> msg = new ArrayList<String>();
					for(String v:pl.getConfig().getStringList("Msg.Lider_Perdeu_Na_Votacao")){
						v=v.replace("&", "§").replace("@player", p1.getName());
						msg.add(v);
					}
					c.sendMsgLideres(msg, c.getTag());
					votaram.remove(c.getTag());
					return;
				}
			}else{
				c.setSugestaoTirarLider(p1.getName());
				c.getVotaramProTirarLider().add(p.getName());
				votaram.put(c.getTag(), c);
				List<String> msg = new ArrayList<String>();
				String lideresVotaram = getLideresVotaram(c.getVotaramProTirarLider());
				for(String v:pl.getConfig().getStringList("Msg.Falta_Lider_Votar_Tirar_Lider")){
					v=v.replace("&", "§").replace("@jaVotaram", lideresVotaram);
					v=v.replace("@player", p1.getName());
					msg.add(v);
				}
				c.sendMsgLideres(msg, p1.getName());
				return;
			}
		}
		//utils.addPlayerClan(p1.getName(), utils.getTagForPlayer(p.getName()), false);
		//utils.mudarLeder(p1.getName(), false);
		cp2.setLider(false);
		sendMsg(p, getMsg("Msg.Removeu_Lider").replace("@player", p1.getName()));
	}
	
	public String getLideresVotaram(List<String> s){
		String x = "";
		for(String v:s){
			if(x.equals("")){
				x=v;
			}else{
				x=x+", "+v;
			}
		}
		return x;
	}
	
}
