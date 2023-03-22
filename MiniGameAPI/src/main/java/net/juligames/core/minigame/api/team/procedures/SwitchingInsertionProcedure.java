package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.api.TODO;
import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * This is now independent of the {@link IterativeInsertionProcedure}! Within the {@link #apply(Set, UUID)} method there are
 * parts of the implementation of {@link ComparatorInsertionProcedure} and {@link IterativeInsertionProcedure}
 *
 * @author Ture Bentzin
 * 23.12.2022
 * @apiNote Im not sure if i want to keep this behavior..
 */
@TODO(doNotcall = false)
public final class SwitchingInsertionProcedure implements InsertionProcedure {

    @Nullable
    private Team lastTeam;

    @Contract(pure = true)
    @Override
    public @NotNull Boolean apply(@NotNull Set<Team> teams, UUID uuid) {
        if (lastTeam == null)
            teams.stream().findAny().ifPresentOrElse(team -> lastTeam = team, () -> {
                throw new IllegalArgumentException("lastTeam is null and there is no team available to be selected!");
            });

        if (!teams.contains(lastTeam))
            throw new IllegalArgumentException("lastTeam of this SwitchingInsertionProcedure is not part of teams!");
        /*
        Boolean apply = new ComparatorInsertionProcedure((o1, o2) -> { //needs testing - maybe reversed order - not running - no feedback on what team was selected...
            if (o2 == lastTeam) return 1;
            if (o2 == o1 || o2.equals(o1)) return 0;
            return -1;
        }).apply(teams, uuid);
         */
        for (Team team : teams.stream().sorted((o1, o2) -> {
            if (o2 == lastTeam) return 1;
            if (o2 == o1 || o2.equals(o1)) return 0;
            return -1;
        }).toList()) {
            if (team.tryAdd(uuid)) {
                lastTeam = team;
                return true;
            }
        }
        return false;
    }

    public @Nullable Team getLastTeam() {
        return lastTeam;
    }

    public @NotNull Optional<Team> lastTeamOptional() {
        return Optional.ofNullable(getLastTeam());
    }
}
