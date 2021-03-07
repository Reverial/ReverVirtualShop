package me.pikod.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.pikod.commands.cmdMain;
import me.pikod.commands.cmdMarket;
import me.pikod.commands.cmdSell;
import me.pikod.listener.ActionHandler;
import me.pikod.main.data.DataSaver;
import me.pikod.utils.Color;
import me.pikod.utils.f;
import net.milkbowl.vault.economy.Economy;

public class VirtualShop extends JavaPlugin {

	
	public static Logger log;
	public static VirtualShop plugin;
	public static PManager pmanager;
	public static YamlConfiguration shops;
	public static YamlConfiguration lang;
	public static YamlConfiguration config;
	public static Economy vault;
	public static UpdateChecker uc;
	public static boolean debugMode = false;
	public static boolean isUpperVersion;
	public static String country = System.getProperty("user.country");
	
	
	public void vaultGet() {
		RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
		if(eco != null) {
			VirtualShop.vault = eco.getProvider();
		}else {
			if(country.equals("TR")) {
				log.warning("Vault bulunamad, eklenti devred braklyor.");
			}else {
				log.warning("Failed get Vault! Disabling plugin.");
			}
			Bukkit.getPluginManager().disablePlugin(this);
		}
		log.info("Detected Country: "+country);
	}
	
	@Override
	public void onEnable() {
		if(country == null) {
			country = "en";
		}
		uc = new UpdateChecker(this);
		pmanager = new PManager(this);
		plugin = this;
		log = getLogger();
	
		shops = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml"));
		config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));

		log.info("Configuration files ready.");
		
		new ActionHandler(this);
		new cmdMain(this);
		new cmdMarket(this);
		new cmdSell(this);
		
		if(!getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
			log.warning(Color.chat("Vault are not installed! Disabling plugin!"));
			Bukkit.getPluginManager().disablePlugin(this);
		}
		vaultGet();
		try {
			if(Material.matchMaterial("BLACK_STAINED_GLASS_PANE") == null) {
				isUpperVersion = false;
			}else isUpperVersion = true;
		}catch(Exception e) {
			isUpperVersion = false;
		}
		
		Seller.loadClass();
		DataSaver.LoadData();
		log.info("Loaded all data.");

		SendSite.runSender();
		if(uc.hasUpdate) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				
				@Override
				public void run() {
						getLogger().warning("Founded new version! Download: https://www.spigotmc.org/resources/74496/");
						for(Player player : Bukkit.getOnlinePlayers()) {
							if(player.isOp())
								player.sendMessage(f.autoLang("newUpdates"));
						}
					
				}
			}, 30L);
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static void saveShops() {
		try {
			shops.save(pmanager.shopConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reloadShops() {
		log.info("Plugin reloading.");
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
			log.info("Plugin's folder has created.");
		}
		if(!pmanager.shopConfig.exists()) {
			pmanager.copy(plugin.getResource("shops.yml"), pmanager.shopConfig);
			log.info("Created new \"shops.yml\" file.");
		}
		if(!pmanager.langConfig.exists()) {
			if(country.equals("TR")) {
				pmanager.copy(plugin.getResource("lang_tr.yml"), pmanager.langConfig);
			}else {
				pmanager.copy(plugin.getResource("lang.yml"), pmanager.langConfig);
			}
			log.info("Created new \"lang.yml\" file.");
		}
		if(!pmanager.config.exists()) {
			pmanager.copy(plugin.getResource("config.yml"), pmanager.config);
			log.info("Created new \"config.yml\" file.");
		}
		config = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "config.yml"));
		shops = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "shops.yml"));
		lang = YamlConfiguration.loadConfiguration(new File(VirtualShop.plugin.getDataFolder(), "lang.yml"));
		
		Seller.loadClass();
		DataSaver.LoadData();
		log.info("Plugin has been successfully reloaded.");
	}
	public static String strSade(String str) {
		char trueChars[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's' , 't', 'y', 'u', 'v', 'y', 'z', 'x' , 'q'};
		String ret = "";
		
		for(char key : str.toCharArray()){
			boolean isCorrect = false;
			for(char trueKey : trueChars) {
				if(key == trueKey) {
					isCorrect = true;
				}
			}
			if(isCorrect == false) {
				if(key == ' ') {
					ret = ret+"i";
				}else if(key == ' ') {
					ret = ret+"i";
				}else if(key == ' ') {
					ret = ret+"o";
				}else if(key == ' ') {
					ret = ret+"";
				}else if(key == ' ') {
					ret = ret+"s";
				}
			}else {
				ret = ret+key;
			}
			
			
		}
		return ret;
	}
	
	public static boolean isSade(String str) {
		char trueChars[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'r', 's' , 't', 'y', 'u', 'v', 'y', 'z', 'x' , 'q'};
		boolean isSade = true;
		
		for(char key : str.toCharArray()){
			boolean isCorrect = false;
			for(char trueKey : trueChars) {
				if(key == trueKey) {
					isCorrect = true;
				}
			}
			if(isCorrect == false) {
				isSade = false;
			}
			
		}
		return isSade;
	}
	
	public static String numberToStr(double sayi) {
		String strNumber = String.valueOf(sayi);
		String ret = ""+(long) sayi;
		if(sayi > 999) {
			ret = ret.substring(0, ret.length()-3) + "," + ret.substring(ret.length()-3);
		}
		if(sayi > 999_999) {
			ret = ret.substring(0, ret.length()-7) + "," + ret.substring(ret.length()-7);
		}
		if(sayi > 999_999_999) {
			ret = ret.substring(0, ret.length()-11) + "," + ret.substring(ret.length()-11);
		}
		if(sayi > 999_999_999_999l) {
			ret = ret.substring(0, ret.length()-16) + "," + ret.substring(ret.length()-16);
		}
		if(sayi > 999_999_999_999_999l) {
			ret = ret.substring(0, ret.length()-23) + "," + ret.substring(ret.length()-23);
		}
		if(sayi > 999_999_999_999_999_999l) {
			ret = ret.substring(0, ret.length()-31) + "," + ret.substring(ret.length()-31);
		}
		String kalan = strNumber.substring(strNumber.indexOf('.'));
		if(!kalan.equals(".0")) {
			ret += kalan;
		}
		return ret;
	}
	
	public static void setStainedGlassItem(ItemStack item, String color, int id) {
		if(isUpperVersion) {
			Material m = Material.matchMaterial(color+"_STAINED_GLASS_PANE");
			item.setType(m);
		}else {
			item.setDurability((short) id);
		}
	}
	
	public static ItemStack getStainedGlassItem(String color, int id) {
		ItemStack item;
		if(isUpperVersion) {
			if(debugMode) {
				f.bm("IS UPPER VERSION: TRUE\nCOLOR: "+color);
			}
			
			item = new ItemStack(Material.matchMaterial(color+"_STAINED_GLASS_PANE"));
		}else {
			if(debugMode) {
				f.bm("IS UPPER VERSION: FALSE\nCOLOR: "+color);
			}
			
			item = new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"));
			item.setDurability((short) id);
		}
		return item;
	}
}
