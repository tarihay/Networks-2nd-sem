package ru.nsu.ccfit.gorin.snakes.net.gamelistchecker;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface GameListObserver {
    void updateGameList(@NotNull Collection<GameInfo> gameInfos);
}
