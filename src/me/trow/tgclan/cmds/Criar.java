package me.trow.tgclan.cmds;



import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Criar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		if(args.length < 3){
			p.sendMessage(getMsg("Msg.Use_Criar"));
			return;
		}
		if(args.length >= 3){
			String tag = args[1];
			String tagReplaced=tag.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", "");
			String nome = "";
			for(int i = 2; i < args.length; i++){
				nome=nome+args[i]+" ";
			}
			nome=nome.replaceAll("&([0-9|a-f|r])", "");
			ClanPlayer cp = pl.getMembro(p.getName());
			if(cp.temClan()){
				p.sendMessage(getMsg("Msg.Ja_Tem_Clan"));
				return;
			}
			if(pl.haveSpecialCharacter(nome)){
				p.sendMessage(getMsg("Msg.Caractere_Especial.Nome"));
				return;
			}
			if(pl.haveSpecialCharacter(tagReplaced)){
				p.sendMessage(getMsg("Msg.Caractere_Especial.Tag"));
				return;
			}
			if(utils.clanExist(tagReplaced)||utils.clanExistName(nome)){
				p.sendMessage(getMsg("Msg.Clan_Ja_Existe"));
				return;
			}
			int maxtag = cnf.getInt("Config.Tamanho_Max_Tag");
			int mintag = cnf.getInt("Config.Tamanho_Min_Tag");
			if(tagReplaced.length() < mintag || tagReplaced.length() > maxtag){
				p.sendMessage(getMsg("Msg.Erro_Tag"));
				return;
			}
			double money = cnf.getDouble("Config.Preco_Criar_Clan");
			if(!utils.moneySuficiente(p.getName(), money)){
				p.sendMessage(getMsg("Msg.Sem_Money"));
				return;
			}
			utils.createClan(tag, nome);
			Clan c = pl.getClan(tagReplaced);
			c.setCreating(true);
			cp.setClan(c);
			utils.addPlayerClan(cp, c, true);
			p.sendMessage(getMsg("Msg.Clan_Criado").replace("@clan", tag.replace("&", "§")));
		}
	}
	
}
