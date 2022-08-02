package me.seamus.seamus_arsenal.seamus_arsenal;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveWeaponCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3){
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "Not enough arguments, command usage: /giveweapon <player> <tool> <weapon name>");
                return true;
            }else if (sender instanceof BlockCommandSender){
                return true;
            }else{
                System.out.println("Seamus\' Arsenal: command giveweapon used incorrectly! correct usage: giveweapon <player> <tool> <weapon name>");
                return true;
            }
        } //<player> <tool> <weapon name>

        Player target = sender.getServer().getPlayer(args[0]);

        if (target == null){
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "target is null!");
                return true;
            }else if (sender instanceof BlockCommandSender){
                return true;
            }else{
                System.out.println("Seamus\' Arsenal: command giveweapon used incorrectly! target is null!");
                return true;
            }
        }

        ItemStack item = new ItemStack(Material.AIR);
        Material itemMaterial = Material.matchMaterial(args[1]);

        if (itemMaterial == null){
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "Item type is null!");
                return true;
            }else if (sender instanceof BlockCommandSender){
                return true;
            }else{
                System.out.println("Seamus\' Arsenal: command giveweapon used incorrectly! Item type is null!");
                return true;
            }
        }

        item.setType(itemMaterial);
        ItemMeta meta =  item.getItemMeta();
        meta.setDisplayName(args[2]);
        item.setItemMeta(meta);

        target.getInventory().setItemInMainHand(item);

        return true;
    }
}
