package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ChatAPI;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Deletar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private static List<String> confirmar = new ArrayList<>();
	private static HashMap<String, Clan> votos = new HashMap<>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		if(!confirmar.contains(p.getName())){
			new ChatAPI(getMsg("Msg.Confirmar_Deletar_Clan")).Action(Action.RUN_COMMAND, "/clan deletar").sendPlayers(p);
			confirmar.add(p.getName());
			return;
		}
		int b = cp.getClan().getLideres().size();
		if(b>1){
			if(votos.containsKey(cp.getTagClan())){
				if(args.length<2){
					p.sendMessage(getMsg("Msg.Use_Deletar"));
					return;
				}
				String voto = args[1];
				if(!voto.equalsIgnoreCase("sim")&&!voto.equalsIgnoreCase("nao")){
					p.sendMessage(getMsg("Msg.Use_Sim_Nao"));
					return;
				}
				Clan c = votos.get(cp.getTagClan());
				if(c.getVotaramProDeletar().contains(p.getName())){
					p.sendMessage(getMsg("Msg.Voce_Ja_Votou").replace("@voto", "Sim"));
					return;
				}
				if(voto.equalsIgnoreCase("sim")){
					c.getVotaramProDeletar().add(p.getName());
					votos.put(c.getTag(), c);
					if(c.getLideres().size()>c.getVotaramProDeletar().size()){
						List<String> msg = new ArrayList<String>();
						String lideresVotaram = getLideresVotaram(c.getVotaramProDeletar());
						for(String v:pl.getConfig().getStringList("Msg.Falta_Lider_Votar_Deletar")){
							v=v.replace("&", "§").replace("@jaVotaram", lideresVotaram);
							msg.add(v);
						}
						c.sendMsgLideres(msg, c.getTag());
					}else{
						List<String> msg = new ArrayList<String>();
						String lideresVotaram = getLideresVotaram(c.getVotaramProDeletar());
						for(String v:pl.getConfig().getStringList("Msg.Clan_Removido_Democraticamente")){
							v=v.replace("&", "§").replace("@votaram", lideresVotaram);
							msg.add(v);
						}
						c.sendMsgLideres(msg, c.getTag());
						votos.remove(c.getTag());
						utils.removeClan(cp.getClan().getID());
					}
				}else{
					List<String> msg = new ArrayList<String>();
					for(String v:pl.getConfig().getStringList("Msg.Clan_Salvo_Por_Voto_Nao")){
						v=v.replace("&", "§").replace("@player", p.getName());
						msg.add(v);
					}
					c.sendMsgLideres(msg, c.getTag());
					votos.remove(c.getTag());
					return;
				}
			}else{
				Clan c = cp.getClan();
				c.getVotaramProDeletar().add(p.getName());
				votos.put(c.getTag(), c);
				List<String> msg = new ArrayList<String>();
				String lideresVotaram = getLideresVotaram(c.getVotaramProDeletar());
				for(String v:pl.getConfig().getStringList("Msg.Falta_Lider_Votar_Deletar")){
					v=v.replace("&", "§").replace("@jaVotaram", lideresVotaram);
					msg.add(v);
				}
				c.sendMsgLideres(msg, c.getTag());
				return;
			}
			return;
		}
		utils.removeClan(cp.getClan().getID());
		p.sendMessage(getMsg("Msg.Deletado_Com_Sucesso"));
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
