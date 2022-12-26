package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public record WhiteListInsertionProcedure(BiPredicate<Team, UUID> allower) implements InsertionProcedure {
    @Override
    @NotNull
    public Boolean apply(@NotNull Set<Team> teams, UUID uuid) {
        return IterativeInsertionProcedure.
                getInstance().
                apply(teams.stream().filter(team -> allower.test(team, uuid)).
                        collect(Collectors.toCollection(LinkedHashSet::new)), uuid);

    }
}
