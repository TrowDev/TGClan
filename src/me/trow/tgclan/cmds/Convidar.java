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

public class Convidar {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	public static HashMap<String, String> convite = new HashMap<>();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(final Player convidou,String[] args){
		if(args.length < 2){
			convidou.sendMessage(getMsg("Msg.Use_Convidar"));
			return;
		}
		Player convidado=Bukkit.getPlayer(args[1]);
		if(convidado==null){
			convidou.sendMessage(getMsg("Msg.Offline"));
			return;
		}
		String pConvidou=convidou.getName();
		String pConvidado=convidado.getName();
		ClanPlayer cp = pl.getMembro(pConvidou);
		ClanPlayer cp2 = pl.getMembro(pConvidado);
		if(!cp.temClan()){
			convidou.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(convite.containsKey(cp.getTagClan())){
			convidou.sendMessage(getMsg("Msg.Ja_Convidando").replace("@tipo", "player"));
			return;
		}
		if(cp2.temClan()){
			convidou.sendMessage(getMsg("Msg.Player_Ja_Tem_Clan"));
			return;
		}
		if(!cp.isLider()){
			convidou.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		if(cp.mesmoClan(cp2)){
			convidou.sendMessage(getMsg("Msg.Player_Ja_Esta_No_Clan"));
			return;
		}
		if(!utils.hasPermissionUser(convidou, "convidar",false)){
			convidou.sendMessage(getMsg("Msg.Sem_Permissao"));
			return;
		}
		final String tagClanSemCor=cp.getTagClan();
		int totalMembers=cp.getClan().getTotalMembros();
		totalMembers+=1;
		//int maxPlayers = pl.getConfig().getInt("Config.Max_Players_Por_Clan");
		int maxPlayers=cp.getClan().getSlots();
		if(totalMembers>maxPlayers){
			convidou.sendMessage(getMsg("Msg.Max_Players_Atingido").replace("@maxplayers", maxPlayers+""));
			return;
		}
		String tagClanComCor=cp.getColorTag();
		if(convite.containsKey(tagClanSemCor)){
			convidou.sendMessage(getMsg("Msg.Aguarde_Fim_Convite"));
			return;
		}
		double money = cnf.getDouble("Config.Preco_Convidar");
		if(!utils.moneySuficiente(pConvidou, money)){
			convidou.sendMessage(getMsg("Msg.Sem_Money"));
			return;
		}
		pl.econ.withdrawPlayer(pConvidou, money);
		convite.put(tagClanSemCor, pConvidado);
		for(String msg:cnf.getStringList("Msg.Convite_Clan")){
			msg=msg.replace("&", "§").replace("@clan", tagClanComCor);
			String[] d = msg.split("@");
			ChatAPI c = new ChatAPI(d[0]);
			if(msg.contains("@cmd:")){
				String[] r = msg.split("@cmd:");
				c.Action(Action.RUN_COMMAND, pl.tirarCodigoCor(r[1]));
			}
			c.sendPlayers(convidado);
		}
		convidou.sendMessage(getMsg("Msg.Convite_Enviado").replace("@player", convidado.getName()));
		removeConvite(cp.getClan());//tagClanSemCor,cp.getColorTag());
	}
	
	public void removeConvite(final Clan c){
		new Thread(){
			public void run(){
				try {
					Thread.sleep(1000*pl.getConfig().getInt("Config.Tempo_Expirar.Convite_Player"));
					if(convite.containsKey(c.getTag())){
						String nome=convite.get(c.getTag());
						Player p = Bukkit.getPlayer(nome);
						if(p!=null){
							p.sendMessage(getMsg("Msg.Convite_Expirado").replace("@clan", c.getColorTag()));
						}
						convite.remove(c.getTag());
						List<String> msg = new ArrayList<String>();
						for(String v:pl.getConfig().getStringList("Msg.Convite_Expirado_Clan.Player")){
							v=v.replace("@player", nome).replace("&", "§");
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
