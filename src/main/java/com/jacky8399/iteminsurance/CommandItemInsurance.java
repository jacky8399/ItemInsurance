package com.jacky8399.iteminsurance;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandItemInsurance implements TabExecutor {

    static boolean checkPerm(CommandSender sender, String command) {
        boolean result = sender.hasPermission("iteminsurance.command." + command);
        if (!result)
            sender.sendMessage(ChatColor.RED + "You don't have permission!");
        return result;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ItemInsurance plugin = ItemInsurance.get();
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "You are running ItemInsurance v" + plugin.getDescription().getVersion());
            return true;
        }
        switch (args[0]) {
            case "reload" -> {
                if (!checkPerm(sender, "reload"))
                    return true;
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "Configuration reloaded!");
            }
            case "test" -> {
                if (!checkPerm(sender, "test"))
                    return true;
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /<command> test <player>");
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player " + args[1] + " is not online!");
                }

                ItemStack[] items = plugin.provider.getLastInventory(player);
                if (items == null) {
                    sender.sendMessage(ChatColor.YELLOW + "Couldn't get last inventory of player");
                } else {
                    sender.sendMessage(ChatColor.GREEN + "Last inventory of player:");
                    sender.sendMessage(ChatColor.GREEN + Arrays.stream(items)
                            .filter(Objects::nonNull)
                            .map(stack -> stack.getType().name())
                            .collect(Collectors.joining(", "))
                    );
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return switch (args.length) {
            case 0, 1 -> Stream.of("reload", "test")
                    .filter(arg -> args.length == 0 || arg.startsWith(args[0]))
                    .collect(Collectors.toList());
            case 2 -> switch (args[0]) {
                case "test" -> Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(playerName -> playerName.startsWith(args[1]))
                        .collect(Collectors.toList());
                default -> Collections.emptyList();
            };
            default -> Collections.emptyList();
        };
    }
}
