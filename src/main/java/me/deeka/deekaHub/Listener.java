package me.deeka.deekaHub;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import java.io.File;
import java.io.IOException;

import static me.deeka.deekaHub.MidiUtil.playMidi;
import static me.deeka.deekaHub.ServerSelectorGUI.hex;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String p = event.getPlayer().getName();
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.YELLOW + p + " has joined the lobby!");
        player.sendTitle(hex(""), hex("&fWelcome back to &d&lDeeka Network!"));
        try {
            MidiUtil.playMidi(new File("/home/mindgoesbye/minecraft_server/hub-dev/plugins/DeekaHub/song.midi"), 1.2f, "song-midi", player);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }

        // compass item
        ItemStack item = new ItemStack(Material.RECOVERY_COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(hex("&fServer Selector"));
        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        // giving to player
        player.getInventory().setItem(4, item);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.RECOVERY_COMPASS && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                if (meta.getDisplayName().equals(hex("&fServer Selector"))) {
                    new ServerSelectorGUI(event.getPlayer()).open();
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!player.isOp()) {
            if (event.getSlot() == 4 || event.getHotbarButton() == 4) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String p = event.getPlayer().getName();
        event.setQuitMessage(ChatColor.YELLOW + p + " has left the lobby!");
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                player.teleport(player.getWorld().getSpawnLocation());
            } else {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        if (!p.isOp()) {
            event.setFormat(ChatColor.GRAY + p.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + event.getMessage());
        } else {
            event.setFormat(ChatColor.RED + p.getDisplayName() + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + event.getMessage());
        }
    }
}
