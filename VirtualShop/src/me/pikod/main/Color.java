package me.pikod.main;

import org.bukkit.ChatColor;

public class Color {
	public static String chat(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
