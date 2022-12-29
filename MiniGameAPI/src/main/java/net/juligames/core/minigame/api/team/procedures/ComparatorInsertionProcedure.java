package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
public record ComparatorInsertionProcedure(Comparator<Team> teamComparator) implements InsertionProcedure {
    @Override
    public Boolean apply(@NotNull Set<Team> teams, UUID uuid) {
        return IterativeInsertionProcedure
                .getInstance().apply(teams.stream().
                        sorted(teamComparator).
                        collect(Collectors.toCollection(LinkedHashSet::new)), uuid);
    }
}
