package me.justeli.coins.hook;

import io.papermc.lib.PaperLib;
import me.justeli.coins.Coins;
import me.justeli.coins.config.Config;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * Created by Eli on July 09, 2021.
 * Coins: me.justeli.coins.hook
 */
public class bStatsMetrics
{
    public static void metrics (JavaPlugin plugin, final Consumer<Metric> consumer)
    {
        Metrics metrics = new Metrics(plugin, 831);
        consumer.accept(new Metric(metrics));
    }

    public static class Metric
    {
        private final Metrics metrics;

        public Metric (Metrics metrics)
        {
            this.metrics = metrics;
        }

        public void add (String key, Object value)
        {
            if (value == null || value.toString() == null)
                return;

            metrics.addCustomChart(new SimplePie(key, value::toString));
        }
    }

    public static void register ()
    {
        metrics(Coins.plugin(), metrics ->
        {
            metrics.add("language", Config.LANGUAGE.toLowerCase(Locale.ROOT));
            metrics.add("currencySymbol", Config.CURRENCY_SYMBOL);
            metrics.add("nameOfCoin", Config.NAME_OF_COIN);
            metrics.add("multiSuffix", Config.MULTI_SUFFIX);
            metrics.add("coinItem", Config.COIN_ITEM);
            metrics.add("enchantedCoin", Config.ENCHANTED_COIN);
            metrics.add("pickupMessage", Config.PICKUP_MESSAGE);
            metrics.add("dropEachCoin", Config.DROP_EACH_COIN);
            metrics.add("dropWithAnyDeath", Config.DROP_WITH_ANY_DEATH);
            metrics.add("moneyAmount", (Config.MONEY_AMOUNT_FROM + Config.MONEY_AMOUNT_TO) / 2);
            metrics.add("moneyDecimals", Config.MONEY_DECIMALS);
            metrics.add("stackCoins", Config.STACK_COINS);
            metrics.add("percentagePlayerHit", Config.PERCENTAGE_PLAYER_HIT * 100 + "%");
            metrics.add("disableHoppers", Config.DISABLE_HOPPERS);
            metrics.add("playerDrop", Config.PLAYER_DROP);
            metrics.add("preventAlts", Config.PREVENT_ALTS);
            metrics.add("spawnerDrop", Config.SPAWNER_DROP);
            metrics.add("passiveDrop", Config.PASSIVE_DROP);
            metrics.add("preventSplits", Config.PREVENT_SPLITS);
            metrics.add("soundEnabled", Config.PICKUP_SOUND);
            metrics.add("pickupSound", Config.SOUND_NAME);
            metrics.add("soundPitch", Config.SOUND_PITCH);
            metrics.add("soundVolume", Config.SOUND_VOLUME);
            metrics.add("dropChance", Config.DROP_CHANCE * 100 + "%");
            metrics.add("limitForLocation", Config.LIMIT_FOR_LOCATION);
            metrics.add("customModelData", Config.CUSTOM_MODEL_DATA);
            metrics.add("enableWithdraw", Config.ENABLE_WITHDRAW);
            metrics.add("maxWithdrawAmount", Config.MAX_WITHDRAW_AMOUNT);
            metrics.add("minePercentage", Config.MINE_PERCENTAGE * 100 + "%");
            metrics.add("onlyExperienceBlocks", Config.ONLY_EXPERIENCE_BLOCKS);
            metrics.add("loseOnDeath", Config.LOSE_ON_DEATH);
            metrics.add("moneyTaken", (Config.MONEY_TAKEN_FROM + Config.MONEY_TAKEN_TO) / 2);
            metrics.add("takePercentage", Config.TAKE_PERCENTAGE);
            metrics.add("dropOnDeath", Config.DROP_ON_DEATH);
            metrics.add("deathMessage", Config.DEATH_MESSAGE);

            metrics.add("usingSkullTexture", Config.SKULL_TEXTURE != null && !Config.SKULL_TEXTURE.isEmpty());
            metrics.add("usingPaper", PaperLib.isPaper());
            metrics.add("usingMythicMobs", Coins.hasMythicMobs());
        });
    }
}
