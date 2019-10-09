package me.trow.tgclan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.trow.tgclan.Main;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Clan {
	
	private String clanNome,colorTag,tag,flags,sugestaoNovoLider,sugestaoTirarLider;
	private List<String> allys,rivais,votaramProDeletar,votaramProNovoLider,votaramProTirarLider;
	private long dataFundacao;
	private boolean fogoAmigo,verificada,canHaveMotim,criando;
	private List<ClanPlayer> membros;
	private List<ClanPlayer> lideres;
	private int id,maxPlayers;
	private double money;
	private ItemStack is;
	private HashMap<String, String> flag;
	
	public Clan(String nome,String tag,boolean v,int id,double m,int cHM,int maxP,String it,String flag){
		if(cHM==1){
			canHaveMotim=true;
		}else{
			canHaveMotim=false;
		}
		this.flag=new HashMap<>();
		flags=flag;
		maxPlayers=maxP;
		clanNome=nome;
		money=m;
		colorTag=tag;
		verificada=v;
		this.tag=tag.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", "");
		allys=new ArrayList<>();rivais=new ArrayList<>();
		membros=new ArrayList<>();lideres=new ArrayList<>();
		this.id=id;
		is=Item.getItemStack(it);//new ItemStack(Integer.parseInt(it.split(":")[0]),1,(byte)Integer.parseInt(it.split(":")[1]));
		votaramProDeletar=new ArrayList<>();
		votaramProNovoLider=new ArrayList<>();
		votaramProTirarLider=new ArrayList<>();
	}
	
	public String getFirstCharTag(){
		String c = getTag().substring(0, 1);
		return c;
	}
	
	public String getSugestaoTirarLider(){
		return sugestaoTirarLider;
	}
	
	public void setSugestaoTirarLider(String b){
		sugestaoTirarLider=b;
	}
	
	public String getSugestaoNovoLider(){
		return sugestaoNovoLider;
	}
	
	public void setSugestaoNovoLider(String b){
		sugestaoNovoLider=b;
	}
	
	public List<String> getVotaramProDeletar(){
		return votaramProDeletar;
	}
	
	public List<String> getVotaramProNovoLider(){
		return votaramProNovoLider;
	}
	
	public List<String> getVotaramProTirarLider(){
		return votaramProTirarLider;
	}
	
	public HashMap<String,String> getFlags(){
		if(flags==null) return flag;
		if(flags.length()>0&&!flags.equalsIgnoreCase("")){
			for(String v:flags.split(",")){
				String[] d = v.split("=");
				String key = d[0].replace(" ", "").trim();
				String val = d[1].replace("{", "").replace("}", "");
				flag.put(key, val);
			}
		}
		return flag;
	}
	
	public String getFlagsStr(){
		return flags;
	}
	
	public void setColorTag(String s){
		colorTag=s;
		Main.pl.updateClan(this);
	}
	
	public boolean removeFlag(String key){
		if(getFlags().containsKey(key)){
			getFlags().remove(key);
			flags="";
			for(String x:getFlags().keySet()){
				if(x.equalsIgnoreCase(key)) continue;
				String ob = x+"={"+getFlags().get(x)+"}";
				flags=flags+", "+ob;
			}
		}
		return Main.pl.updateClan(this);
		//return Main.pl.editClanFlag(flags, getID());
	}
	
	public boolean addFlag(String key,String value){
		getFlags().put(key, value);
		flags="";
		for(String x:getFlags().keySet()){
			if(flags.equalsIgnoreCase("")){
				String ob = x+"={"+getFlags().get(x)+"}";
				flags=ob;
			}else{
				String ob = x+"={"+getFlags().get(x)+"}";
				flags=flags+", "+ob;
			}
		}
		return Main.pl.updateClan(this);
		//return Main.pl.editClanFlag(flags, getID());
	}
	
	public String getHomeStr(){
		if(getFlags().containsKey("home")){
			return getFlags().get("home");
		}
		return null;
	}
	
	public Location getHomeLocation(){
		String locStr = getHomeStr();
		if(locStr==null) return null;
		Location l = Main.pl.deserializeLocation(locStr);
		if(l==null) return null;
		return l;
	}
	
	public boolean isCreating(){
		return criando;
	}
	
	public void setCreating(boolean b){
		criando=b;
	}
	
	public ItemStack getIcone(){
		return is;
	}
	
	public void setIcone(ItemStack iss){
		is=iss;
		Main.pl.updateClan(this);
		//Main.pl.editClanItem(is.getTypeId()+":"+is.getData().getData(), id);
	}
	
	public int getSlots(){
		return maxPlayers;
	}
	
	public void setSlots(int i){
		maxPlayers=i;
		Main.pl.updateClan(this);
		//Main.pl.editSlots(i, id);
	}
	
	public boolean canHaveMotim(){
		return canHaveMotim;
	}
	
	public void setCanMotim(boolean b){
		if(canHaveMotim()==b) return;
		canHaveMotim=b;
		//int i = b?1:0;
		//Main.pl.editMotim(i, id);
		Main.pl.updateClan(this);
	}
	
	public double getMoney(){
		return money;
	}
	
	public void setMoney(double m){
		money=m;
		//Main.pl.changeMoney(tag, money);
		Main.pl.updateClan(this);
	}
	
	public boolean addMoney(double d){
		setMoney(getMoney()+d);
		return true;
	}
	
	public boolean hasMoney(double d){
		return getMoney()>=d;
	}
	
	public boolean removeMoney(double d){
		if(!hasMoney(d)){
			return false;
		}
		setMoney(getMoney()-d);
		return true;
	}
	
	public int getTotalKills(){
		int i = 0;
		for(ClanPlayer cp:getMembros()){
			i+=cp.getKillsCivis()+cp.getKillsNeutras();
		}
		return i;
	}
	
	public int getTotalMortes(){
		int i = 0;
		for(ClanPlayer cp:getMembros()){
			i+=cp.getMorreu();
		}
		return i;
	}
	
	public double getKDR(){
		double kdr=0;
		int totalKills=getTotalKills();
		int totalMorte=getTotalMortes();
		if(totalKills==0&&totalMorte==0){
			return 0;
		}
		if(totalMorte==0){
			return totalKills;
		}
		kdr = totalKills/totalMorte;
		if(String.valueOf(kdr).length() >= 4){
			kdr = Double.valueOf(String.valueOf((double)kdr).substring(0, 4));
		}
		if(totalKills<totalMorte){
			kdr=-kdr;
		}
		return kdr;
	}
	
	public int getID(){
		return id;
	}
	
	public String getTag(){
		return tag;
	}
	
	public String getColorTag(){
		return colorTag;
	}
	
	public String getNomeClan(){
		return clanNome;
	}
	
	public long getDataFundacao(){
		return dataFundacao;
	}
	
	public boolean isVerificada(){
		return verificada;
	}
	
	public boolean fogoAmigo(){
		return fogoAmigo;
	}
	
	public int getTotalMembros(){
		return getMembros().size();
	}
	
	public List<ClanPlayer> getLideres(){
		if(lideres==null||lideres.size()==0){
			setMembros(Main.pl.getMembros(this));
		}
		return lideres;
	}
	
	public List<ClanPlayer> getMembros(){
		if(membros==null||membros.size()==0){
			setMembros(Main.pl.getMembros(this));
		}
		return membros;
	}
	
	public void setAllys(List<String> a){
		allys=a;
	}
	
	public void setRivais(List<String> r){
		rivais=r;
	}
	
	public void setFogoAmigo(boolean b,boolean c){
		fogoAmigo=b;
		if(c){
			Main.pl.setFogoAmigoClan(getID(), b?1:2);
		}
	}
	
	public List<String> getAllys(){
		return allys;
	}
	
	public List<String> getRivais(){
		return rivais;
	}
	
	public void setMembros(List<ClanPlayer> membros){
		for(ClanPlayer cp:membros){
			if(cp.isLider()){
				lideres.add(cp);
			}
		}
		this.membros=membros;
	}
	
	public void sendMsgLideres(List<String> msg,String c){
		if(getLideres()==null||getLideres().size()==0){
			setMembros(Main.pl.getMembros(this));
		}
		for(String m:msg){
			m=m.replace("&", "§");
			String[] d = m.split("@cmd");
			ChatAPI chat = new ChatAPI(d[0]);
			if(m.contains("@cmd")){
				String[] r = m.split("@cmd:");
				chat.Action(Action.RUN_COMMAND, r[1].replace("@clan", c).replace("@player", c));
			}
			for(ClanPlayer cp:getLideres()){
				Player p = Bukkit.getPlayer(cp.getPlayer());
				if(p!=null){
					chat.sendPlayers(p);
				}
			}
		}
	}
	
	public String toStringRivais(){
		String s = "";
		for(String a:getAllys()){
			if(a.equalsIgnoreCase("")){
				s=a;
			}else{
				s=s+","+a;
			}
		}
		if(s.startsWith(",")){
			s=s.substring(1);
		}
		return s;
	}
	
	public String toStringAlly(){
		String s = "";
		for(String a:getAllys()){
			if(a.equalsIgnoreCase("")){
				s=a;
			}else{
				s=s+","+a;
			}
		}
		if(s.startsWith(",")){
			s=s.substring(1);
		}
		return s;
	}
	
	public String getNomeLideres(){
		String l="Nenhum";
		for(ClanPlayer cp:getLideres()){
			if(l.equals("Nenhum")){
				l="§a"+cp.getPlayer();
			}else{
				l=l+"§6, §a"+cp.getPlayer();
			}
		}
		return l;
	}
	
	public String getTagAliados(){
		String l="Nenhum";
		for(String cp:getAllys()){
			if(l.equals("Nenhum")){
				l="§a"+cp;
			}else{
				l=l+"§6, §a"+cp;
			}
		}
		return l;
	}
	
	public String getTagRivais(){
		String l="Nenhum";
		for(String cp:getRivais()){
			if(l.equals("Nenhum")){
				l="§a"+cp;
			}else{
				l=l+"§6, §a"+cp;
			}
		}
		return l;
	}	
	
	public boolean isAliado(ClanPlayer cp){ 
		if(getAllys().contains(cp.getClan().getTag())) return true;
		return false;
	}
	
	public boolean isRival(ClanPlayer cp){ 
		if(getRivais().contains(cp.getClan().getTag())) return true;
		return false;
	}
	
	public boolean isAliado(Clan c){ 
		if(getAllys().contains(c.getTag())) return true;
		return false;
	}
	
	public boolean isRival(Clan c){ 
		if(getRivais().contains(c.getTag())) return true;
		return false;
	}
	
}
