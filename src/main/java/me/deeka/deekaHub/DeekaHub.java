package me.deeka.deekaHub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

public final class DeekaHub extends JavaPlugin {

    private static DeekaHub instance;

    File pluginsFolder = new File(getDataFolder(), "");
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        new InventoryAPI(this).init(); // idk load ingenveotyrypaifvbegrr
        getServer().getPluginManager().registerEvents(new Listener(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord"); // i hate bungeecord but it's required
    }
    public static DeekaHub getInstance() {
        return instance;
    }

    public static void sendPlayerToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(DeekaHub.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED+"Error when trying to connect to "+server);
        }
    }

    public static int getPlayerCount(Player player, String server) {
        try {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(server);
            player.sendPluginMessage(getInstance(), "BungeeCord", out.toByteArray());
        } catch (Exception e) {
            Bukkit.getLogger().info(ChatColor.RED + "Error when trying to fetch players from " + server);
            return 0;
        }
        return 0;
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}