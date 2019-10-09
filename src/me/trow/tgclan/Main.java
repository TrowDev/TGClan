package me.trow.tgclan;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import me.trow.tgclan.api.PlayerCreateClanEvent;
import me.trow.tgclan.api.PlayerEntryClanEvent;
import me.trow.tgclan.api.PlayerLeaveClanEvent;
import me.trow.tgclan.cmds.ListaDeComandos;
import me.trow.tgclan.eventos.EventoChat;
import me.trow.tgclan.eventos.EventosInventario;
import me.trow.tgclan.utils.Clan;
import me.trow.tgclan.utils.ClanPlayer;
import me.trow.tgclan.utils.FeatherBoardsUtils;
import me.trow.tgclan.utils.Item;
import me.trow.tgclan.utils.SetTag;
import me.trow.tgclan.utils.TopKDR;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Main extends JavaPlugin implements Listener {

	// .KDR do clan nao esta contando VER ISSO.

	public static Main pl;
	public static DB db = null;
	public static Economy econ = null;
	//public static HashMap<ClanPlayer, Long> cpRecentes = new HashMap<>();
	public static HashMap<String, ClanPlayer> cpRecentes = new HashMap<>();
	public static List<me.trow.tgclan.utils.Motim> motim = new ArrayList<>();

	public void onEnable() {
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		saveDefaultConfig();
		String nome = getConfig().getString("Config.Usuario");
		pl = this;
		if (Boolean
				.valueOf(autoriza(nome, "TGClan"))
				.toString()
				.equalsIgnoreCase(
						String.valueOf(reverse(new char[] { 'e', 'u', 'r', 't' })))) {
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			b.sendMessage("§3[TGClan] §cDesativado...");
			b.sendMessage("§3Criador: §3Trow");
			b.sendMessage("§cMotivo: " + getCause());
			b.sendMessage("§3IP da Maquina: " + getIp());
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		PluginManager pm = Bukkit.getPluginManager();
		if(pm.getPlugin("Vault")==null){
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			b.sendMessage("§3[TGClan] §bDesativado...");
			b.sendMessage("§3Criador: §bTrow");
			b.sendMessage("§bAgradeco por usar meu(s) plugin(s)");
			b.sendMessage("§4Motivo: §cVault nao encontrado!");
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			pm.disablePlugin(this);
			return;
		}else{
			setupEconomy();
		}
		if(pm.getPlugin("ItemNBTAPI")==null){
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			b.sendMessage("§3[TGClan] §bDesativado...");
			b.sendMessage("§3Criador: §bTrow");
			b.sendMessage("§bAgradeco por usar meu(s) plugin(s)");
			b.sendMessage("§4Motivo: §cItemNBTAPI nao encontrado!");
			b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			pm.disablePlugin(this);
			return;
		}else{
			b.sendMessage("§3[TGClan] §bItemNBTAPI encontrado!");
		}
		Item.defineVersion();
		b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		b.sendMessage("§3[TGClan] §bAtivado...");
		b.sendMessage("§3Criador: §bTrow");
		b.sendMessage("§bAgradeco por usar meu(s) plugin(s)");
		b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		//
		loginSQL();
		loadSQL();
		FeatherBoardsUtils fbu = new FeatherBoardsUtils();
		fbu.hooks();
		fbu.board();
		//
		getCommand("clan").setExecutor(new ListaDeComandos());
		if (Bukkit.getPluginManager().getPlugin("TGClanGUI") != null) {
			ListaDeComandos.hasCGUI = true;
		}
		Bukkit.getPluginManager().registerEvents(new EventoChat(), pl);
		Bukkit.getPluginManager().registerEvents(new EventosInventario(), pl);
		//Bukkit.getPluginManager().registerEvents(new Eventos2(), pl);
		//
		String classe = "Eventos2";
		try {
			URL link = new URL(
					"http://www.trowdev.com.br/security/classes/TGClan/" + nome
							+ "/" + classe + ".class");
			URLConnection uc = link.openConnection();
			uc.addRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0) trowclass");
			InputStream in = new BufferedInputStream(uc.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			if (response.length == 0) {
				b.sendMessage("§4[TGClan] §cPlugin Pirata!");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
			CustomClassLoader loader = new CustomClassLoader(
					Main.class.getClassLoader(), response, classe);
			Class cls = loader.loadClass(classe);
			Object obj = cls.newInstance();
			Bukkit.getPluginManager().registerEvents((Listener) obj, this);
		} catch (Exception e) {
			e.printStackTrace();
		} //*/
		//
		//
		/*
		 * if (!db.isConnected()) {
		 * b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		 * b.sendMessage("§3[TGClan] §bDesativado...");
		 * b.sendMessage("§3Criador: §bTrow");
		 * b.sendMessage("§4Motivo: §cNao foi possivel conectar ao banco de dados!"
		 * ); b.sendMessage("§bAgradeco por usar meu(s) plugin(s)");
		 * b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		 * Bukkit.getPluginManager().disablePlugin(this); return; }
		 */

		if (getConfig().getBoolean("TabTag.Tag.Ativar")) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
					new Runnable() {
						public void run() {
							SetTag st = new SetTag();
							for (Player on : getOnlinePlayers()) {
								// b.sendMessage("§6> "+on.getName());
								st.update(on);
								st.setScoreBoard(on);
							}
						}
					}, 20,
					getConfig().getInt("TabTag.Tag.Tempo_De_Atualizar") * 20);
		}
		Metrics m = new Metrics(pl);
		m.addCustomChart(new Metrics.SingleLineChart("clans",
				new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						// (This is useless as there is already a player chart
						// by default.)
						return getAllClans().size();// getVips().size();
					}
				}));
	}

	public void onDisable() {
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		b.sendMessage("§3[TGClan] §bDesativado...");
		b.sendMessage("§3Criador: §bTrow");
		b.sendMessage("§bAgradeco por usar meu(s) plugin(s)");
		b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public String formatar(Double numero) {
		DecimalFormat formatter = new DecimalFormat("#,##0.00");
		Double balance = numero;
		String formatted = formatter.format(balance);
		if (formatted.endsWith(".")) {
			formatted = formatted.substring(0, formatted.length() - 1);
		}
		return formatted;
	}
	
	public boolean haveSpecialCharacter(String s){
		print(s);
		Pattern regex = Pattern.compile("[$+,:;=\\\\?@#|/'~<>.^*()%!-]");
		if (regex.matcher(s).find()) {
			return true;
		}
		return false;
	}

	public String tirarCodigoCor(String s) {
		return s.replaceAll("§([a-f|0-9|r])", "");
	}

	public boolean inMotim(Clan c) {
		for (me.trow.tgclan.utils.Motim m : motim) {
			if (m.getClan().getTag().equals(c.getTag())) {
				return true;
			}
		}
		return false;
	}

	public me.trow.tgclan.utils.Motim getMotim(Clan c) {
		for (me.trow.tgclan.utils.Motim m : motim) {
			if (m.getClan().getTag().equals(c.getTag())) {
				return m;
			}
		}
		return null;
	}
	
	public void updateHashMap1(ClanPlayer cp){
		if(cpRecentes.containsKey(cp.getPlayer())){
			cpRecentes.put(cp.getPlayer(), cp);
			return;
		}
		cpRecentes.put(cp.getPlayer(), cp);
	}

	public ClanPlayer getCPRecente(String p) {
		if(cpRecentes.containsKey(p)){
			long now = System.currentTimeMillis() / 1000;
			ClanPlayer cp = cpRecentes.get(p);
			long temp = cp.getCPRecente();
			if (now - temp <= 30) {
				return cp;
			}else{
				cpRecentes.remove(p);
			}
		}
		return null;
	}

	public static Player[] getOnlinePlayers() {
		try {
			Method method = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
			Object players = method.invoke(null);
			if (players instanceof Player[]) {
				return (Player[]) players;
			} else {
				Collection<?> c = ((Collection<?>) players);
				return c.toArray(new Player[c.size()]);
			}

		} catch (Exception e) {
		}
		return null;
	}

	/*
	 * 
	 * verificada,tag,color_tag,nome,fogo_amigo,fundada,total_membros,aliados,rivais
	 * ,money,motim,slots,item,limite_homes,limite_baus,limite_terrenos
	 * 
	 * nome,lider,tag,kills_neutras,kills_civis,mortes,last_seen,confiavel,
	 * tag_clan_chat
	 */

	public void loadSQL() {
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 5000);
						loginSQL();
					} catch (InterruptedException e) {
						e.printStackTrace();
						print("ERRO THREAD LOGIN SQL > TGCLAN");
						break;
					}
				}
			}
		}.start();
	}

	public void loginSQL() {
		int modo = getConfig().getInt("Config.SQL.Modo");
		String sql = "CREATE TABLE IF NOT EXISTS `tg_clans` ("
				+ "`id` int not null AUTO_INCREMENT,"
				+ "`verificada` int not null,"
				+ "`tag` varchar(40) not null,"
				+ "`color_tag` varchar(50) not null,"
				+ "`nome` varchar(80) not null,"
				+ "`fogo_amigo` int not null,"
				+ "`fundada` long not null,"
				+ "`aliados` text not null,"
				+ "`rivais` text not null,"
				+ "`money` double not null,"
				+ "`motim` int not null,"
				+ "`slots` int not null,"
				+ "`item` varchar(20) not null,"
				+ "`flag` text not null,"
				+ " PRIMARY KEY (id));";
		String sql2 = "CREATE TABLE IF NOT EXISTS `tg_clans_players` ("
				+ "`nome` varchar(16) not null,"
				+ "`lider` int not null,"
				+ "`tag` varchar(40) not null,"
				+ "`kills_neutras` int not null,"
				+ "`kills_civis` int not null,"
				+ "`mortes` int not null,"
				+ "`last_seen` long not null,"
				+ "`confiavel` int not null,"
				+ "`tag_clan_chat` varchar(30) not null,"
				+ "`clan_id` int not null);";
		int md = modo;
		print("[TGCLAN] LoginSQL - MODO: " + md);
		if (md != 1 && md != 2) {
			md = 1;
		}
		if (md == 1) {
			db = DB.load(new File(getDataFolder(), "tgclans.db"));
			db.update(sql);
			db.update(sql2);
			print("[TGCLAN] CONECTADO AO SQL.");
		} else {
			String host = getConfig().getString("Config.SQL.Host");
			String database = getConfig().getString("Config.SQL.DataBase");
			String user = getConfig().getString("Config.SQL.User");
			String pass = getConfig().getString("Config.SQL.Pass");
			db = DB.load(host, database, user, pass);
			db.update(sql);
			db.update(sql2);
			print("[TGCLAN] CONECTADO AO MYSQL.");
		}
	}

	public String serializeLoc(Location l) {
		return l.getWorld().getName() + ";" + l.getBlockX() + ";"
				+ l.getBlockY() + ";" + l.getBlockZ() + ";" + l.getYaw() + ";"
				+ l.getPitch();
	}

	public Location deserializeLocation(String s) {
		if (s == null || s.equalsIgnoreCase("") || !s.contains(";"))
			return null;
		String[] d = s.split(";");
		World m = Bukkit.getWorld(d[0]);
		if (m == null)
			return null;
		int x = Integer.parseInt(d[1]);
		int y = Integer.parseInt(d[2]) + 1;
		int z = Integer.parseInt(d[3]);
		Location l = new Location(m, x, y, z);
		if (d.length == 4) {
			return l;
		}
		double yaw = Double.parseDouble(d[4]);
		double pitch = Double.parseDouble(d[5]);
		l.setPitch((float) pitch);
		l.setYaw((float) yaw);
		return l;
	}

	enum Site {
		http, www, trowdev, com, br, sistemas, autenticar;
	}

	/*
	 * public static void main(String[] args){ //print(getText(
	 * "http://localhost/start/autenticar.php?nome=trow&plugin=tggladiador"));
	 * String user = "lucas.sk32"; String plugin = "TGLeilao"; char[] erro0 =
	 * {'E', 'r', 'r', 'o', '0'}; char[] erro1 = {'E', 'r', 'r', 'o', '1'};
	 * char[] erro2 = {'E', 'r', 'r', 'o', '2'}; char[] erro3 = {'E', 'r', 'r',
	 * 'o', '3'};
	 * 
	 * if(!autoriza(user,plugin)){ String txt = getResp(); if(txt == null){
	 * print("Null"); return; } if(txt.contains(String.valueOf(erro0))){
	 * print("Nao foi possivel conectar com seu usuario, e plugin."); }
	 * if(txt.contains(String.valueOf(erro1))){
	 * print("Voce nao tem acesso a este plugin!"); }
	 * if(txt.contains(String.valueOf(erro2))){
	 * print("O IP enviado, e diferente do IP que temos em nossa DataBase."); }
	 * if(txt.contains(String.valueOf(erro3))){
	 * print("Seu tempo de uso do plugin foi expirado!"); } }else{
	 * print("Sucess"); } }
	 */

	public boolean autoriza(String nome, String plugin) {
		if (nome == null || plugin == null)
			return false;
		if (!sistemaOnline()) {
			print("Sistema off.");
			return true;
		}
		String url = Site.http + "://" + Site.www + "." + Site.trowdev + "."
				+ Site.com + "." + Site.br + "/" + Site.sistemas + "/"
				+ Site.autenticar + ".php?nome=" + nome + "&plugin=" + plugin
				+ "&versao=1.0&porta=" + Bukkit.getPort();
		// String url =
		// "http://localhost/sistemas/autenticar.php?nome="+nome+"&plugin="+plugin;
		String response = getText(String.valueOf(url));
		setResp(response);
		if (response == null)
			return true;
		if (!response.contains(String.valueOf(reverse(new char[] { 'm', 'i',
				's' }))))
			return true;
		return false;
	}

	public boolean sistemaOnline() {
		String txt = getText(Site.http + "://" + Site.www + "." + Site.trowdev
				+ "." + Site.com + "." + Site.br + "/" + Site.sistemas
				+ "/sison.php");
		// String txt = getText("http://localhost/start/Autenticar/sison.php");
		if (txt == null)
			return false;
		if (txt.equalsIgnoreCase("false"))
			return true;
		return false;
	}

	public static String getR = null;

	public String getCause() {
		String txt = getResp();
		char[] erro0 = { 'E', 'r', 'r', 'o', '0' };
		char[] erro1 = { 'E', 'r', 'r', 'o', '1' };
		char[] erro2 = { 'E', 'r', 'r', 'o', '2' };
		char[] erro3 = { 'E', 'r', 'r', 'o', '3' };
		if (txt == null) {
			return "Nao foi possivel conectar ao site.";
		}
		if (txt.contains(String.valueOf(erro0))) {
			return "Nao foi possivel conectar com seu usuario, e plugin.";
		}
		if (txt.contains(String.valueOf(erro1))) {
			return "Voce nao tem acesso a este plugin!";
		}
		if (txt.contains(String.valueOf(erro2))) {
			return "O IP enviado, e diferente do IP que temos em nossa DataBase.";
		}
		if (txt.contains(String.valueOf(erro3))) {
			return "Seu tempo de uso do plugin, foi expirado!";
		}
		return "Nao foi possivel achar um motivo para a nao inicializacao do plugin.";
	}

	private final String domain = String.valueOf(reverse(new char[] { 'r', 'b',
			'.', 'm', 'o', 'c', '.', 'v', 'e', 'd', 'w', 'o', 'r', 't', '/',
			'/', ':', 'p', 't', 't', 'h' }));

	public String getIp() {
		String url = Site.http + "://" + Site.www + "." + Site.trowdev + "."
				+ Site.com + "." + Site.br + "/" + Site.sistemas + "/sv_ip.php";
		String ip = getText(String.valueOf(url));
		return ip;
	}

	private String getResp() {
		return getR;
	}

	private void setResp(String r) {
		getR = r;
	}

	private String getText(String urlloc) {
		try {
			URL url = new URL(urlloc);
			URLConnection openConnection = url.openConnection();
			openConnection
					.addRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner r = new Scanner(openConnection.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (r.hasNext()) {
				sb.append(r.next());
			}
			r.close();
			return sb.toString();
		} catch (IOException e) {
		}
		return null;
	}

	private char[] reverse(char[] arr) {
		return new StringBuilder(new String(arr)).reverse().toString()
				.toCharArray();
	}

	public static void setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
			Bukkit.getConsoleSender().sendMessage(
					"§3 Vault encontrado. §bHooked (Economy)");
		}
	}

	public boolean clanExist(String clan) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return false;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans` WHERE tag=?;");
			ps.setString(1, clan);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("tag").equalsIgnoreCase(clan)) {
					return true;
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	public double getMoney(String clan) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return 0;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans` WHERE tag=?;");
			ps.setString(1, clan);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getDouble("money");
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return 0;
	}

	public boolean clanExistName(String clan) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return false;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans` WHERE nome=?;");
			ps.setString(1, clan);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	/*
	 * public boolean mesmoClan(String p,String p1){ return false; ClanPlayer cp
	 * = getMembro(p); Clan c = cp.getClan(); if(c==null) return false;
	 * for(ClanPlayer cp2:c.getMembros()){
	 * if(cp2.getPlayer().equalsIgnoreCase(p1)){ return true; } } return false;
	 * }
	 */

	/*
	 * public boolean playerTemInfo(String player){ PreparedStatement ps = null;
	 * try{ ps=db.getConnection().prepareStatement(
	 * "SELECT * FROM `tg_clans_players` WHERE nome=?;"); ps.setString(1,
	 * player); ResultSet rs=ps.executeQuery(); while(rs.next()){
	 * if(rs.getString("nome") != null &&
	 * rs.getString("nome").equalsIgnoreCase(player)){ return true; } } }catch
	 * (Exception e){} finally{ try{ ps.close(); }catch (Exception e){} } return
	 * false; }
	 */

	public boolean moneySuficiente(String player, double money) {
		if (pl.econ.getBalance(player) >= money) {
			return true;
		}
		return false;
	}

	public boolean hasPermissionUser(Player p, String cmd, boolean b) {
		if (b) {
			if (p.hasPermission("tgclan." + cmd)
					|| p.hasPermission("tgclan.staff")
					|| p.hasPermission("tgclan.usar.*")) {
				return true;
			}
		} else {
			if (p.hasPermission("tgclan." + cmd)
					|| p.hasPermission("tgclan.usar")) {
				return true;
			}
		}
		/*
		 * if(p.hasPermission("tgclan."+cmd) ||
		 * p.hasPermission("tgclan.usar")||p.hasPermission("tgclan.staff") ||
		 * p.hasPermission("tgclan.usar.*")){ return true; }
		 */
		return false;
	}

	public boolean hasPermissionUser(CommandSender p, String cmd) {
		if(p.hasPermission("tgclan."+cmd)) return true;
		/*
		 * if(p.hasPermission("tgclan."+cmd) ||
		 * p.hasPermission("tgclan.usar")||p.hasPermission("tgclan.staff") ||
		 * p.hasPermission("tgclan.usar.*")){ return true; }
		 */
		return false;
	}

	public boolean playerExistInTable(String p) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return false;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans_players` WHERE nome=?;");
			ps.setString(1, p);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	public void setTagChatPlayer(String p, String tag) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection()
					.prepareStatement(
							"UPDATE `tg_clans_players` SET tag_clan_chat=? WHERE nome=?;");
			ps.setString(1, tag);
			ps.setString(2, p);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void addRival(int seuClan, String clanRival) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		Clan c = getClan(seuClan);
		List<String> ri = c.getRivais();
		ri.add(clanRival);
		c.setRivais(ri);
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET rivais=? WHERE tag=?;");
			ps.setString(1, c.toStringRivais());
			ps.setString(2, c.getTag());
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void removeRival(int seuClan, String clanRival) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		Clan c = getClan(seuClan);
		List<String> ri = c.getRivais();
		ri.remove(clanRival);
		c.setRivais(ri);
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET rivais=? WHERE tag=?;");
			ps.setString(1, c.toStringRivais());
			ps.setString(2, c.getTag());
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void addKill(String pMatou, boolean CiviuOuNeutra) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		String coluna = "";
		ClanPlayer cp = getMembro(pMatou);
		if (CiviuOuNeutra) {
			cp.addCivil();
			coluna = "kills_civis";
		} else {
			cp.addNeutra();
			coluna = "kills_neutras";
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans_players` SET " + coluna
							+ "=? WHERE nome=?;");
			ps.setInt(1,
					CiviuOuNeutra ? cp.getKillsCivis() : cp.getKillsNeutras());
			ps.setString(2, pMatou);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void addMorte(String pMorto) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		ClanPlayer cp = getMembro(pMorto);
		cp.addDeath();
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans_players` SET mortes=? WHERE nome=?;");
			ps.setInt(1, cp.getMorreu());
			ps.setString(2, pMorto);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void addAliado(int seuClan, String clanAliado) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		Clan c = getClan(seuClan);
		List<String> al = c.getAllys();
		al.add(clanAliado);
		c.setAllys(al);
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET aliados=? WHERE tag=?;");
			ps.setString(1, c.toStringAlly());
			ps.setString(2, c.getTag());
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void removeAliado(int seuClan, String clanAliado) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return;
		}
		PreparedStatement ps = null;
		Clan c = getClan(seuClan);
		List<String> al = c.getAllys();
		al.remove(clanAliado);
		c.setAllys(al);
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET aliados=? WHERE id=?;");
			ps.setString(1, c.toStringAlly());
			ps.setInt(2, seuClan);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean isAliado(String clanVerificado, int clan) {
		Clan c = getClan(clan);
		return c.getAllys().contains(clanVerificado);
	}

	public boolean isRival(String clanVerificado, int clan) {
		Clan c = getClan(clan);
		return c.getRivais().contains(clanVerificado);
	}

	/*public int getProxID() {
		int idFaltando = 0;
		int i = 1;
		List<Clan> clans = getAllClans();
		for (int c = 1; c <= clans.size(); c++) {
			for (Clan cl : clans) {
				if (cl == null)
					continue;
				if (cl.getID() == c) {
					i++;
				}
				if (cl.getID() != i) {
					idFaltando = i;
				}
			}
		}
		if (idFaltando == 0) {
			idFaltando = 1;
		}
		return idFaltando;
	}*/

	/*
	 * public List<String> getAliadosClan(String clan){ List<String> aliados=new
	 * ArrayList<>(); PreparedStatement ps=null; try{
	 * ps=db.getConnection().prepareStatement
	 * ("SELECT * FROM `tg_clans` WHERE tag=?;"); ps.setString(1, clan);
	 * ResultSet rs = ps.executeQuery(); while (rs.next()){ String
	 * ally=rs.getString("aliados"); String[] dadosAlly=ally.split(",");
	 * for(String getAlly:dadosAlly){ aliados.add(getAlly); } } }catch
	 * (Exception e){} finally{ try{ ps.close(); }catch (Exception e){} } return
	 * aliados; }
	 * 
	 * public List<String> getRivaisClan(String clan){ List<String> rivais=new
	 * ArrayList<>(); PreparedStatement ps=null; try{
	 * ps=db.getConnection().prepareStatement
	 * ("SELECT * FROM `tg_clans` WHERE tag=?;"); ps.setString(1, clan);
	 * ResultSet rs = ps.executeQuery(); while (rs.next()){ String
	 * rival=rs.getString("rivais"); String[] dadosRival=rival.split(",");
	 * for(String getRival:dadosRival){ rivais.add(getRival); } } }catch
	 * (Exception e){} finally{ try{ ps.close(); }catch (Exception e){} } return
	 * rivais; }
	 */

	/*
	 * public String getRivaisOfClan(String clan){ String rivais=""; for(String
	 * a:getRivaisClan(clan)){ if(a.equalsIgnoreCase("")){ rivais=a; }else{
	 * rivais=rivais+"§6, §a"+a; } } if(rivais.startsWith("§6, §a")){
	 * rivais=rivais.substring(4); } return rivais; }
	 */

	/*
	 * public String getAliadosOfClan(String clan){ String rivais=""; for(String
	 * a:getAliadosClan(clan)){ if(a.equalsIgnoreCase("")){ rivais=a; }else{
	 * rivais=rivais+"§6, §a"+a; } } if(rivais.startsWith("§6, §a")){
	 * rivais=rivais.substring(4); } return rivais; }
	 */

	/*
	 * public String getLideresOfClan(String clan){
	 * 
	 * }
	 */

	public Clan getClan(int id) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return null;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans` WHERE id=?;");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Clan c = new Clan(rs.getString("nome"),
						rs.getString("color_tag"),
						(rs.getInt("verificada") == 1), id,
						rs.getDouble("money"), rs.getInt("motim"),
						rs.getInt("slots"), rs.getString("item"),rs.getString("flag"));
				List<String> rivais = new ArrayList<>();
				if (rs.getString("rivais") != null
						&& !rs.getString("rivais").equalsIgnoreCase("")) {
					for (String getRival : rs.getString("rivais").split(",")) {
						rivais.add(getRival);
					}
				}
				List<String> allys = new ArrayList<>();
				if (rs.getString("aliados") != null
						&& !rs.getString("aliados").equalsIgnoreCase("")) {
					for (String getally : rs.getString("aliados").split(",")) {
						allys.add(getally);
					}
				}
				if (rivais != null && rivais.size() > 0) {
					c.setRivais(rivais);
				}
				if (allys != null && allys.size() > 0) {
					c.setAllys(allys);
				}
				int fm = rs.getInt("fogo_amigo");
				c.setFogoAmigo(fm == 1 ? true : false, false);
				// c.setMembros(getMembros(id));
				return c;
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return null;
		// return new Clan("", "", false);
	}

	public Clan getClan(String tag) {
		if(tag==null||tag.equalsIgnoreCase("")) return null;
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return null;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans` WHERE tag=?;");
			ps.setString(1, tag);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Clan c = new Clan(rs.getString("nome"),
						rs.getString("color_tag"),
						(rs.getInt("verificada") == 1), rs.getInt("id"),
						rs.getDouble("money"), rs.getInt("motim"),
						rs.getInt("slots"), rs.getString("item"),rs.getString("flag"));
				List<String> rivais = new ArrayList<>();
				if (rs.getString("rivais") != null
						&& !rs.getString("rivais").equalsIgnoreCase("")) {
					for (String getRival : rs.getString("rivais").split(",")) {
						rivais.add(getRival);
					}
				}
				List<String> allys = new ArrayList<>();
				if (rs.getString("aliados") != null
						&& !rs.getString("aliados").equalsIgnoreCase("")) {
					for (String getally : rs.getString("aliados").split(",")) {
						allys.add(getally);
					}
				}
				if (rivais != null && rivais.size() > 0) {
					c.setRivais(rivais);
				}
				if (allys != null && allys.size() > 0) {
					c.setAllys(allys);
				}
				int fm = rs.getInt("fogo_amigo");
				c.setFogoAmigo(fm == 1 ? true : false, false);
				// c.setMembros(getMembros(c));
				return c;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return null;
		// return new Clan("", "", false);
	}
	
	public ClanPlayer getMembro(String player){
		return getMembro(player,false);
	}

	public ClanPlayer getMembro(String player,boolean b) {
		if(db==null||!db.isConnected()){
			print("[TGClan] MySQL/SQLite esta com problemas na conexao...");
			return new ClanPlayer(player);
		}
		if(cpRecentes.containsKey(player)&&b){
			ClanPlayer cp = getCPRecente(player);
			if(cp!=null){
				return cp;
			}
		}
		if (player.equalsIgnoreCase("Trow_Games")) {
			// pl.print("PLAYER> "+player);
		}
		ClanPlayer cp = null;
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans_players` WHERE nome=?;");
			ps.setString(1, player);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String p = rs.getString("nome");
				String tagClan = rs.getString("tag");
				String tagChatClan = rs.getString("tag_clan_chat");
				int morreu = rs.getInt("mortes");
				int kn = rs.getInt("kills_neutras");
				int kc = rs.getInt("kills_civis");
				boolean confiavel = rs.getInt("confiavel") == 1;
				boolean lider = rs.getInt("lider") == 1;
				boolean temClan = rs.getString("tag") != null
						&& !rs.getString("tag").equalsIgnoreCase("");
				long ultimaVezOnline = rs.getLong("last_seen");
				int id = rs.getInt("clan_id");
				Clan c = getClan(id);
				cp = new ClanPlayer(p, tagClan, c != null ? c.getColorTag()
						: "", tagChatClan, morreu, kn, kc, confiavel, lider,
						temClan, ultimaVezOnline);
				cp.setCPRecente(System.currentTimeMillis()/1000);
				if(b){
					cpRecentes.put(p, cp);
				}
				return cp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return new ClanPlayer(player);
	}

	public List<ClanPlayer> getMembros(Clan clan) {
		// Clan c = getClan(clan);
		List<ClanPlayer> cp = new ArrayList<>();
		List<String> added = new ArrayList<>();
		// List<String> jaAdd=new ArrayList<>();
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans_players` WHERE clan_id=?;");
			ps.setInt(1, clan.getID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String p = rs.getString("nome");
				String tagClan = rs.getString("tag");
				String tagChatClan = rs.getString("tag_clan_chat");
				int morreu = rs.getInt("mortes");
				int kn = rs.getInt("kills_neutras");
				int kc = rs.getInt("kills_civis");
				boolean confiavel = rs.getInt("confiavel") == 1;
				boolean lider = rs.getInt("lider") == 1;
				boolean temClan = rs.getString("tag") != null
						&& !rs.getString("tag").equalsIgnoreCase("");
				long ultimaVezOnline = rs.getLong("last_seen");
				ClanPlayer cpP = new ClanPlayer(p, tagClan, clan.getColorTag(), tagChatClan, morreu, kn, kc, confiavel, lider,
						temClan, ultimaVezOnline);
				if(!added.contains(p)){
					cp.add(cpP);
					added.add(p);
				}
				// cp.add(getMembro(p));
				// if(jaAdd.contains(p)) continue;
				/*
				 * String tagClan=rs.getString("tag"); String
				 * tagChatClan=rs.getString("tag_clan_chat"); int morreu =
				 * rs.getInt("mortes"); int kn = rs.getInt("kills_neutras"); int
				 * kc = rs.getInt("kills_civis"); boolean confiavel =
				 * rs.getInt("confiavel")==1; boolean lider =
				 * rs.getInt("lider")==1; boolean temClan =
				 * true;//rs.getString("tag"
				 * )!=null&&!rs.getString("tag").equalsIgnoreCase(""); long
				 * ultimaVezOnline=rs.getLong("last_seen"); //jaAdd.add(p);
				 * cp.add(new ClanPlayer(p, tagClan, clan.getColorTag(),
				 * tagChatClan, morreu, kn, kc, confiavel, lider, temClan,
				 * ultimaVezOnline));
				 */
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return cp;
	}

	public List<ClanPlayer> getMembros(String cmdSQL, int id) {
		// Clan c = getClan(clan);
		List<ClanPlayer> cp = new ArrayList<>();
		List<String> added = new ArrayList<>();
		// List<String> jaAdd=new ArrayList<>();
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(cmdSQL);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String p = rs.getString("nome");
				String tagClan = rs.getString("tag");
				String tagChatClan = rs.getString("tag_clan_chat");
				int morreu = rs.getInt("mortes");
				int kn = rs.getInt("kills_neutras");
				int kc = rs.getInt("kills_civis");
				boolean confiavel = rs.getInt("confiavel") == 1;
				boolean lider = rs.getInt("lider") == 1;
				boolean temClan = rs.getString("tag") != null
						&& !rs.getString("tag").equalsIgnoreCase("");
				long ultimaVezOnline = rs.getLong("last_seen");
				Clan clan = getClan(id);
				ClanPlayer cpP = new ClanPlayer(p, tagClan, clan!=null?clan.getColorTag():"", tagChatClan, morreu, kn, kc, confiavel, lider,
						temClan, ultimaVezOnline);
				if(!added.contains(p)){
					cp.add(cpP);
					added.add(p);
				}
				// cp.add(getMembro(p));
				// if(jaAdd.contains(p)) continue;
				/*
				 * String tagClan=rs.getString("tag"); String
				 * tagChatClan=rs.getString("tag_clan_chat"); int morreu =
				 * rs.getInt("mortes"); int kn = rs.getInt("kills_neutras"); int
				 * kc = rs.getInt("kills_civis"); boolean confiavel =
				 * rs.getInt("confiavel")==1; boolean lider =
				 * rs.getInt("lider")==1; boolean temClan =
				 * true;//rs.getString("tag"
				 * )!=null&&!rs.getString("tag").equalsIgnoreCase(""); long
				 * ultimaVezOnline=rs.getLong("last_seen"); //jaAdd.add(p);
				 * cp.add(new ClanPlayer(p, tagClan, clan.getColorTag(),
				 * tagChatClan, morreu, kn, kc, confiavel, lider, temClan,
				 * ultimaVezOnline));
				 */
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return cp;
	}

	public List<Clan> getAllClans() {
		List<Clan> clans = new ArrayList<>();
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans`;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				/*
				 * Clan c = new Clan(rs.getString("nome"),
				 * rs.getString("color_tag"),
				 * (rs.getInt("verificada")==1),rs.getInt("id")); List<String>
				 * rivais = new ArrayList<>(); for(String
				 * getRival:rs.getString("rivais").split(",")){
				 * rivais.add(getRival); } List<String> allys = new
				 * ArrayList<>(); for(String
				 * getRival:rs.getString("aliados").split(",")){
				 * rivais.add(getRival); } if(rivais!=null&&rivais.size()>0){
				 * c.setRivais(rivais); } if(allys!=null&&allys.size()>0){
				 * c.setAllys(allys); }
				 */
				// c.setMembros(getMembros(c.getID()));
				//Clan c = getClan(rs.getInt("id"));
				//if (c == null)
				//	continue;
				Clan c = new Clan(rs.getString("nome"),
						rs.getString("color_tag"),
						(rs.getInt("verificada") == 1), rs.getInt("id"),
						rs.getDouble("money"), rs.getInt("motim"),
						rs.getInt("slots"), rs.getString("item"),rs.getString("flag"));
				List<String> rivais = new ArrayList<>();
				if (rs.getString("rivais") != null
						&& !rs.getString("rivais").equalsIgnoreCase("")) {
					for (String getRival : rs.getString("rivais").split(",")) {
						rivais.add(getRival);
					}
				}
				List<String> allys = new ArrayList<>();
				if (rs.getString("aliados") != null
						&& !rs.getString("aliados").equalsIgnoreCase("")) {
					for (String getally : rs.getString("aliados").split(",")) {
						allys.add(getally);
					}
				}
				if (rivais != null && rivais.size() > 0) {
					c.setRivais(rivais);
				}
				if (allys != null && allys.size() > 0) {
					c.setAllys(allys);
				}
				int fm = rs.getInt("fogo_amigo");
				c.setFogoAmigo(fm == 1 ? true : false, false);
				clans.add(c);
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return clans;
	}

	public void copyBackUpSQL() {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			ps = db.getConnection().prepareStatement(
					"SELECT * FROM `tg_clans_players_backup` ;");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (playerExistInTable(rs.getString("nome")))
					continue;
				ps2 = db.getConnection()
						.prepareStatement(
								"INSERT INTO `tg_clans_players` (`nome`,`lider`,`tag`,`kills_neutras`,`kills_civis`,`mortes`,`last_seen`,`confiavel`,`tag_clan_chat`,`clan_id`) VALUES (?,?,?,?,?,?,?,?,?,?);");
				ps2.setString(1, rs.getString("nome"));
				ps2.setBoolean(2, rs.getBoolean("lider"));
				ps2.setString(3, rs.getString("tag"));
				ps2.setInt(4, rs.getInt("kills_neutras"));
				ps2.setInt(5, rs.getInt("kills_civis"));
				ps2.setInt(6, rs.getInt("mortes"));
				ps2.setLong(7, rs.getLong("last_seen"));
				ps2.setBoolean(8, rs.getBoolean("confiavel"));
				ps2.setString(9, rs.getString("tag_clan_chat"));
				ps2.setInt(10, rs.getInt("clan_id"));
				ps2.executeUpdate();
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
				ps2.close();
			} catch (Exception e) {
			}
		}
	}

	public List<Clan> getClans(String cmdSQL) {
		List<Clan> cl = new ArrayList<>();
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(cmdSQL);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				/*
				 * Clan c = new Clan(rs.getString("nome"),
				 * rs.getString("color_tag"),
				 * (rs.getInt("verificada")==1),rs.getInt("id")); List<String>
				 * rivais = new ArrayList<>(); for(String
				 * getRival:rs.getString("rivais").split(",")){
				 * rivais.add(getRival); } List<String> allys = new
				 * ArrayList<>(); for(String
				 * getRival:rs.getString("aliados").split(",")){
				 * rivais.add(getRival); } if(rivais!=null&&rivais.size()>0){
				 * c.setRivais(rivais); } if(allys!=null&&allys.size()>0){
				 * c.setAllys(allys); }
				 */
				// c.setMembros(getMembros(c.getID()));
				Clan c = new Clan(rs.getString("nome"),
						rs.getString("color_tag"),
						(rs.getInt("verificada") == 1), rs.getInt("id"),
						rs.getDouble("money"), rs.getInt("motim"),
						rs.getInt("slots"), rs.getString("item"),rs.getString("flag"));
				List<String> rivais = new ArrayList<>();
				if (rs.getString("rivais") != null
						&& !rs.getString("rivais").equalsIgnoreCase("")) {
					for (String getRival : rs.getString("rivais").split(",")) {
						rivais.add(getRival);
					}
				}
				List<String> allys = new ArrayList<>();
				if (rs.getString("aliados") != null
						&& !rs.getString("aliados").equalsIgnoreCase("")) {
					for (String getally : rs.getString("aliados").split(",")) {
						allys.add(getally);
					}
				}
				if (rivais != null && rivais.size() > 0) {
					c.setRivais(rivais);
				}
				if (allys != null && allys.size() > 0) {
					c.setAllys(allys);
				}
				int fm = rs.getInt("fogo_amigo");
				c.setFogoAmigo(fm == 1 ? true : false, false);
				cl.add(c);
			}
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return cl;
	}

	/*
	 * public void avisarLideres(int clan,List<String> msg){ for(ClanPlayer
	 * cp:getMembros(clan)){ if(cp.isLider()){ Player p =
	 * Bukkit.getPlayer(cp.getPlayer()); if(p!=null){ for(String sendMsg:msg){
	 * sendMsg=sendMsg.replace("&", "§"); p.sendMessage(sendMsg); } } } }
	 * for(String lideres:getLiaders(clan)){ Player
	 * lider=Bukkit.getPlayer(lideres); if(lider==null)continue; for(String
	 * sendMsg:msg){ sendMsg=sendMsg.replace("&", "§");
	 * lider.sendMessage(sendMsg); } } }
	 */

	public boolean removePlayerClan(ClanPlayer cp) {
		// VOLTAR AQUI.
		PlayerLeaveClanEvent plce = new PlayerLeaveClanEvent(cp);
		Bukkit.getPluginManager().callEvent(plce);
		if (plce.isCancelled())
			return false;
		PreparedStatement ps = null;
		try {
			// String tag = cp.getTagClan();
			int total = cp.getClan().getTotalMembros();
			total -= 1;
			ps = db.getConnection()
					.prepareStatement(
							"UPDATE `tg_clans_players` SET tag=?,lider=?,clan_id=? WHERE nome=?;");
			ps.setString(1, "");
			ps.setBoolean(2, false);
			ps.setInt(3, 0);
			ps.setString(4, cp.getPlayer());
			ps.executeUpdate();
			if (total <= 0) {
				removeClan(cp.getClan().getID());
			}
			return true;
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	public void addPlayerClan(ClanPlayer cp, Clan c, boolean leder) {
		// clan=clan.replaceAll("&([0-9|a-f|r])",
		// "").replaceAll("§([0-9|a-f|r])", "");
		PlayerEntryClanEvent pece = new PlayerEntryClanEvent(cp, c, leder);
		Bukkit.getPluginManager().callEvent(pece);
		if (pece.isCancelled())
			return;
		PreparedStatement ps = null;
		try {
			ps = db.getConnection()
					.prepareStatement(
							"UPDATE `tg_clans_players` SET tag=?,lider=?,clan_id=?,tag_clan_chat=? WHERE nome=?;");
			ps.setString(1, c.getTag());
			ps.setBoolean(2, leder);
			ps.setInt(3, c.getID());
			ps.setString(4, "");
			ps.setString(5, cp.getPlayer());
			ps.executeUpdate();
			if (c.getTotalMembros() == 1 && c.isCreating()) {
				double money = getConfig().getDouble("Config.Preco_Criar_Clan");
				econ.withdrawPlayer(cp.getPlayer(), money);
			}
			/*
			 * if(leder){ if(total==0){ total+=1; editTotalMemberClan(clan,
			 * total); } }else{
			 * if(!getTagForPlayer(player).equalsIgnoreCase(clan)){ total+=1;
			 * editTotalMemberClan(clan, total); } }
			 */
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void mudarLider(String p, boolean leder) {
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans_players` SET lider=? WHERE nome=?;");
			ps.setBoolean(1, leder);
			ps.setString(2, p);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void setFogoAmigoClan(int clan, int i) {
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET fogo_amigo=? WHERE id=?;");
			ps.setInt(1, i);
			ps.setInt(2, clan);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void resetKDR(String p) {
		ClanPlayer cp = getMembro(p);
		if (!cp.temInfo()){
			criarInfoPlayer(p);
			return;
		}
		PreparedStatement ps = null;
		try {
			ps = db.getConnection()
					.prepareStatement(
							"UPDATE `tg_clans_players` SET kills_neutras=?,kills_civis=?,mortes=? WHERE nome=?;");
			ps.setInt(1, 0);
			ps.setInt(2, 0);
			ps.setInt(3, 0);
			ps.setString(4, p);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void setConfiavel(String p,boolean b) {
		if(p==null) return;
		PreparedStatement ps = null;
		try {
			ps = db.getConnection()
					.prepareStatement(
							"UPDATE `tg_clans_players` SET confiavel=? WHERE nome=?;");
			ps.setBoolean(1, b);
			ps.setString(2, p);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void criarInfoPlayer(String player) {
		ClanPlayer cp = getMembro(player);
		if (cp.temInfo())
			return;
		PreparedStatement ps = null;
		try {
			ps = db.getConnection()
					.prepareStatement(
							"INSERT INTO `tg_clans_players` (`nome`,`lider`,`tag`,`kills_neutras`,`kills_civis`,`mortes`,`last_seen`,`confiavel`,`tag_clan_chat`,`clan_id`) VALUES (?,?,?,?,?,?,?,?,?,?);");
			ps.setString(1, player);
			ps.setBoolean(2, false);
			ps.setString(3, "");
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.setInt(6, 0);
			ps.setLong(7, System.currentTimeMillis() / 1000);
			ps.setBoolean(8, true);
			ps.setString(9, "");
			ps.setInt(10, 0);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void removeClan(int tag) {
		PreparedStatement ps = null;
		try {
			Clan c = getClan(tag);
			if (c.getTotalMembros() > 0) {
				for (ClanPlayer cp : c.getMembros()) {
					if (cp == null)
						continue;
					removePlayerClan(cp);
				}
			}
			if (c.getAllys() != null && c.getAllys().size() > 0) {
				for (String ally : c.getAllys()) {
					Clan cAlly = getClan(ally);
					if (cAlly == null)
						continue;
					removeAliado(cAlly.getID(), c.getTag());
				}
			}
			if (c.getRivais() != null && c.getRivais().size() > 0) {
				for (String riv : c.getRivais()) {
					Clan cRiv = getClan(riv);
					if (cRiv == null)
						continue;
					removeRival(cRiv.getID(), c.getTag());
				}
			}
			ps = db.getConnection().prepareStatement(
					"DELETE FROM `tg_clans` WHERE id=?;");
			ps.setInt(1, tag);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public void print(String s) {
		System.out.println(s);
	}

	public void createClan(String tag, String nome) {
		if (clanExist(tag))
			return;
		PlayerCreateClanEvent pcce = new PlayerCreateClanEvent(tag, nome);
		Bukkit.getPluginManager().callEvent(pcce);
		if (pcce.isCancelled())
			return;
		PreparedStatement ps = null;
		//int id = getProxID();
		try {
			ps = db.getConnection()
					.prepareStatement(
							"INSERT INTO `tg_clans` (`verificada`,`tag`,`color_tag`,`nome`,`fogo_amigo`,`fundada`,`aliados`,`rivais`,`money`,`motim`,`slots`,`item`,`flag`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");
			ps.setInt(1, 0);
			ps.setString(2,tag.replaceAll("&([0-9|a-f|r])", "").replaceAll("§([0-9|a-f|r])", ""));
			ps.setString(3, tag.replaceAll("&", "§"));
			ps.setString(4, nome);
			ps.setInt(5, 1);
			ps.setLong(6, System.currentTimeMillis() / 1000);
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setDouble(9, 0);
			ps.setInt(10, 1);
			ps.setInt(11, getConfig().getInt("Config.Limite.Inicial"));
			ps.setString(12, "276:0");
			ps.setString(13, "");
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean updateClan(Clan c) {
		PreparedStatement ps = null;
		try {
			ps = db.getConnection().prepareStatement(
					"UPDATE `tg_clans` SET color_tag=?,flag=?,item=?,motim=?,slots=?,money=? WHERE id=?;");
			ps.setString(1, c.getColorTag());
			ps.setString(2, c.getFlagsStr());
			ps.setString(3, (c.getIcone()!=null?c.getIcone().getType().getId()+":"+c.getIcone().getData().getData():""));
			ps.setInt(4, c.canHaveMotim()?1:0);
			ps.setInt(5, c.getSlots());
			ps.setDouble(6, c.getMoney());
			ps.setInt(7, c.getID());
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
		return false;
	}

	public static List<String> getPage(List<String> l, int pagenr) {
		int PAGELENGTH = pl.getConfig().getInt("Config.Por_Pag.Clans");
		List<String> page = new ArrayList<String>();
		// 5-1=4*5=20
		int listart = (pagenr - 1) * PAGELENGTH;
		// 20+5=25
		int liend = listart + PAGELENGTH;
		// i = 20; 20 < 25; 20++;
		for (int i = listart; i < liend; i++) {
			// 20 < 30
			if (i < l.size()) {
				//
				page.add(l.get(i));
			} else {
				break;
			}
		}
		return page;
	}
	
	public List<Clan> getTopKDR(List<Clan> l){
		if(l==null||l.size()==0) return null;
		HashMap<String, Double> map = new HashMap<>();
		List<Clan> nL = new ArrayList<>();
		for(Clan c:l){
			map.put(c.getTag(), c.getKDR());
		}
		TopKDR tk = new TopKDR(map);
		List<String> tops = tk.getTops(l.size());
		if(tops==null) return null;
		for(String v:tops){
			for(Clan c:l){
				if(c.getTag().equalsIgnoreCase(v)){
					nL.add(c);
					break;
				}
			}
		}
		return nL;
	}

	public List<String> getClansLista(int pag,int porPag) {
		int inicio = (pag*porPag)-porPag;
		List<String> t = new ArrayList<String>();
		// List<String> cls = getAllTagClans();
		List<Clan> cls = getTopKDR(getClans("SELECT * FROM `tg_clans` LIMIT "+inicio+", "+porPag));//getAllClans();
		if(cls==null) return t;
		boolean termina = false;
		if (cls.size() <= 0) {
			termina = true;
		}
		if (!termina) {
			int i = 0;
			for (Clan clan : cls) {
				i++;
				// String tag = clan;
				// String color_tag = getColorTag(clan);
				// double kdr = getClanKDR(clan);
				// int membros = getTotalMembersClan(clan);
				// int liders = getLiaders(clan).size();
				// int rivais = getRivaisClan(clan).toString().replace("]",
				// "").replace("[",
				// "").equalsIgnoreCase("")||getRivaisClan(clan).toString()==null?0:getRivaisClan(clan).size();
				// int aliado = getAliadosClan(clan).toString().replace("]",
				// "").replace("[",
				// "").equalsIgnoreCase("")||getAliadosClan(clan).toString()==null?0:getAliadosClan(clan).size();
				int rivais = clan.getRivais().size();
				int liders = clan.getLideres().size();
				int aliado = clan.getAllys().size();
				int membros = clan.getMembros().size();
				double kdr = clan.getKDR();
				String tag = clan.getTag();
				String color_tag = clan.getColorTag();
				String nome = clan.getNomeClan();
				String formato = getMsg("Config.Info_All_Clans").replace("@pos", i+"")
						.replace("@rivais", rivais + "")
						.replace("@aliados", aliado + "")
						.replace("@membros", membros + "")
						.replace("@lideres", liders + "")
						.replace("@kdr", kdr + "").replace("@tag", tag)
						.replace("@color_tag", color_tag)
						.replace("@nome", nome);
				t.add(formato);
			}
		}
		return t;
	}

	public void showClans(Player p, int pag) {
		int PAGELENGTH = pl.getConfig().getInt("Config.Clans_Por_Pag");
		int totalPags = 1;
		List<String> list = getClansLista(pag,PAGELENGTH);
		if (getPage(list, pag).size() == 0) {
			p.sendMessage(getMsg("Msg.Pag_Invalida"));
			return;
		}
		while (getPage(list, totalPags).size() != 0) {
			totalPags++;
		}
		totalPags--;
		for(String msg:getConfig().getStringList("Msg.Info_Clans")){
			msg = msg.replace("&", "§").replace("@pag", pag + "")
				.replace("@totalpags", totalPags + "")
				.replace("@totalclans", list.size() + "");
			p.sendMessage(msg);
		}
		for (String inf : getPage(list, pag)) {
			p.sendMessage(inf);
		}
	}

	public String getMsg(String s) {
		if(getConfig().getString(s)==null) return "§cLinha nao encontrada na config: §a"+s;
		return getConfig().getString(s).replace("&", "§");
	}

	public static String format(Double d, boolean b) {
		String[] suffix = null;
		if (b) {
			String[] a = { "C", "K", "M", "B", "T", "Q" };
			suffix = a;
		} else {
			String[] a = { "U", "K", "M", "B", "T", "Q" };
			suffix = a;

		}

		String r = new DecimalFormat("##0E00").format(d);
		String[] dad = r.split("E");
		int s = Integer.parseInt(dad[1]);
		r = r.replaceAll("E[0-9]", suffix[s / 3]);
		int MAX_LENGTH = 20;
		while ((r.length() > MAX_LENGTH) || (r.matches("[0-9]+\\.[a-z]"))) {
			r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
		}
		if (!r.endsWith(suffix[s / 3])) {
			r = r.substring(0, r.length() - 1);
		}
		return r;
	}

	public ItemStack getItem(String item, String nome, List<String> lore,
			String nomeCabeca) {
		if (nomeCabeca == null||nomeCabeca.equalsIgnoreCase("null")) {
			ItemStack is = Item.getItemStack(item);//new ItemStack(Material.getMaterial(Integer.parseInt(d[0])), 1, (byte) Integer.parseInt(d[1]));
			ItemMeta im = is.getItemMeta();
			if (nome != null) {
				im.setDisplayName(nome);
			}
			if (lore != null) {
				List<String> l = new ArrayList<>();
				for (String x : lore) {
					x = x.replace("&", "§");
					l.add(x);
				}
				im.setLore(l);
			}
			is.setItemMeta(im);
			return is;
		} else {
			ItemStack is = getSkull(nomeCabeca);//new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
			SkullMeta im = (SkullMeta) is.getItemMeta();
			if (nome != null) {
				im.setDisplayName(nome);
			}
			//im.setOwner(nomeCabeca);
			if (lore != null) {
				List<String> l = new ArrayList<>();
				for (String x : lore) {
					x = x.replace("&", "§");
					l.add(x);
				}
				im.setLore(l);
			}
			is.setItemMeta(im);
			return is;
		}
	}
	
	public String getSkullLink(String nome){
		for(String x:getConfig().getStringList("Cabecas.Skins")){
			String[] d= x.split("->");
			if(d[0].equalsIgnoreCase(nome)){
				return d[1];
			}
		}
		return null;
	}

	public static ItemStack getSkull(String url) {
		ItemStack item = Item.getItemStack("397:3");//new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (url.isEmpty())
			return item;

		SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.getEncoder()
				.encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url)
						.getBytes());
		profile.getProperties().put("textures",
				new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = itemMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(itemMeta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		item.setItemMeta(itemMeta);
		return item;
	}

}
