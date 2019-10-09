package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PvP {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length <2){
			for(String msg:cnf.getStringList("Msg.Use_PvP")){
				msg=msg.replace("&", "§");
				p.sendMessage(msg);
			}
			return;
		}
		String opcao=args[1];
		double moneyDesativar=cnf.getDouble("Config.Money_Desativar_PvP");
		double moneyAtivar=cnf.getDouble("Config.Money_Ativar_PvP");
		if(!opcao.equalsIgnoreCase("1")&&!opcao.equalsIgnoreCase("2")&&!opcao.equalsIgnoreCase("on")&&!opcao.equalsIgnoreCase("off")){
			for(String msg:cnf.getStringList("Msg.Use_PvP")){
				msg=msg.replace("&", "§");
				p.sendMessage(msg);
			}
			return;
		}
		String resp="";
		if(opcao.equalsIgnoreCase("1")||opcao.equalsIgnoreCase("on")){
			resp="Ativou";
		}else{
			resp="Desativou";
		}
		if(!utils.moneySuficiente(p.getName(), moneyAtivar)&&resp.equalsIgnoreCase("Ativou")){
			p.sendMessage(getMsg("Msg.Sem_Money"));
			return;
		}
		if(!utils.moneySuficiente(p.getName(), moneyDesativar)&&resp.equalsIgnoreCase("Desativou")){
			p.sendMessage(getMsg("Msg.Sem_Money"));
			return;
		}
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		cp.getClan().setFogoAmigo(resp.equalsIgnoreCase("Ativou"),true);
		//pl.cpRecentes.put(cp, System.currentTimeMillis()/1000);
		cp.setCPRecente(System.currentTimeMillis()/1000);
		pl.cpRecentes.put(p.getName(), cp);
		//utils.setFogoAmigoClan(pl.getMembro(p.getName()).getClan().getID(), Integer.parseInt(opcao));
		p.sendMessage(getMsg("Msg.PvP_Mudado").replace("@pvp", resp));
	}
	
}
