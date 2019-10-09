package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.api.PlayerSetHomeEvent;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Home {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		ClanPlayer cp = utils.getMembro(p.getName());
		String a=args[0];
		if(a.equalsIgnoreCase("home")){
			if(!cp.temClan()){
				p.sendMessage(getMsg("Msg.Sem_Clan"));
				return;
			}
			if(args.length==1){
				Location l = cp.getClan().getHomeLocation();
				if(l==null){
					p.sendMessage(getMsg("Msg.Clan_Sem_Home"));
					return;
				}
				p.teleport(l);
				p.sendMessage(getMsg("Msg.Teleportado"));
			}
			return;
		}
		if(args.length>=0&&a.equalsIgnoreCase("delhome")){
			if(!cp.temClan()){
				p.sendMessage(getMsg("Msg.Sem_Clan"));
				return;
			}
			if(!cp.isLider()){
				p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
				return;
			}
			Location l = cp.getClan().getHomeLocation();
			if(l==null){
				p.sendMessage(getMsg("Msg.Clan_Sem_Home"));
				return;
			}
			if(cp.getClan().removeFlag("home")){
				p.sendMessage(getMsg("Msg.Home_Deletada"));
			}else{
				p.sendMessage("§4[CLAN] §cOuve um erro ao deletar a home!");
			}
		}
		if(args.length>=0&&a.equalsIgnoreCase("sethome")){
			if(!cp.temClan()){
				p.sendMessage(getMsg("Msg.Sem_Clan"));
				return;
			}
			if(!cp.isLider()){
				p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
				return;
			}
			if(cp.getClan().getHomeLocation()!=null){
				p.sendMessage(getMsg("Msg.Home_Ja_Existe"));
				return;
			}
			Location l = p.getLocation();
			String loc = pl.serializeLoc(l);
			PlayerSetHomeEvent pshe = new PlayerSetHomeEvent(p);
			Bukkit.getPluginManager().callEvent(pshe);
			if(pshe.isCancelled()){
				p.sendMessage("§4[Clan] §cOps... Parece que nao foi possivel setar a home do seu clan.");
				return;
			}
			if(cp.getClan().addFlag("home", loc)){
				p.sendMessage(getMsg("Msg.Home_Setada"));
			}else{
				p.sendMessage("§4[CLAN] §cOuve um erro ao setar a home!");
			}
		}
	}
	
}
