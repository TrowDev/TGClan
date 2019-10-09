package me.trow.tgclan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.idlibrary.main.IDLibrary;
import me.trow.tgclan.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;

public class Item {
	
	public static String version = "0.0";
	
	public static boolean acimaBukkit12(){
		return versionCompare(version, "1.12")>=0;
	}
	
	public static boolean defineVersion(){
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		Server server = Bukkit.getServer();
		Pattern pattern = Pattern.compile("(^[^\\-]*)");
		Matcher matcher = pattern.matcher(server.getBukkitVersion());
		if (!matcher.find()) {
			b.sendMessage("§4["+Main.pl.getDescription().getName()+"] §cCould not find Bukkit version... Disabling plugin");
			Bukkit.getPluginManager().disablePlugin(Main.pl);
			return false;
		}
		version = matcher.group(1);
		b.sendMessage("§3["+Main.pl.getDescription().getName()+"] §bWe found Bukkit version! Version: "+version);

		if(acimaBukkit12()){
			if(Bukkit.getPluginManager().getPlugin("ID-Library")==null){
				b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				b.sendMessage("§3[TGClan] §cDesativado...");
				b.sendMessage("§3Criador: §3Trow");
				b.sendMessage("§cMotivo: ID-Library nao encontrado. Download: https://www.spigotmc.org/resources/id-library-old-id-support-lib-new-minecraft-ids-1-13-x-1-14-x.62124/");
				b.sendMessage("§6=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				Bukkit.getPluginManager().disablePlugin(Main.pl);
				return true;
			}
		}
		return true;
	}

	public static ItemStack getItemStack(String mat){
		if(versionCompare(version, "1.12")<0){
			try{
				String[] d = mat.split(":");
				ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(d[0])),1,(byte)Integer.parseInt(d[1]));
				return is;
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}else{
			//Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cEssa versao do plugin nao e compativel com a versao do jogo.");
			try{
				return new ItemStack(IDLibrary.getMaterial(mat));
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}
	}
	
	public static ItemStack getItemStack(String mat,int amt){
		if(versionCompare(version, "1.12")<0){
			try{
				String[] d = mat.split(":");
				ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(d[0])),amt,(byte)Integer.parseInt(d[1]));
				return is;
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}else{
			try{
				return new ItemStack(IDLibrary.getMaterial(mat),amt);
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}
	}
	
	public static ItemStack getItemStack(String mat,int amt,byte b){
		if(versionCompare(version, "1.12")<0){
			try{
				String[] d = mat.split(":");
				ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(d[0])),1,b);
				return is;
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}else{
			try{
				return new ItemStack(IDLibrary.getMaterial(mat),amt,b);
			}catch (Exception e){
				Bukkit.getConsoleSender().sendMessage("§4["+Main.pl.getDescription().getName()+"] §cO material §a"+mat+"§c nao esta configurado corretamente.");
				return null;
			}
		}
	}

	// System.out.println(versionCompare("1.6", "1.8")); // -1 as 1.8 is newer
	// System.out.println(versionCompare("1.7", "1.8")); // -1 as 1.8 is newer
	// System.out.println(versionCompare("1.8", "1.8")); // 0 as same
	// System.out.println(versionCompare("1.9", "1.8")); // 1 as 1.9 is newer
	public static int versionCompare(String str1, String str2) {
		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		int i = 0;
		while (i < vals1.length && i < vals2.length
				&& vals1[i].equals(vals2[i])) {
			i++;
		}
		if (i < vals1.length && i < vals2.length) {
			int diff = Integer.valueOf(vals1[i]).compareTo(
					Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		return Integer.signum(vals1.length - vals2.length);
	}
	
}
