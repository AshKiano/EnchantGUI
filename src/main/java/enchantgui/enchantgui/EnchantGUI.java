package enchantgui.enchantgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantGUI extends JavaPlugin implements Listener {

    private final Map<UUID, Map<Enchantment, Integer>> playerEnchants = new HashMap<>();

    @Override
    public void onEnable() {
        this.getCommand("enchantgui").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("enchantgui.use")) {
                    openEnchantMenu(player);
                } else {
                    player.sendMessage("You do not have permission to use this command.");
                }
            }
            return true;
        });
        Bukkit.getPluginManager().registerEvents(this, this);
        Metrics metrics = new Metrics(this, 21786);
        this.getLogger().info("Thank you for using the EnchantGUI plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");
    }

    private void openEnchantMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Enchant your item");
        addEnchantment(inv, 0, Enchantment.PROTECTION_ENVIRONMENTAL, Material.ENCHANTED_BOOK, "Protection");
        addEnchantment(inv, 1, Enchantment.PROTECTION_FIRE, Material.ENCHANTED_BOOK, "Fire Protection");
        addEnchantment(inv, 2, Enchantment.PROTECTION_FALL, Material.ENCHANTED_BOOK, "Feather Falling");
        addEnchantment(inv, 3, Enchantment.PROTECTION_EXPLOSIONS, Material.ENCHANTED_BOOK, "Blast Protection");
        addEnchantment(inv, 4, Enchantment.PROTECTION_PROJECTILE, Material.ENCHANTED_BOOK, "Projectile Protection");
        addEnchantment(inv, 5, Enchantment.OXYGEN, Material.ENCHANTED_BOOK, "Respiration");
        addEnchantment(inv, 6, Enchantment.WATER_WORKER, Material.ENCHANTED_BOOK, "Aqua Affinity");
        addEnchantment(inv, 7, Enchantment.THORNS, Material.ENCHANTED_BOOK, "Thorns");
        addEnchantment(inv, 8, Enchantment.DEPTH_STRIDER, Material.ENCHANTED_BOOK, "Depth Strider");
        addEnchantment(inv, 9, Enchantment.FROST_WALKER, Material.ENCHANTED_BOOK, "Frost Walker");
        addEnchantment(inv, 10, Enchantment.DAMAGE_ALL, Material.ENCHANTED_BOOK, "Sharpness");
        addEnchantment(inv, 11, Enchantment.DAMAGE_UNDEAD, Material.ENCHANTED_BOOK, "Smite");
        addEnchantment(inv, 12, Enchantment.DAMAGE_ARTHROPODS, Material.ENCHANTED_BOOK, "Bane of Arthropods");
        addEnchantment(inv, 13, Enchantment.KNOCKBACK, Material.ENCHANTED_BOOK, "Knockback");
        addEnchantment(inv, 14, Enchantment.FIRE_ASPECT, Material.ENCHANTED_BOOK, "Fire Aspect");
        addEnchantment(inv, 15, Enchantment.LOOT_BONUS_MOBS, Material.ENCHANTED_BOOK, "Looting");
        addEnchantment(inv, 16, Enchantment.DIG_SPEED, Material.ENCHANTED_BOOK, "Efficiency");
        addEnchantment(inv, 17, Enchantment.SILK_TOUCH, Material.ENCHANTED_BOOK, "Silk Touch");
        addEnchantment(inv, 18, Enchantment.DURABILITY, Material.ENCHANTED_BOOK, "Unbreaking");
        addEnchantment(inv, 19, Enchantment.LOOT_BONUS_BLOCKS, Material.ENCHANTED_BOOK, "Fortune");
        addEnchantment(inv, 20, Enchantment.ARROW_DAMAGE, Material.ENCHANTED_BOOK, "Power");
        addEnchantment(inv, 21, Enchantment.ARROW_KNOCKBACK, Material.ENCHANTED_BOOK, "Punch");
        addEnchantment(inv, 22, Enchantment.ARROW_FIRE, Material.ENCHANTED_BOOK, "Flame");
        addEnchantment(inv, 23, Enchantment.ARROW_INFINITE, Material.ENCHANTED_BOOK, "Infinity");
        addEnchantment(inv, 24, Enchantment.LUCK, Material.ENCHANTED_BOOK, "Luck of the Sea");
        addEnchantment(inv, 25, Enchantment.LURE, Material.ENCHANTED_BOOK, "Lure");
        addEnchantment(inv, 26, Enchantment.MENDING, Material.ENCHANTED_BOOK, "Mending");
        addEnchantment(inv, 27, Enchantment.BINDING_CURSE, Material.ENCHANTED_BOOK, "Curse of Binding");
        addEnchantment(inv, 28, Enchantment.VANISHING_CURSE, Material.ENCHANTED_BOOK, "Curse of Vanishing");
        addEnchantment(inv, 29, Enchantment.SWEEPING_EDGE, Material.ENCHANTED_BOOK, "Sweeping Edge");
        // Continue adding more enchantments as needed

        player.openInventory(inv);
    }

    private void addEnchantment(Inventory inv, int slot, Enchantment enchantment, Material material, String name) {
        ItemStack item = new ItemStack(material);
        item.addUnsafeEnchantment(enchantment, 1);
        inv.setItem(slot, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Enchant your item") && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.ENCHANTED_BOOK) {
                Enchantment enchant = clickedItem.getEnchantments().keySet().iterator().next();
                Map<Enchantment, Integer> enchants = playerEnchants.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
                int currentLevel = enchants.getOrDefault(enchant, 0);

                if (event.isLeftClick()) {
                    enchants.put(enchant, currentLevel + 1);
                } else if (event.isRightClick()) {
                    if (currentLevel > 1) {
                        enchants.put(enchant, currentLevel - 1);
                    } else {
                        enchants.remove(enchant);  // Remove enchant from map when level reaches zero
                    }
                }

                ItemStack handItem = player.getInventory().getItemInMainHand();
                if (handItem != null && handItem.getType() != Material.AIR) {
                    if (enchants.containsKey(enchant)) {
                        handItem.addUnsafeEnchantment(enchant, enchants.get(enchant));
                    } else {
                        handItem.removeEnchantment(enchant);  // Remove the enchantment from the item if level is zero
                    }
                    player.sendMessage("Enchantment " + enchant.getKey().getKey() + " adjusted to level " + (enchants.containsKey(enchant) ? enchants.get(enchant) : 0) + "!");
                } else {
                    player.sendMessage("You must hold an item in your hand to enchant!");
                }
            }
        }
    }
}