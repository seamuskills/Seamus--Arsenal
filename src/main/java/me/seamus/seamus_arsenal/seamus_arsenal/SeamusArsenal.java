package me.seamus.seamus_arsenal.seamus_arsenal;

import jdk.internal.util.Preconditions;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.data.type.Bed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;

import java.util.function.Predicate;

public final class SeamusArsenal extends JavaPlugin {
    public BukkitScheduler scheduler = getServer().getScheduler();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new eventHandling(),this);
        this.getCommand("giveweapon").setExecutor(new GiveWeaponCommand());
    }

    public class eventHandling implements Listener {
        @EventHandler
        public void onClick(PlayerInteractEvent event){
            Player p = event.getPlayer(); //player
            ItemStack item = event.getItem() == null ? (event.getHand() == EquipmentSlot.HAND ? event.getPlayer().getInventory().getItemInMainHand() : p.getInventory().getItemInOffHand()) : event.getItem(); //item used
            Location playerLocation = p.getLocation(); //location of player
            Action action = event.getAction(); //what kind of interaction this was
            ItemMeta meta = item.getItemMeta(); //the metadata of the item
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){ //if action was a right click
                if (meta != null && item.getType().getMaxDurability() > 0){ //if the item has metadata and durability
                    for (String name : getConfig().getKeys(true)){
                        if (name.equals("weapons")){
                            continue;
                        }
                        if (name.equals("WeaponStop")){
                            break;
                        }
                        String[] parts = name.split("\\.");
                        if (parts.length > 2){
                            continue;
                        }
                        name = parts[parts.length-1];
                        if (meta.getDisplayName().equals(name)){
                            event.setCancelled(true);
                            if (p.hasCooldown(item.getType())){
                                return;
                            }
                            String path = "weapons."+name+".";
                            if (getIfUsed(meta,item,getConfig().getInt(path+"clipSize"))){
                                p.setCooldown(item.getType(), 10000000);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                scheduler.scheduleSyncDelayedTask(SeamusArsenal.this, () -> {
                                    item.getItemMeta().removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                    p.setCooldown(item.getType(),0);
                                    ((Damageable)meta).setDamage(0);
                                    item.setItemMeta(meta);
                                },(long) Math.floor(getConfig().getDouble(path + "reloadTime") * 20));
                            }else{
                                p.setCooldown(item.getType(), (int) Math.floor(getConfig().getDouble(path + "fireRate") * 20));
                                RayTraceResult hit = p.getWorld().rayTrace(p.getEyeLocation(),p.getEyeLocation().getDirection(),getConfig().getDouble(path+"range"), FluidCollisionMode.NEVER,true,getConfig().getDouble(path+"bulletSize"), (Entity e) -> e != p);
                                if (hit != null) {
                                    if (hit.getHitEntity() != null && hit.getHitEntity() instanceof LivingEntity) {
                                        Entity e = hit.getHitEntity();
                                        ((LivingEntity) e).damage(getConfig().getDouble(path + "damage"), p);
                                    }
                                    if (getConfig().getString(path+"particle") != null) {
                                        try {
                                            p.getWorld().spawnParticle(Particle.valueOf(getConfig().getString(path + "particle")), hit.getHitPosition().toLocation(p.getWorld()), 2);
                                        }catch(Exception ex){
                                            System.out.println("particle used for "+path+" is invalid! nothing displayed!");
                                        }
                                    }
                                }
                                ((Damageable) meta).setDamage(((Damageable)meta).getDamage() + (item.getType().getMaxDurability() / getConfig().getInt(path+"clipSize")));
                                item.setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean getIfUsed(ItemMeta meta, ItemStack item, int magSize){ //get if item is on last use of durability
        float increment = item.getType().getMaxDurability() / magSize;
        float damage = ((Damageable)meta).getDamage();

        return damage >= increment * magSize-1;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
