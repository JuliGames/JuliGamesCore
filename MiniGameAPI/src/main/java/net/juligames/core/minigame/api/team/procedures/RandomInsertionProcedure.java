package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import net.juligames.core.util.ShuffleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This Procedure selects one random Team and assigns the UUID to it
 *
 * @author Ture Bentzin
 * 18.12.2022
 */
public record RandomInsertionProcedure(boolean fillFirst) implements InsertionProcedure {

    @Override
    public @NotNull Boolean apply(Set<Team> teams, UUID uuid) {
        return new IterativeInsertionProcedure(fillFirst)
                .apply(new LinkedHashSet<>(ShuffleUtil.shuffleToNew(teams)), uuid);
    }
}
