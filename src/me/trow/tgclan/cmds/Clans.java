package me.trow.tgclan.cmds;

import java.util.ArrayList;
import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.GUIAPI;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.itemnbtapi.NBTItem;

public class Clans {
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	private Main utils = pl;
	
	public String getMsg(String msg){
		if(cnf.getString(msg)==null) return "§cLinha nao encontrada na config: §a"+msg;
		return cnf.getString(msg).replace("&","§");
	}
	
	public void execute(Player p,String[] args){
		boolean b = pl.getConfig().getBoolean("GUI.Clans.Ativar");
		if(args.length==1){
			int pag = 1;
			if(b){
				showGUI(p, pag);
			}else{
				utils.showClans(p, pag);
			}
		}else{
			try{
				Integer.parseInt(args[1]);
			}catch (Exception e){
				p.sendMessage(getMsg("Msg.Nao_E_Numero"));
				return;
			}
			int pag = Integer.parseInt(args[1]);
			if(b){
				showGUI(p, pag);
			}else{
				utils.showClans(p, pag);
			}
		}
	}
	
	public void showGUI(Player p,int pag){
		int porPag = pl.getConfig().getInt("Config.Por_Pag.Clans");
		int start = (pag*porPag)-porPag;
		GUIAPI g = new GUIAPI(pl.getConfig().getInt("GUI.Clans.Tamanho"), getMsg("GUI.Clans.Nome"));
		List<Clan> clans = pl.getTopKDR(pl.getClans("SELECT * FROM `tg_clans` LIMIT "+start+", "+porPag));
		List<String> lore = new ArrayList<>();
		if(clans==null||clans.size()==0){
			ItemStack is = pl.getItem(getMsg("GUI.Clans.Item_Sem_Clan.Item_ID"), getMsg("GUI.Clans.Item_Sem_Clan.Nome"),
					pl.getConfig().getStringList("GUI.Clans.Item_Sem_Clan.Lore"), null);
			int slot = pl.getConfig().getInt("GUI.Clans.Item_Sem_Clan.Slot");
			int linha = pl.getConfig().getInt("GUI.Clans.Item_Sem_Clan.Linha");
			g.setItem(slot, linha, is);
			p.openInventory(g.getInventory());
			return;
		}
		int slot = pl.getConfig().getInt("GUI.Clans.Show.Start");
		int end = pl.getConfig().getInt("GUI.Clans.Show.End");
		for(String v:pl.getConfig().getStringList("GUI.Clans.Lore")){
			v=v.replace("&", "§");
			lore.add(v);
		}
		for(Clan c:clans){
			if(slot>end) break;
			String x=pl.getSkullLink(c.getFirstCharTag());
			ItemStack is = null;
			List<String> loreReplaced = new ArrayList<>();
			for(String v:lore){
				v=v.replace("@nome", c.getNomeClan()).replace("@tag", c.getColorTag()).replace("@membros", ""+c.getMembros().size());
				v=v.replace("@kdr", c.getKDR()+"").replace("@lideres", c.getNomeLideres()).replace("@rivais", c.getTagRivais());
				v=v.replace("@aliados", c.getTagAliados()).replace("@money", pl.format(c.getMoney(), true));
				loreReplaced.add(v);
			}
			if(x==null){
				is=c.getIcone();
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(c.getColorTag());
				im.setLore(loreReplaced);
				is.setItemMeta(im);
			}else{
				is=pl.getItem("397:0", c.getColorTag(), loreReplaced, x);//pl.getSkull(x);
			}
			NBTItem nb = new NBTItem(is);
			nb.setString("tag", c.getTag());
			nb.setInteger("id",c.getID());
			nb.setString("nomeClan", c.getNomeClan());
			is=nb.getItem();
			g.setItem(slot, is);
			slot++;
		}
		ItemStack isSearch = pl.getItem(getMsg("GUI.Clans.Item_Search.Item_ID"), getMsg("GUI.Clans.Item_Search.Nome"),
				pl.getConfig().getStringList("GUI.Clans.Item_Search.Lore"), getMsg("GUI.Clans.Item_Search.URL"));
		//
		NBTItem nbSearch = new NBTItem(isSearch);
		nbSearch.setBoolean("searchClan", true);
		isSearch=nbSearch.getItem();
		//
		if(pag>1){
			ItemStack isBackPag = pl.getItem(getMsg("GUI.Clans.Item_Back_Pag.Item_ID"), getMsg("GUI.Clans.Item_Back_Pag.Nome"),
					pl.getConfig().getStringList("GUI.Clans.Item_Back_Pag.Lore"), getMsg("GUI.Clans.Item_Back_Pag.URL"));
			NBTItem nbBack = new NBTItem(isBackPag);
			nbBack.setInteger("paginaAnteriorClan", (pag-1));
			isBackPag=nbBack.getItem();
			g.setItem(getInt("GUI.Clans.Item_Back_Pag.Slot"), getInt("GUI.Clans.Item_Back_Pag.Linha"), isBackPag);
		}
		int novoStart = ((pag+1)*porPag)-porPag;
		if(pl.getClans("SELECT * FROM `tg_clans` LIMIT "+novoStart+", "+porPag).size()>0){
			ItemStack isNextPag = pl.getItem(getMsg("GUI.Clans.Item_Next_Pag.Item_ID"), getMsg("GUI.Clans.Item_Next_Pag.Nome"),
					pl.getConfig().getStringList("GUI.Clans.Item_Next_Pag.Lore"), getMsg("GUI.Clans.Item_Next_Pag.URL"));
			NBTItem nbNext = new NBTItem(isNextPag);
			nbNext.setInteger("paginaSeguinteClan", (pag+1));
			isNextPag=nbNext.getItem();
			g.setItem(getInt("GUI.Clans.Item_Next_Pag.Slot"), getInt("GUI.Clans.Item_Next_Pag.Linha"), isNextPag);
		}
		g.setItem(getInt("GUI.Clans.Item_Search.Slot"), getInt("GUI.Clans.Item_Search.Linha"), isSearch);
		//
		if(pl.getConfig().getBoolean("GUI.Clans.Completar_GUI.Ativar")){
			ItemStack is = pl.getItem(getMsg("GUI.Clans.Completar_GUI.Item_ID"), getMsg("GUI.Clans.Completar_GUI.Nome"),
					pl.getConfig().getStringList("GUI.Clans.Completar_GUI.Lore"), null);
			g.completSlotEmpty(is);
		}
		//
		p.openInventory(g.getInventory());
	}
	
	public int getInt(String s){
		return pl.getConfig().getInt(s);
	}
	
}
