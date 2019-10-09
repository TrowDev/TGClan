package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Motim {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public void execute(Player p,String[] args){
		// /clan motim <novo lider>
		if(args.length <2){
			p.sendMessage(getMsg("Msg.Use_Motim"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(args[1].equalsIgnoreCase("Votar")){
			if(args.length<3){
				p.sendMessage(getMsg("Msg.Use_Votar_Motim"));
				return;
			}
			if(!args[2].equalsIgnoreCase("SIM")&&!args[2].equalsIgnoreCase("NAO")){
				p.sendMessage(getMsg("Msg.Use_Votar_Motim"));
				return;
			}
			if(!utils.inMotim(cp.getClan())){
				p.sendMessage(getMsg("Msg.Sem_Motim"));
				return;
			}
			// clan motim votar <Opcao>
			me.trow.tgclan.utils.Motim m = utils.getMotim(cp.getClan());
			if(m.jaVotou(cp)){
				p.sendMessage(getMsg("Msg.Voce_Ja_Votou").replace("@voto", (m.getVoto(cp)==true?"Sim":"Nao")));
				return;
			}
			if(args[2].equalsIgnoreCase("Sim")){
				m.addVoto(true);
				m.addEleitor(cp, true);
			}else if(args[2].equalsIgnoreCase("Nao")){
				m.addVoto(false);
				m.addEleitor(cp, false);
			}
			if(m.acabou()){
				if(m.ganhandoVotacao()){
					utils.mudarLider(m.getCandidato().getPlayer(), true);
					utils.motim.remove(utils.getMotim(cp.getClan()));
					for(ClanPlayer cp2:cp.getClan().getMembros()){
						Player p1 =Bukkit.getPlayer(cp2.getPlayer());
						if(p1==null)continue;
						p1.sendMessage(getMsg("Msg.Virou_Lider").replace("@player", m.getCandidato().getPlayer()));
					}
				}else if(m.empate()){
					for(ClanPlayer cp2:cp.getClan().getMembros()){
						Player p1 =Bukkit.getPlayer(cp2.getPlayer());
						if(p1==null)continue;
						p1.sendMessage(getMsg("Msg.Empate").replace("@player", m.getCandidato().getPlayer()));
					}
					utils.motim.remove(utils.getMotim(cp.getClan()));
					return;
				}else{
					utils.motim.remove(utils.getMotim(cp.getClan()));
					for(ClanPlayer cp2:cp.getClan().getMembros()){
						Player p1 =Bukkit.getPlayer(cp2.getPlayer());
						if(p1==null)continue;
						p1.sendMessage(getMsg("Msg.Perdeu_Votacao").replace("@player", m.getCandidato().getPlayer()));
					}
				}
			}else{
				for(ClanPlayer cp2:cp.getClan().getMembros()){
					Player p1 =Bukkit.getPlayer(cp2.getPlayer());
					if(p1==null)continue;
					p1.sendMessage(getMsg("Msg.Votem").replace("@player", m.getCandidato().getPlayer()));
				}
			}
			return;
		}
		ClanPlayer cp2 = pl.getMembro(args[1]);
		if(cp2.temClan()&&cp2.isLider()){
			p.sendMessage(getMsg("Msg.E_Lider"));
			return;
		}
		if(!cp.mesmoClan(cp2)){
			p.sendMessage(getMsg("Msg.Nao_Sao_Do_Mesmo_Clan"));
			return;
		}
		me.trow.tgclan.utils.Motim m = new me.trow.tgclan.utils.Motim(cp2);
		if(utils.inMotim(cp.getClan())){
			p.sendMessage(getMsg("Msg.Em_Motim"));
			return;
		}
		utils.motim.add(m);
		for(ClanPlayer mem:cp.getClan().getMembros()){
			Player me = Bukkit.getPlayer(mem.getPlayer());
			if(me==null) continue;
			sendMsg(me, getMsg("Msg.Motim_Iniciado").replace("@candidato", args[1]));
		}
	}
	
}
