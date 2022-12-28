package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * 23.12.2022
 */
@SuppressWarnings("AssignmentUsedAsCondition")
public record IterativeInsertionProcedure(boolean fillFirst) implements InsertionProcedure {

    /**
     * @return an instance of {@link IterativeInsertionProcedure} that does not fill non-empty Teams first!
     */
    public static InsertionProcedure getInstance() {
        return instance;
    }

    private static final IterativeInsertionProcedure instance = new IterativeInsertionProcedure(false);

    @Override
    public Boolean apply(final @NotNull Set<Team> teams, final UUID uuid) {
        boolean success = false;
        //return success;
        if(fillFirst) {
            for (Team team : teams) {
                if(!success && !team.isEmpty() && team.hasCapacity())
                    success = team.tryAdd(uuid);
            }
            if(!success) {
                return new IterativeInsertionProcedure(false).apply(teams,uuid);
            }else {
                return true;
            }
        }else {
            //normal procedure
            for (Team team : teams) if (success = team.tryAdd(uuid)) break; //deal?
            return success;
        }
    }
}
