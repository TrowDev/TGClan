package me.trow.tgclan.cmds;

import java.util.List;

import me.trow.tgclan.Main;
import me.trow.tgclan.cmds.slots.Slots;
import me.trow.tgclan.utils.ChatAPI;
import me.trow.tgclan.utils.ClanPlayer;
import net.md_5.bungee.api.chat.ClickEvent.Action;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ListaDeComandos implements CommandExecutor{
	
	private Main pl = Main.pl;
	FileConfiguration cnf = pl.getConfig();
	
	public static boolean hasCGUI=false;
	
	public String getMsg(String msg){
		return cnf.getString(msg).replace("&","§");
	}
	
	public void sendMessage(Player p,String msg){
		if(p==null)return;
		p.sendMessage(msg);
		//p.sendMessage(getMsg(msg));
	}
	
	public boolean isInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch (Exception e){}
		return false;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(label.equalsIgnoreCase("clan")){
			//Main u=new Main();
			
			ClanPlayer cp = null;//pl.getMembro(p.getName());
			Player p=null;
			if(sender instanceof Player){
				p = (Player)sender;
				cp=pl.getMembro(p.getName());
			}
			if(cp!=null&&!cp.temInfo()){
				cp.setInfo(true);
			}
			if(args.length == 0){
				if(!(sender instanceof Player)){
					sender.sendMessage("§cComando exclusivo para players.");
					return true;
				}
				if(hasCGUI){
					p.chat("/cgui");
				}else{
					showCmds(p, 1);
				}
			}
			if(args.length >= 1){
				String cmd1 = args[0];
				boolean b = true;
				if(isInt(cmd1)){
					b=false;
					int pag = Integer.parseInt(cmd1);
					showCmds(p, pag);
					return false;
				}
				if(cmd1.equalsIgnoreCase("rl")){
					b=false;
					if(!sender.hasPermission("tgclan.staff")){
						return false;
					}
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload TGTorre");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload TGTerrenos");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload TGTab");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload TGClanGUI");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman unload TGChatJSON");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman reload TGClan");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman load TGTorre");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman load TGTerrenos");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman load TGTab");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman load TGClanGUI");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "plugman load TGChatJSON");
					sender.sendMessage("§3[Clan] §bRecarregado!");
				}
				if(cmd1.equalsIgnoreCase("backup")){
					b=false;
					if(!sender.hasPermission("tgclan.staff")) return false;
					pl.copyBackUpSQL();
					sender.sendMessage("§3[Clan] §bBackUP copiado com sucesso!");
				}
				if(cmd1.equalsIgnoreCase("money")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao"));
						return false;
					}
					Money m = new Money();
					m.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("home")||cmd1.equalsIgnoreCase("sethome")||cmd1.equalsIgnoreCase("delhome")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					Home h = new Home();
					h.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("slots")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					Slots s = new Slots();
					s.execute(sender, args);
				}
				if(cmd1.equalsIgnoreCase("moditem")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					ModItem mi = new ModItem();
					mi.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("motimadm")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					MotimAdm ma = new MotimAdm();
					ma.execute(sender, args);
				}
				if(cmd1.equalsIgnoreCase("clans")||cmd1.equalsIgnoreCase("list")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Clans clans = new Clans();
					clans.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("confiar")||cmd1.equalsIgnoreCase("trust")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Confiar c = new Confiar();
					c.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("criar")||cmd1.equalsIgnoreCase("create")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Criar c=new Criar();
					c.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("deletar")||cmd1.equalsIgnoreCase("disband")||cmd1.equalsIgnoreCase("delete")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Deletar del=new Deletar();
					del.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("aceitar")||cmd1.equalsIgnoreCase("accept")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Aceitar ac=new Aceitar();
					ac.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("negar")||cmd1.equalsIgnoreCase("deny")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Negar n=new Negar();
					n.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("convidar")||cmd1.equalsIgnoreCase("invite")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Convidar conv=new Convidar();
					conv.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("stats")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Stats s=new Stats();
					s.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("info")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Info i=new Info();
					i.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("lider")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Lider l=new Lider();
					l.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("tirarlider")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					TirarLider tl=new TirarLider();
					tl.execute(p, args);
				}
				// CHAT REGISTRADO NA MAIN.
				if(cmd1.equalsIgnoreCase("prefix")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Prefix pre=new Prefix();
					pre.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("pvp")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					PvP pvp=new PvP();
					pvp.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("resetkdr")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					ResetKDR rKDR=new ResetKDR();
					rKDR.execute(sender, args);
				}
				if(cmd1.equalsIgnoreCase("kick")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Kick kick=new Kick();
					kick.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("sair")||cmd1.equalsIgnoreCase("leave")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Sair s=new Sair();
					s.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("remover")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					Remover r =new Remover();
					r.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("aliado")||cmd1.equalsIgnoreCase("ally")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Aliado ally=new Aliado();
					ally.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("rival")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Rival r =new Rival();
					r.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("modtag")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					ModTag mt = new ModTag();
					mt.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("aceitaralianca")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					AceitarAliado aa=new AceitarAliado();
					aa.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("negaralianca")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					NegarAlianca aa=new NegarAlianca();
					aa.execute(p, args);
				}
				if(cmd1.equalsIgnoreCase("motim")){
					b=false;
					if(!pl.hasPermissionUser(p, cmd1)){
						p.sendMessage(getMsg("Msg.Sem_Permissao").replace("@perm", cmd1));
						return false;
					}
					if(p==null) return false;
					Motim m = new Motim();
					m.execute(p, args);
				}
				if(b){
					new ChatAPI(getMsg("Msg.Comando_Nao_Existe")).Action(Action.RUN_COMMAND, "/clan 1").sendPlayers(p);
					return false;
				}
			}
		}
		return false;
	}
	
	public void showCmds(Player p,int pag){
		int porPag = pl.getConfig().getInt("Config.Por_Pag.Comandos");
		int pul = (pag*porPag)-porPag;
		List<String> cmds = pl.getConfig().getStringList("Comandos");
		if(pul>=cmds.size()){
			if(p.hasPermission("tgclan.staff")||p.hasPermission("tgclan.usar.*")||p.isOp()){
				for(String v:pl.getConfig().getStringList("Comandos_Staff")){
					v=v.replace("&", "§");
					p.sendMessage(v);
				}
				return;
			}
			p.sendMessage(getMsg("Msg.Sem_Mais_Comandos"));
			return;
		}
		new ChatAPI(getMsg("Msg.Prox_Pag_Comandos").replace("@prox", (pag+1)+"")).Action(Action.RUN_COMMAND, "/clan "+(pag+1)).sendPlayers(p);
		int i = 0;
		int count=0;
		for(String v:cmds){
			v=v.replace("&", "§");
			i++;
			if(i<=pul) continue;
			count++;
			if(count>porPag) break;
			String[] d = v.split("->");
			ChatAPI c = new ChatAPI(d[0]);
			if(v.contains("@cmd")){
				c.Action(Action.SUGGEST_COMMAND, d[1].replace("@cmd:", ""));
			}
			c.sendPlayers(p);
		}
		new ChatAPI(getMsg("Msg.Prox_Pag_Comandos").replace("@prox", (pag+1)+"")).Action(Action.RUN_COMMAND, "/clan "+(pag+1)).sendPlayers(p);
	}
	
}
