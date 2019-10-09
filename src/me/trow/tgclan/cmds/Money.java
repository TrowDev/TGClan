package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Money {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public boolean ehDouble(String s){
		try{
			Double.parseDouble(s);
			return true;
		}catch (Exception e){}
		return false;
	}
	
	public void execute(Player p,String[] args){
		ClanPlayer cp = utils.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(args.length==1){
			p.sendMessage(getMsg("Msg.Money").replace("@money", utils.formatar(cp.getClan().getMoney())));
			p.sendMessage(getMsg("Msg.Use_Money"));
			return;
		}
		if(args[1].equalsIgnoreCase("give")){
			if(args.length<3){
				p.sendMessage(getMsg("Msg.Use_Money"));
				return;
			}
			if(!ehDouble(args[2])){
				p.sendMessage(getMsg("Msg.Valor_Invalido"));
				return;
			}
			double d = Double.parseDouble(args[2]);
			if(d<0){
				p.sendMessage(getMsg("Msg.Valor_Invalido"));
				return;
			}
			if(!utils.moneySuficiente(p.getName(), d)){
				p.sendMessage(getMsg("Msg.Sem_Money"));
				return;
			}
			utils.econ.withdrawPlayer(p.getName(), d);
			cp.getClan().addMoney(d);
			p.sendMessage(getMsg("Msg.Money_Add").replace("@money", d+""));
		}
		if(args[1].equalsIgnoreCase("take")){
			if(!cp.isLider()){
				p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
				return;
			}
			if(args.length<3){
				p.sendMessage(getMsg("Msg.Use_Money"));
				return;
			}
			if(!ehDouble(args[2])){
				p.sendMessage(getMsg("Msg.Valor_Invalido"));
				return;
			}
			double d = Double.parseDouble(args[2]);
			if(d<0){
				p.sendMessage(getMsg("Msg.Valor_Invalido"));
				return;
			}
			if(cp.getClan().getMoney()<d){
				p.sendMessage(getMsg("Msg.Clan_Sem_Money"));
				return;
			}
			utils.econ.depositPlayer(p.getName(), d);
			cp.getClan().removeMoney(d);
			p.sendMessage(getMsg("Msg.Money_Removido").replace("@money", d+""));
		}
	}
	
}
