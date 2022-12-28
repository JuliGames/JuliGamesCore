package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import java.util.function.BiFunction;

/**
 * @apiNote This should only be used in cases where the MiniGame requires a game-specific insertion. In the case you miss
 * a procedure you think might advance this api, then please report this via issue on GitHub or email me
 * at  {@summary bentzin@tdrstudios.de}
 * @author Ture Bentzin
 * 23.12.2022
 */
@ApiStatus.Experimental
public record ConsumerInsertionProcedure(BiFunction<Collection<Team>, UUID, Boolean> procedure) implements InsertionProcedure {
    @Override
    @Nullable
    public Boolean apply(Set<Team> teams, UUID uuid) {
        return procedure.apply(teams,uuid);
    }
}
