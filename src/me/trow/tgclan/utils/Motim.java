package me.trow.tgclan.utils;

import java.util.HashMap;

public class Motim {
	
	private ClanPlayer cpCandidato;
	private HashMap<ClanPlayer, Boolean> votos;
	private Clan c;
	private int votosPRO,votosContra;
	
	public Motim(ClanPlayer cp){
		cpCandidato=cp;
		c=cp.temClan()?cp.getClan():null;
		votos=new HashMap<>();
	}
	
	public HashMap<ClanPlayer, Boolean> getQuemVotou(){
		return votos;
	}
	
	public void addEleitor(ClanPlayer eleitor,boolean b){
		votos.put(eleitor, b);
	}
	
	public Clan getClan(){
		return c;
	}
	
	public ClanPlayer getCandidato(){
		return cpCandidato;
	}
	
	public int getVotosPRO(){
		return votosPRO;
	}
	
	public int getVotosContra(){
		return votosContra;
	}
	
	public boolean ganhandoVotacao(){
		if(getPorcentagemVotos(true)>getPorcentagemVotos(false)){
			return true;
		}
		return false;
	}
	
	public boolean jaVotou(ClanPlayer eleitor){
		for(ClanPlayer ele:votos.keySet()){
			if(ele.getPlayer().equalsIgnoreCase(eleitor.getPlayer())){
				return true;
			}
		}
		return false;
	}
	
	public boolean getVoto(ClanPlayer eleitor){
		if(jaVotou(eleitor)){
			for(ClanPlayer e:votos.keySet()){
				if(e.getPlayer().equalsIgnoreCase(eleitor.getPlayer())){
					return votos.get(e);
				}
			}
		}
		return false;
	}
	
	public double getPorcentagemVotos(boolean proOrContra){
		int pro = getVotosPRO();
		int con = getVotosContra();
		int tot = getClan().getMembros().size(); // 100%
		if(proOrContra){
			return ((pro*100)/tot);
		}else{
			return ((con*100)/tot);
		}
	}
	
	public void addVoto(boolean pro){
		if(pro){
			votosPRO=votosPRO+1;
		}else{
			votosContra=votosContra+1;
		}
	}
	
	public boolean empate(){
		double pro=getPorcentagemVotos(true);
		double con=getPorcentagemVotos(false);
		return pro==con;
	}
	
	public boolean acabou(){
		double pro=getPorcentagemVotos(true);
		double con=getPorcentagemVotos(false);
		if(pro>50) return true;
		if(con>50) return true;
		if(pro==con) return true;
		return false;
	}
	
}
