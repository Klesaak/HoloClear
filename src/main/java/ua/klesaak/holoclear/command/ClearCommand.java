package ua.klesaak.holoclear.command;


import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ua.klesaak.holoclear.manager.ClearManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//todo clear all, entities, drop, projectiles
public class ClearCommand implements CommandExecutor, TabCompleter {
    private final ClearManager clearManager;

    public ClearCommand(ClearManager clearManager) {
        this.clearManager = clearManager;
        Objects.requireNonNull(clearManager.getHoloClearPlugin().getCommand("clear")).setExecutor(this);
        Objects.requireNonNull(clearManager.getHoloClearPlugin().getCommand("clear")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return new ArrayList<>();
    }
}
