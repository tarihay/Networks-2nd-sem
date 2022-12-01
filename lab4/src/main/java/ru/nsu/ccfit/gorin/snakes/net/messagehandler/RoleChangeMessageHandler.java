package ru.nsu.ccfit.gorin.snakes.net.messagehandler;

import org.jetbrains.annotations.NotNull;
import ru.nsu.ccfit.gorin.snakes.net.NetNode;
import ru.nsu.ccfit.gorin.snakes.net.messages.RoleChangeMessage;

public interface RoleChangeMessageHandler {
    void handle(@NotNull NetNode sender, @NotNull RoleChangeMessage roleChangeMsg);
}
