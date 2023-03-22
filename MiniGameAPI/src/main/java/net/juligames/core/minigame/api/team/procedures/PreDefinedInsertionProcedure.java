package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PreDefinedInsertionProcedure implements InsertionProcedure {

    private final Map<UUID, Team> preDefinedTeaming;

    public PreDefinedInsertionProcedure(Map<UUID, Team> preDefinedTeaming) {

        this.preDefinedTeaming = preDefinedTeaming;
    }

    @Override
    public @NotNull Boolean apply(Set<Team> teams, UUID uuid) {
        if (preDefinedTeaming.containsKey(uuid)) {
            Team should = preDefinedTeaming.get(uuid);
            if (teams.contains(should)) {
                return should.tryAdd(uuid);
            }
        }
        return false;
    }
}
