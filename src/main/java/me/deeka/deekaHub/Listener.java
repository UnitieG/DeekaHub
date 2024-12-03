package me.deeka.deekaHub;

import org.bukkit.Bukkit;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import static me.deeka.deekaHub.ServerSelectorGUI.hex;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String p = event.getPlayer().getName();
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.YELLOW + p + " has joined the lobby!");
        player.sendTitle(hex(""), hex("&fWelcome back to &d&lDeeka Network!"));
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(DeekaHub.getInstance(), new Runnable() {
            public void run() {
                player.setPlayerListHeader(hex("\n&d&lDeeka Network\n&fYou are currently in &e&nhub&f!\n\n&fPing: &b" + player.getPing() + "\n"));
                player.setPlayerListName(hex("&7" + player.getName()));
                player.setPlayerListFooter(hex("\n&b&n/discord\n\n&fPowered by &ea few cats &d&n:3\n\n&7play.deeka.me\n"));
            }
        }, 0, 20L);
        try {
            // check if dir exist
            if (!DeekaHub.getInstance().pluginsFolder.exists()) {
                DeekaHub.getInstance().pluginsFolder.mkdir();
            }
            // check if midi does exist or not
            File midiFile = new File(DeekaHub.getInstance().pluginsFolder + "song.mid");
            if (midiFile.exists()) {
                // dont use absolute path, absolute path for dev environment :0-----> /home/mindgoesbye/minecraft_server/hub-dev/plugins/DeekaHub/song.midi
                MidiUtil.playMidi(new File(DeekaHub.getInstance().pluginsFolder + "song.mid"), 1.2f, "song-midi", player);
            } else if (!midiFile.exists()) {
                Bukkit.getServer().getLogger().info("Song.midi does not exist, trying to download it from a private nat");
                try {
                    // donwloadm idi
                    URL midiURL = new URL("http://internal-deeka-project-directory-listing.deeka.me/song.mid");
                    ReadableByteChannel rbc = Channels.newChannel(midiURL.openStream());
                    FileOutputStream fos = new FileOutputStream(DeekaHub.getInstance().pluginsFolder + "song.mid");
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    MidiUtil.playMidi(new File(DeekaHub.getInstance().pluginsFolder + "song.mid"), 1.2f, "song-midi", player);
                } catch (IOException e) {
                    Bukkit.getServer().getLogger().severe("Failed to download the midi. Stack Trace Below for Debugging");
                    e.printStackTrace();
                }

            }
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }

        // compass item
        ItemStack item = new ItemStack(Material.RECOVERY_COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(hex("&7Server Selector"));
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
                if (meta.getDisplayName().equals(hex("&7Server Selector"))) {
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
