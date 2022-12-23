package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public final class SwitchingInsertionProcedure implements InsertionProcedure {

    @Nullable
    private Team lastTeam;

    @Contract(pure = true)
    @Override
    public @NotNull Boolean apply(@NotNull Set<Team> teams, UUID uuid) {
        if(!teams.contains(lastTeam)) {
            throw new IllegalArgumentException("lastTeam of this SwitchingInsertionProcedure is not part of teams!");
        }
        return new ComparatorInsertionProcedure((o1, o2) -> { //needs testing - maybe reversed order
            if (o2 == lastTeam) return 1;
            if (o2 == o1 || o2.equals(o1)) return 0;
            return -1;
        }).apply(teams, uuid);
    }

    public @Nullable Team getLastTeam() {
        return lastTeam;
    }
}
