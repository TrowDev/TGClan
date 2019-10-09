package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Aliado {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	public static HashMap<String, String> ally=new HashMap<>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 3){
			p.sendMessage(getMsg("Msg.Use_Aliado"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		//p.sendMessage("§c> "+cp.temClan());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(ally.containsKey(cp.getTagClan())){
			p.sendMessage(getMsg("Msg.Ja_Convidando").replace("@tipo", "clan"));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		String seuClan=cp.getTagClan();
		String tipo=args[1];
		String clan=args[2];
		String tagC=cp.getColorTag();
		Clan c = pl.getClan(clan);
		if(c==null){
			p.sendMessage(getMsg("Msg.Clan_Nao_Existe"));
			return;
		}
		if(seuClan.equalsIgnoreCase(clan)){
			p.sendMessage(getMsg("Msg.Nao_Pode_Executar_Mesmo_Clan"));
			return;
		}
		if(tipo.equalsIgnoreCase("add")){
			if(cp.isAliado(c)){
				p.sendMessage(getMsg("Msg.Clan_Ja_E_Aliado"));
				return;
			}
			ally.put(seuClan, clan);
			List<String> msg=new ArrayList<>();
			for(String allya:cnf.getStringList("Msg.Add_Aliado_Info")){
				allya=allya.replace("@clan", tagC);
				msg.add(allya);
			}
			c.sendMsgLideres(msg,cp.getTagClan());
			//utils.avisarLideres(c.getID(), msg);
			p.sendMessage(getMsg("Msg.Pedindo_De_Alianca_Enviado"));
			removeConvite(cp.getClan(),c);//seuClan,cp.getColorTag());
			return;
		}
		if(tipo.equalsIgnoreCase("remove")){
			if(!cp.isAliado(c)){
				p.sendMessage(getMsg("Msg.Clan_Nao_E_Aliado"));
				return;
			}
			utils.removeAliado(cp.getClan().getID(), clan);
			utils.removeAliado(c.getID(), seuClan);
			p.sendMessage(getMsg("Msg.Clan_Nao_E_Mais_Aliado").replace("@clan", tagC));
		}
	}
	
	public void removeConvite(final Clan c,final Clan co){
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000*pl.getConfig().getInt("Config.Tempo_Expirar.Convite_Clan"));
					if(ally.containsKey(c.getTag())){
						String tagClanSemCor = c.getTag();
						if(co!=null){
							List<String> msg=new ArrayList<>();
							msg.add(getMsg("Msg.Convite_Expirado").replace("@clan", c.getColorTag()));
							co.sendMsgLideres(msg, c.getTag());
						}
						ally.remove(tagClanSemCor);
						List<String> msg = new ArrayList<String>();
						for(String v:pl.getConfig().getStringList("Msg.Convite_Expirado_Clan.Clan")){
							v=v.replace("@clan", co.getColorTag()).replace("&", "§");
							msg.add(v);
						}
						c.sendMsgLideres(msg, c.getColorTag());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}
