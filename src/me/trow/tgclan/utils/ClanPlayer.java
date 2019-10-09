package me.trow.tgclan.utils;

import java.util.Date;

import me.trow.tgclan.Main;

public class ClanPlayer {
	
	private String pNick,tagClan,colorTagClan,tagChatClan;
	private int morreu,killsNeutras,killsCivis;
	private boolean confiavel,lider,temClan,temInfo;
	public long ultimaVezOnline,cpRecente;
	public double kdr;
	private Clan clan;
	
	public ClanPlayer(String p,String tagClan,String colorTagClan,String tagChatClan,int morreu,int killsNeutras,int killsCivis,boolean confiavel
			,boolean lider,boolean temClan,long ultimaVezOnline){
		pNick=p;
		this.tagClan=tagClan;
		this.colorTagClan=colorTagClan;
		this.tagChatClan=tagChatClan;
		this.morreu=morreu;
		this.killsNeutras=killsNeutras;
		this.killsCivis=killsCivis;
		this.confiavel=confiavel;
		this.lider=lider;
		this.temClan=temClan;
		this.ultimaVezOnline=ultimaVezOnline;
		temInfo=true;
		clan=Main.pl.getClan(tagClan);
		double matouTotal = killsNeutras+killsCivis;
		if(matouTotal==0&&morreu==0){
			kdr=0;
		}
		if(morreu==0){
			kdr=matouTotal;
		}
		if(morreu!=0&&matouTotal!=0){
			kdr=matouTotal/(double)morreu;
			if(String.valueOf(kdr).length() >= 4){
				kdr = Double.valueOf(String.valueOf((double)kdr).substring(0, 4));
			}
			if(matouTotal<morreu){
				kdr=-kdr;
			}
		}
	}
	
	public ClanPlayer(String p){
		pNick=p;
		tagClan="";
		colorTagClan="";
		tagChatClan="";
		temClan=false;
		lider=false;
		morreu=0;
		killsCivis=0;
		killsNeutras=0;
		ultimaVezOnline=(new Date()).getTime();
		int matouTotal = killsNeutras+killsCivis;
		kdr = matouTotal*morreu/100;
		temInfo=false;
	}
	
	public long getCPRecente(){
		return cpRecente;
	}
	
	public void setCPRecente(long l){
		cpRecente=l;
	}
	
	public void setInfo(boolean b){
		temInfo=b;
		Main.pl.criarInfoPlayer(getPlayer());
	}
	
	public boolean temInfo(){
		return temInfo;
	}
	
	public double getKDR(){
		return kdr;
	}
	
	public Clan getClan(){
		return clan;
	}
	
	public void setClan(Clan c){
		clan=c;
		if(c==null){
			temClan=false;
		}else{
			temClan=true;
			tagClan=c.getTag();
			colorTagClan=c.getColorTag();
		}
	}
	
	public void setUltimaVezOnline(long l){
		ultimaVezOnline=l;
	}
	
	public void addNeutra(){
		killsNeutras+=1;
	}
	
	public void addCivil(){
		killsCivis+=1;
	}
	
	public void addDeath(){
		morreu+=1;
	}
	
	public void setTagChat(String tagChat){
		if(!temClan()) return;
		tagChatClan=tagChat;
		Main.pl.setTagChatPlayer(getPlayer(), tagChat);
	}
	
	public void setLider(boolean b){
		if(!temClan()) return;
		lider=b;
		Main.pl.mudarLider(getPlayer(), b);
	}
	
	public void setConfiavel(boolean b){
		confiavel=b;
		Main.pl.setConfiavel(getPlayer(), b);
	}
	
	public boolean temClan(){
		return temClan;
	}
	
	public boolean isLider(){
		return lider;
	}
	
	public boolean isConfiavel(){
		return confiavel;
	}
	
	public int getKillsCivis(){
		return killsCivis;
	}
	
	public int getKillsNeutras(){
		return killsNeutras;
	}
	
	public int getMorreu(){
		return morreu;
	}
	
	public String getPlayer(){
		return pNick;
	}
	
	public String getTagClanChat(){
		return tagChatClan;
	}
	
	public String getTagClan(){
		return tagClan;
	}
	
	public String getColorTag(){
		if(getClan()==null) return "";
		if(colorTagClan==""&&getTagClan()!=null&&!getTagClan().equalsIgnoreCase("")){
			colorTagClan=Main.pl.getClan(tagClan).getColorTag();
		}
		return colorTagClan;
	}
	
	public boolean mesmoClan(ClanPlayer cp2){
		if(getClan()==null) return false;
		if(cp2.getClan()==null) return false;
		if(getTagClan().equalsIgnoreCase(cp2.getTagClan())){
			if(getClan().getID()==cp2.getClan().getID()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isAliado(ClanPlayer cp){ 
		if(getClan()==null) return false;
		if(cp.getClan()==null) return false;
		if(getClan().getAllys().contains(cp.getClan().getTag())) return true;
		return false;
	}
	
	public boolean isRival(ClanPlayer cp){ 
		if(getClan()==null) return false;
		if(getClan().getRivais().contains(cp.getClan().getTag())) return true;
		return false;
	}
	
	public boolean isAliado(Clan c){ 
		if(getClan()==null) return false;
		if(getClan().getAllys().contains(c.getTag())) return true;
		return false;
	}
	
	public boolean isRival(Clan c){ 
		if(getClan()==null) return false;
		if(getClan().getRivais().contains(c.getTag())) return true;
		return false;
	}
	
}
