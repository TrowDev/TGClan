package me.trow.tgclan.cmds;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.ClanPlayer;
import me.trow.tgclan.utils.Item;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ModItem {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMsg(Player p,String msg){
		p.sendMessage(msg);
	}
	
	public boolean idExist(int id){
		Material m = Item.getItemStack(id+":0").getType();//Material.getMaterial(id);
		return m!=null;
	}
	
	public void execute(Player p,String[] args){
		if(args.length<2){
			p.sendMessage(getMsg("Msg.Use_ModItem")); // clan moditem <item>
			return;
		}
		ClanPlayer cp=utils.getMembro(p.getName());
		if(!cp.temClan()){
			p.sendMessage(getMsg("Msg.Sem_Clan"));
			return;
		}
		if(!cp.isLider()){
			p.sendMessage(getMsg("Msg.Voce_Nao_E_Lider"));
			return;
		}
		String it = args[1];
		if(!it.contains(":")){
			p.sendMessage(getMsg("Msg.Use_ModItem")); // clan moditem <item>
			return;
		}
		String[] d= it.split(":");
		if(d.length==1){
			it=it+"0";
		}
		d= it.split(":");
		boolean ok=false;
		try{
			ok=true;
			Integer.parseInt(d[0]);
			Integer.parseInt(d[1]);
		}catch (Exception e){}
		if(!ok){
			p.sendMessage(getMsg("Msg.Nao_E_Numero"));
			return;
		}
		int id = Integer.parseInt(d[0]);
		int dt = Integer.parseInt(d[1]);
		if(!idExist(id)){
			p.sendMessage(getMsg("Msg.Id_Nao_Existe"));
			return;
		}
		ItemStack is = Item.getItemStack(id+":"+dt);//new ItemStack(id,1,(byte)dt);
		cp.getClan().setIcone(is);
		//utils.editClanItem(it, cp.getClan().getID());
		p.sendMessage(getMsg("Msg.Item_Mudado").replace("@item", it));
	}
}
