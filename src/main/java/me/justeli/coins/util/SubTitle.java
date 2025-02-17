package me.justeli.coins.util;

import io.papermc.lib.PaperLib;
import me.justeli.coins.config.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class SubTitle
{
    private final String subtitle;
    private int fadeIn = 10;
    private int stay = 100;
    private int fadeOut = 20;

    private SubTitle (String subtitle)
    {
        this.subtitle = Util.color(subtitle);
    }

    public static SubTitle of (String text)
    {
        return new SubTitle(text.replace("{$}", Config.CURRENCY_SYMBOL));
    }

    public SubTitle in (int ticks)
    {
        this.fadeIn = ticks;
        return this;
    }

    public SubTitle out (int ticks)
    {
        this.fadeOut = ticks;
        return this;
    }

    public SubTitle stay (int ticks)
    {
        this.stay = ticks;
        return this;
    }

    public void send (Player player)
    {
        if (PaperLib.getMinecraftVersion() >= 11)
        {
            player.sendTitle(ChatColor.RESET.toString(), subtitle, fadeIn, stay, fadeOut);
        }
        else
        {
            try
            {
                Object e;
                Object chatTitle;
                Object chatSubtitle;
                Constructor<?> subtitleConstructor;
                Object titlePacket;
                Object subtitlePacket;

                String title = "";

                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class})
                        .invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle")
                        .getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                sendPacket(player, titlePacket);

                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class})
                        .invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle")
                        .getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);

            }
            catch (Exception exception)
            {
                player.sendMessage(subtitle);
            }
        }
    }

    private void sendPacket (Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception ignored) { }
    }

    private Class<?> getNMSClass (String name)
    {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try
        {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException ignored)
        {
            return null;
        }
    }
}