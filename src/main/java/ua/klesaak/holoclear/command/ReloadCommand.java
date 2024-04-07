package ua.klesaak.holoclear.command;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ua.klesaak.holoclear.manager.ClearManager;

import java.util.Objects;

public class ReloadCommand implements CommandExecutor {
    private final ClearManager clearManager;

    public ReloadCommand(ClearManager clearManager) {
        this.clearManager = clearManager;
        Objects.requireNonNull(clearManager.getHoloClearPlugin().getCommand("hc-reload")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        long time = System.currentTimeMillis();
        this.clearManager.reload();
        commandSender.sendMessage(ChatColor.GOLD + "Found " + this.clearManager.getEntityList().size() + " dropped items!");
        commandSender.sendMessage(ChatColor.GREEN + this.clearManager.getHoloClearPlugin().getDescription().getName() + " successfully reload! (" + (System.currentTimeMillis() - time) + "ms) ");
        return true;
    }
}
