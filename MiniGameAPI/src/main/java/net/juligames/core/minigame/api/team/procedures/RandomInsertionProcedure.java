package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import net.juligames.core.util.ShuffleUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This Procedure selects one random Team and assigns the UUID to it
 *
 * @author Ture Bentzin
 * 18.12.2022
 */
public record RandomInsertionProcedure(boolean fillFirst) implements InsertionProcedure {

    @SuppressWarnings("AssignmentUsedAsCondition")
    @Override
    public @NotNull Boolean apply(Set<Team> teams, UUID uuid) {
        boolean success = false;
        final Collection<Team> shuffled = ShuffleUtil.shuffleToNew(teams);
        if(fillFirst) {
            for (Team team : shuffled) {
                if(!success && !team.isEmpty() && team.hasCapacity()) {
                    success = team.tryAdd(uuid);
                }
            }
            if(!success) {
                return new RandomInsertionProcedure(false).apply(teams,uuid);
            }else {
                return true;
            }
        }else {
            //normal procedure
            for (Team team : shuffled) if(success = team.tryAdd(uuid)) break; //deal?
            return success;
        }
    }
}
