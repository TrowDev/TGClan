package me.trow.tgclan.utils;

import me.trow.tgclan.Main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class SetTag {
	
	private ScoreboardManager sm;
	private Scoreboard sb;
	private Team t;
	private Main pl = Main.pl;
	private FileConfiguration cnf = pl.getConfig();
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&", "§");
	}
	
	public void update(Player p){
		sm=Bukkit.getScoreboardManager();
		sb=sm.getMainScoreboard();
		ClanPlayer cp = pl.getMembro(p.getName());
		for(String tag:cnf.getConfigurationSection("TabTag.Grupos").getKeys(false)){
			if(p.hasPermission("tgclan.tag."+tag)){
				boolean containsClan=cnf.getString("TabTag.Grupos."+tag).contains("{clan}");
				boolean tagClanEnable=cnf.getBoolean("TabTag.Tag.Ativar");
				boolean temClan=cp.temClan();
				if(containsClan&&tagClanEnable&&temClan){
					String clanTag = cp.getTagClan();
					String hasNameTeam=clanTag+"_"+tag;
					String nameTeam;
					if(hasNameTeam.length()<=16){
						nameTeam=hasNameTeam;
					}else{
						nameTeam=hasNameTeam.substring(0,16);
					}
					String clanTagColor=cp.getColorTag();
					
					String tagClanColor=getMsg("TabTag.Tag.Formato").replace("{tag}", clanTagColor);
					String tagClan = getMsg("TabTag.Tag.Formato").replace("{tag}", clanTag);
					
					String getTagFinalColor = getMsg("TabTag.Grupos."+tag).replace("{clan}", tagClanColor);
					String getTagFinal = getMsg("TabTag.Grupos."+tag).replace("{clan}", tagClan);
					String tagFinal;
					
					if(getTagFinalColor.length()<=16){
						tagFinal=getTagFinalColor;
					}else{
						if(getTagFinal.length()<=16){
							tagFinal=getTagFinal;
						}else{
							tagFinal="";
						}
					}
					if (sb.getTeam(nameTeam) != null) {
						t = sb.getTeam(nameTeam);
						t.addPlayer(p);
						t.setPrefix(tagFinal);
					} else {
						t = sb.registerNewTeam(nameTeam);
						t.addPlayer(p);
						t.setPrefix(tagFinal);
					}
				}else{
					String nameTeam;
					if (tag.length() <= 16) {
						nameTeam = tag;
					} else {
						nameTeam = tag.substring(0, 16);
					}
					String getTagFinal = pl.getConfig()
							.getString("TabTag.Grupos." + tag)
							.replace("&", "§").replace("{clan}", "");
					String tagFinal;
					if (getTagFinal.length() <= 16) {
						tagFinal = getTagFinal;
					} else {
						tagFinal = "";
					}
					if (sb.getTeam(nameTeam) != null) {
						t = sb.getTeam(nameTeam);
						t.addPlayer(p);
						t.setPrefix(tagFinal);
					} else {
						t = sb.registerNewTeam(nameTeam);
						t.addPlayer(p);
						t.setPrefix(tagFinal);
					}
				}
			}
		}
	}
	


	public void setScoreBoard(Player p) {
		p.setScoreboard(sb);
	}
	
}
