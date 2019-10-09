



import java.util.Arrays;

import me.trow.tgclan.Main;
import me.trow.tgclan.cmds.Chat;
import me.trow.tgclan.utils.ClanPlayer;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class Eventos2 implements Listener{
	
	private Main pl=Main.pl;
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		//
		Player p =e.getPlayer();
		ClanPlayer cp = pl.getMembro(p.getName());
		if(!cp.temInfo()){
			cp.setInfo(true);
		}
	}
	
	@EventHandler
	public void morreu(PlayerDeathEvent e){
		if(e.getEntity().getKiller() instanceof Player){
			Player p = e.getEntity();
			//Main u =new Main();
			Player k =null;
			EntityDamageEvent ede = p.getLastDamageCause();
			if(ede instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent edbe=(EntityDamageByEntityEvent)ede;
				if(edbe.getDamager() instanceof Player){
					k=(Player)edbe.getDamager();
				}else if(edbe.getDamager() instanceof Arrow){
					Arrow arrow=(Arrow)edbe.getDamager();
					if(arrow.getShooter() instanceof Player){
						k=(Player)arrow.getShooter();
					}
				}
			}
			pl.addMorte(p.getName());
			if(k==null) return;
			ClanPlayer cp = pl.getMembro(p.getName(),true);
			if(!cp.temClan()){
				pl.addKill(k.getName(), true);
			}else{
				/*ClanPlayer cp2 = pl.getMembro(k.getName(),true);
				if(cp2.temClan()&&!cp.isAliado(cp2)){
					if(!cp.isRival(cp2)){
						pl.addKill(k.getName(), false);
					}
				}else{
					pl.addKill(k.getName(), false);
				}*/
				pl.addKill(k.getName(), false);
			}
		}
	}
	
	public String getMsg(String msg){
		return pl.getConfig().getString(msg).replace("&", "§");
	}
	
	@EventHandler
	public void morrer(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player en=(Player)e.getEntity();
			//ClanPlayer cp = pl.cpRecente(en.getName())?pl.getCPRecente(en.getName()):pl.getMembro(en.getName());
			ClanPlayer cp = pl.getMembro(en.getName(),true);
			if(cp.temClan()){
				if(e.getDamager() instanceof Player){
					Player d=(Player)e.getDamager();
					ClanPlayer cpD = pl.getMembro(d.getName(),true);
					//ClanPlayer cpD = pl.cpRecente(d.getName())?pl.getCPRecente(d.getName()):pl.getMembro(d.getName());
					//String clanAtacante=cpD.getTagClan();
					//String clanAtacado=cpD.getTagClan();
					if(!cpD.temClan()) return;
					if(cp.mesmoClan(cpD)){
						if(!cp.getClan().fogoAmigo()){
							e.setCancelled(true);
							d.sendMessage(getMsg("Msg.Mesmo_Clan").replace("@player", en.getName()));
							return;
						}
					}
					if(cp.isAliado(cpD)){
						if(!cp.getClan().fogoAmigo()&&!cpD.getClan().fogoAmigo()){
							e.setCancelled(true);
							d.sendMessage(getMsg("Msg.Clan_Aliado_Com_O_Clan_Do_Player").replace("@player", en.getName()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void chat(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String msg = e.getMessage();
		String[] args = Arrays.copyOfRange(msg.split(" "), 1, msg.split(" ").length);
		if(msg.startsWith("/.")){
			e.setCancelled(true);
			Chat chat=new Chat();
			chat.execute(p, args);
		}
	}
	
}
