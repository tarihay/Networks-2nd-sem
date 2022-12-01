package ru.nsu.ccfit.gorin.snakes.net.messagehandler;

import org.jetbrains.annotations.NotNull;
import ru.nsu.ccfit.gorin.snakes.net.NetNode;
import ru.nsu.ccfit.gorin.snakes.net.messages.StateMessage;

public interface StateMessageHandler {
    void handle(@NotNull NetNode sender, @NotNull StateMessage stateMsg);
}
