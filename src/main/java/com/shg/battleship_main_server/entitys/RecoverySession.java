package com.shg.battleship_main_server.entitys;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class RecoverySession {

    private final UUID gameId;
    private final Map<UUID, Boolean> confirmations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> timeoutTask;
    @Getter
    private volatile boolean cancelled = false;

    public RecoverySession(UUID gameId){
        this.gameId = gameId;
    }

    public void startTimeout(Runnable onTimeout) {
        timeoutTask = scheduler.schedule(() -> {
            cancelled = true;
            onTimeout.run();
        }, 30, TimeUnit.SECONDS);
    }

    public void confirm(UUID playerId, boolean confirm) {
        confirmations.put(playerId, confirm);
        if (!confirm) {
            cancelled = true;
            cancelTimeout();
        } else if (isBothConfirmed()) {
            cancelTimeout();
        }
    }

    public boolean isBothConfirmed() {
        return confirmations.values().stream()
                .filter(Boolean::booleanValue)
                .count() >= 2;
    }

    private void cancelTimeout() {
        if (timeoutTask != null) {
            timeoutTask.cancel(true);
        }
    }
}
