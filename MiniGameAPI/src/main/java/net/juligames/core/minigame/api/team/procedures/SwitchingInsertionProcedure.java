package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.api.TODO;
import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * This is currently not working because the {@link SwitchingInsertionProcedure} has no idea what the {@link IterativeInsertionProcedure} did.
 *
 * @author Ture Bentzin
 * 23.12.2022
 */
@TODO(doNotcall = true)
public final class SwitchingInsertionProcedure implements InsertionProcedure {

    @Nullable
    private Team lastTeam;

    @Contract(pure = true)
    @Override
    public @NotNull Boolean apply(@NotNull Set<Team> teams, UUID uuid) {
        if (lastTeam == null) {
            teams.stream().findAny().ifPresentOrElse(team -> lastTeam = team,
                    () -> {
                        throw new IllegalArgumentException("lastTeam is null and there is no team available to be selected!");
                    });
        }
        if (!teams.contains(lastTeam)) {
            throw new IllegalArgumentException("lastTeam of this SwitchingInsertionProcedure is not part of teams!");
        }
        return new ComparatorInsertionProcedure((o1, o2) -> { //needs testing - maybe reversed order - not running - no feedback on what team was selected...
            if (o2 == lastTeam) return 1;
            if (o2 == o1 || o2.equals(o1)) return 0;
            return -1;
        }).apply(teams, uuid);
    }

    public @Nullable Team getLastTeam() {
        return lastTeam;
    }
}
