package me.deeka.deekaHub;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static me.deeka.deekaHub.ServerSelectorGUI.hex;

public class DeleteMidi implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 1) {
                    File midiFile = new File(DeekaHub.getInstance().pluginsFolder + "song.mid");
                    player.sendMessage(hex("&aReplacing the MID File with: &e&n" + args[0]));
                    if (midiFile.exists()) {
                        player.sendMessage(hex("&aMID Already Exist... &c&l&nDELETING..."));
                        player.sendMessage(hex("&aDeleted Path: &e" + midiFile.getAbsolutePath()));
                        midiFile.delete();
                        player.sendMessage(hex("&cDeleted Successfully!"));
                    }
                    try {
                        // donwloadm idi
                        player.sendMessage(hex("&aDownloading..."));
                        URL midiURL = new URL(args[0]);
                        ReadableByteChannel rbc = Channels.newChannel(midiURL.openStream());
                        FileOutputStream fos = new FileOutputStream(DeekaHub.getInstance().pluginsFolder + "song.mid");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        player.sendMessage(hex("&eDownload successfully! Rejoin to see the effects."));
                    } catch (IOException e) {
                        player.sendMessage(hex("&cFailed to download the MID File."));
                        e.printStackTrace();
                    }
                } else if (args.length != 1) {
                    player.sendMessage(hex("&cPlease provide URI."));
                }
            } else if (!player.isOp()) {
                player.sendMessage(hex("&cYou do not have permission to use this command!"));
            }
        }
        return false;
    }
}
