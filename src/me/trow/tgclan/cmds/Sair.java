package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ChatAPI;
import me.trow.tgclan.utils.ClanPlayer;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Sair {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	private static List<String> confirmar = new ArrayList<String>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		boolean b = cp.getClan().getTotalMembros()==1;
		if(b){
			if(!confirmar.contains(p.getName())){
				for(String v:pl.getConfig().getStringList("Msg.Confirmar_Sair")){
					v=v.replace("&", "§").replace("@player", p.getName());
					new ChatAPI(v).Action(Action.RUN_COMMAND, "/clan sair").sendPlayers(p);
				}
				confirmar.add(p.getName());
				removeConfirmar(p.getName());
				return;
			}
		}
		p.sendMessage(getMsg("Msg.Saiu").replace("@clan", cp.getColorTag()));
		utils.removePlayerClan(cp);
		if(b){
			p.sendMessage(getMsg("Msg.Deletado_Com_Sucesso"));
		}
	}
	
	public void removeConfirmar(final String tagClanSemCor){
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000*60);
					if(confirmar.contains(tagClanSemCor)){
						confirmar.remove(tagClanSemCor);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
