package com.lakki.betterspawnprotect;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class betterSpawnProtectMain extends JavaPlugin implements Listener {

    private static betterSpawnProtectMain instance;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        instance = this;
    }

    public static betterSpawnProtectMain getInstance() {
        return instance;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(getConfig().getBoolean("playerAttack-enable")) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                if (!event.getDamager().isOp()) {
                    Player victim = (Player) event.getEntity();
                    Player attacker = (Player) event.getDamager();

                    Location center = new Location(victim.getWorld(),  getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"));
                    double radius = getConfig().getInt("playerAttack-radius") * getConfig().getInt("playerAttack-radius");

                    if (victim.getLocation().distance(center) <= radius || attacker.getLocation().distance(center) <= radius) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (getConfig().getBoolean("explode-enable")) {
            if (event.getEntity() instanceof Creeper) {
                return;
            }
            if (event.getLocation().distanceSquared(new Location(event.getEntity().getWorld(), getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"))) <= getConfig().getInt("explode-radius") * getConfig().getInt("explode-radius")) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();

        if (getConfig().getBoolean("ignite-enable")) {
            if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
                Player player = event.getPlayer();
                if (!player.isOp()) {
                    Location center = new Location(block.getWorld(), getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"));
                    double radiusSquared = getConfig().getDouble("ignite-radius") * getConfig().getDouble("ignite-radius");

                    if (block.getLocation().distanceSquared(center) <= radiusSquared) {
                        event.setCancelled(true);
                    }
                }
            } else if (event.getCause() == BlockIgniteEvent.IgniteCause.LAVA) {
                Player player = event.getPlayer();
                if (!player.isOp()) {
                    if (event.getIgnitingBlock() != null && event.getIgnitingBlock().hasMetadata("playerPlaced")) {
                        Location center = new Location(block.getWorld(), getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"));
                        double radiusSquared = getConfig().getDouble("ignite-radius") * getConfig().getDouble("ignite-radius");

                        if (block.getLocation().distanceSquared(center) <= radiusSquared) {
                            event.setCancelled(true);
                        }
                    }
                }
            } else {
                event.setCancelled(false);
            }
        }

    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (getConfig().getBoolean("playerCanBreakBlock-enable")) {
            if (!player.isOp()) {
                Location center = new Location(player.getWorld(), getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"));
                double radius = getConfig().getInt("playerCanBreakBlock-radius") * getConfig().getInt("playerCanBreakBlock-radius");

                if (player.getLocation().distance(center) <= radius) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public  void onPlayerPlaceBlock(BlockPlaceEvent event){
        if (getConfig().getBoolean("playerCanPlaceBlock-enable")){
            Player player = event.getPlayer();
            if (!player.isOp()){
                Location center = new Location(player.getWorld(), getConfig().getDouble("spawn-x"), getConfig().getDouble("spawn-y"), getConfig().getDouble("spawn-z"));
                double radius = getConfig().getInt("playerCanPlaceBlock-radius") * getConfig().getInt("playerCanPlaceBlock-radius");

                if (player.getLocation().distance(center) <= radius) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
