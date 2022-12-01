package ru.nsu.ccfit.gorin.snakes.net.messagehandler;

import org.jetbrains.annotations.NotNull;
import ru.nsu.ccfit.gorin.snakes.net.NetNode;
import ru.nsu.ccfit.gorin.snakes.net.messages.ErrorMessage;


public interface ErrorMessageHandler {
    void handle(@NotNull NetNode sender, @NotNull ErrorMessage errorMsg);
}
