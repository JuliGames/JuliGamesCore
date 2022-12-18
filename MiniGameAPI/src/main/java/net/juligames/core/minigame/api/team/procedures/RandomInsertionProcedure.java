package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.TeamColor;

import java.util.Map;
import java.util.UUID;

/**
 * This Procedure selects one random Team and assigns the UUID to it
 * @author Ture Bentzin
 * 18.12.2022
 */
public class RandomInsertionProcedure implements InsertionProcedure{

    private final boolean fillFirst;

    public RandomInsertionProcedure(final boolean fillFirst) {

        this.fillFirst = fillFirst;
    }

    @Override
    public void accept(Map<UUID, TeamColor> uuidTeamColorMap, UUID uuid) {
        if(isFillFirst()) {

        }
    }

    public boolean isFillFirst() {
        return fillFirst;
    }
}
