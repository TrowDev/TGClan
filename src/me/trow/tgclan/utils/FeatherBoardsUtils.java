package me.trow.tgclan.utils;

import me.trow.tgclan.Main;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.command.ConsoleCommandSender;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class FeatherBoardsUtils {

	private static boolean using = false;

	public static void hooks() {
		hook();
	}

	public static void hook() {
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
			b.sendMessage("§3[FeatherBoard] §bEncontrado! Hook habilitado.");
			using = true;
		} else {
			b.sendMessage("§4[FeatherBoard] §cNao Encontrado! Hook desabilitado.");
		}
	}

	public void board() {
		if (using) {
			new BukkitRunnable() {
				public void run() {
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:mortes",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									return cp.getMorreu()+"";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:color_tag",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									if(cp.temClan()){
										return cp.getColorTag();
									}
									return "";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:kdr",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									return cp.getKDR()+"";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:kdr_clan",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									if(cp.temClan()){
										return cp.getClan().getKDR()+"";
									}
									return "0";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:kt",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									return (cp.getKillsNeutras()+cp.getKillsCivis())+"";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:kc",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									if(cp.getKillsCivis()!=0){
										return cp.getKillsCivis()+"";
									}
									return "0";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:kn",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									if(cp.getKillsNeutras()!=0){
										return cp.getKillsNeutras()+"";
									}
									return "0";
								}
							});
					PlaceholderAPI.registerPlaceholder(
							Main.pl, "tgclan:clan_tag",
							new PlaceholderReplacer() {
								public String onPlaceholderReplace(
										PlaceholderReplaceEvent e) {
									ClanPlayer cp = Main.pl.getMembro(e.getPlayer().getName());
									if(cp.temClan()){
										return cp.getTagClan();
									}
									return "";
								}
							});
						cancel();
				}
			}.runTaskTimer(Main.pl, 5L, 5L);
		}
	}

}
