package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.team.Team;

import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * A sealed interface that defines the insertion procedure for adding players to teams.
 * The method {@code apply()} should return {@code true} if the player was successfully inserted into a team,
 * and {@code false} if the insertion failed for any reason.
 * <p>
 * You can use {@link ConsumerInsertionProcedure} to make a custom implementation (at the time) - this system is subjet
 * to change but i will try to not break compatibility
 *
 * @author Ture Bentzin
 * 18.12.2022
 */
public sealed interface InsertionProcedure extends BiFunction<Set<Team>, UUID, Boolean>
        permits ComparatorInsertionProcedure, ConsumerInsertionProcedure, IterativeInsertionProcedure,
        PreDefinedInsertionProcedure, RandomInsertionProcedure, SwitchingInsertionProcedure, WhiteListInsertionProcedure {
}
