package me.deeka.deekaHub;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerSelectorGUI extends Gui {


    public ServerSelectorGUI(Player player) {
        super(player, "server-gui", "Server Selector", 3);
        //player, id, title, row
    }
    @Override
    public void onOpen(InventoryOpenEvent event) {
        // barrier
        Icon barrier = new Icon(Material.GRAY_STAINED_GLASS_PANE);
        barrier.setName("§r");
        barrier.setAmount(1);
        fillGui(barrier);

        // main server
        Icon SMP = new Icon(Material.GRASS_BLOCK).setName(hex("&#9EDF9CClassic SMP &7- &aSurvival Server!"));
        SMP.enchant(Enchantment.FORTUNE);
        SMP.hideFlags();
        ArrayList<String> SMPLore = new ArrayList<String>();
        SMPLore.add(hex("&8&lꜱᴜʀᴠɪᴠᴀʟ"));
        SMPLore.add(hex(""));
        SMPLore.add(hex(" &#9EDF9C&┌ &lServer Information"));
        SMPLore.add(hex(" &#9EDF9C│ &fServer Version: &a1.21.3"));
        SMPLore.add(hex(" &#9EDF9C│ &fBedrock Support: &aExcellent!"));
        SMPLore.add(hex(" &#9EDF9C│ &fSupported Version: &a1.21-1.21.3"));
        SMPLore.add(hex(""));
        SMPLore.add(hex(" &e\uD83D\uDD25 &fOnline Players: &a" + DeekaHub.getPlayerCount(player, "SMP")));
        SMPLore.add(hex(""));
        SMPLore.add(hex(" &7➜ &fLeft Click to &a&nConnect&f!"));
        SMPLore.add(hex(""));
        SMP.setLore(SMPLore);
        addItem(11, SMP);
        SMP.onClick(e -> {
            player.sendMessage(hex("&fSending you to &#9EDF9C&nSMP&f..."));
            DeekaHub.sendPlayerToServer(player, "SMP");
        });

        Icon KBFFA = new Icon(Material.STICK).setName(hex("&#FFBD73Knockback FFA &7- &eFFA PVP!"));
        KBFFA.enchant(Enchantment.FORTUNE);
        KBFFA.hideFlags();
        ArrayList<String> KBFFALore = new ArrayList<String>();
        KBFFALore.add(hex("&8&lᴘᴠᴘ"));
        KBFFALore.add(hex(""));
        KBFFALore.add(hex(" &#FFBD73&┌ &lServer Information"));
        KBFFALore.add(hex(" &#FFBD73│ &fServer Version: &e1.21.1"));
        KBFFALore.add(hex(" &#FFBD73│ &fBedrock Support: &eDecent!"));
        KBFFALore.add(hex(" &#FFBD73│ &fSupported Version: &e1.8-1.21.3"));
        KBFFALore.add(hex(""));
        KBFFALore.add(hex(" &e\uD83D\uDD25 &fOnline Players: &e" + DeekaHub.getPlayerCount(player, "server-knockback-ffa")));
        KBFFALore.add(hex(""));
        KBFFALore.add(hex(" &7➜ &fLeft Click to &e&nConnect&f!"));
        KBFFALore.add(hex(""));
        KBFFA.setLore(KBFFALore);
        addItem(15, KBFFA);
        KBFFA.onClick(e -> {
            player.sendMessage(hex("&fSending you to &e&nKnockback FFA&f..."));
            DeekaHub.sendPlayerToServer(player, "server-knockback-ffa");
        });
    }
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }
}
