package net.juligames.core.minigame.api.team.procedures;

import net.juligames.core.minigame.api.Teamer;
import net.juligames.core.minigame.api.team.Team;
import net.juligames.core.minigame.api.team.TeamColor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public sealed interface InsertionProcedure extends BiFunction<Set<Team>, UUID, Boolean>
        permits ComparatorInsertionProcedure, ConsumerInsertionProcedure, IterativeInsertionProcedure, RandomInsertionProcedure, SwitchingInsertionProcedure, WhiteListInsertionProcedure
{}
