package mr.minecraft15.FreeSigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static String prefix = "§7[§4FreeSigns§8] §7";
    private static String signprefix;
    private static String signcreateprefix;

    private static int seconds;

    private static boolean delay;
    private static boolean sound;

    @Override
    public void onEnable() {
	FileConfiguration cfg = getConfig();
	cfg.options().copyDefaults(true);
	cfg.addDefault("Sign.prefix", "&4SkyPvP");
	cfg.addDefault("Sign.createprefix", "[skypvp]");
	cfg.addDefault("Sign.playsound", false);
	cfg.addDefault("Delay.use", false);
	cfg.addDefault("Delay.seconds", 60);
	saveConfig();
	signprefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("Sign.prefix"));
	signcreateprefix = cfg.getString("Sign.createprefix");
	sound = cfg.getBoolean("Sign.playsound");
	delay = cfg.getBoolean("Delay.use");
	seconds = cfg.getInt("Delay.seconds");
	Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onChangeSign(SignChangeEvent e) {
	Player p = e.getPlayer();
	if (e.getLine(0).equalsIgnoreCase(signcreateprefix) && p.hasPermission("freesigns.create")) {
	    e.setLine(0, signprefix);
	    e.setLine(3, ChatColor.translateAlternateColorCodes('&', e.getLine(3)));
	    p.sendMessage(prefix + "FreeSign successfully created.");
	}
    }

    @EventHandler
    private void onInteractWithSign(PlayerInteractEvent e) {
	final Player p = e.getPlayer();
	if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    BlockState bs = e.getClickedBlock().getState();
	    if (bs instanceof Sign) {
		Sign sign = (Sign) bs;
		String line0 = sign.getLine(0);
		String line1 = sign.getLine(1);
		String line2 = sign.getLine(2);
		String line3 = sign.getLine(3);
		if (line0.equals(signprefix)) {
		    if (delay) {
			String key = p.getName() + ":" + line1;
			if (Delays.haveToWait(key, seconds)) {
			    p.sendMessage(
				    Main.prefix + "You have to wait " + Delays.getRemainingTime(key, seconds) + ".");
			    return;
			}
			Delays.registerDelay(key);
		    }
		    try {
			line1 = line1.replace("§0", "");
			int id = 0;
			int subid = 0;
			if (line1.contains(":")) {
			    String[] parts = line1.split(":");
			    id = Integer.parseInt(parts[0]);
			    subid = Integer.parseInt(parts[1]);
			} else {
			    id = Integer.parseInt(line1);
			}
			int amount = Integer.parseInt(line2.replace("§0", ""));
			if (sound) {
			    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			}
			Inventory i = Bukkit.createInventory(p, 9, line3);
			i.setItem(4, new ItemStack(Material.getMaterial(id), amount, (short) subid));
			p.openInventory(i);
		    } catch (NumberFormatException ex) {
			p.sendMessage(prefix + "Invalid FreeSign.");
		    }
		}
	    }
	}
    }
}
