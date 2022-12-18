package net.juligames.core.minigame.api;

import net.juligames.core.minigame.api.team.Team;
import net.juligames.core.minigame.api.team.TeamColor;
import net.juligames.core.minigame.api.team.procedures.InsertionProcedure;
import net.juligames.core.minigame.api.team.procedures.RandomInsertionProcedure;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public class Teamer {

    private final Set<Team> possibleTeams;

    private final InsertionProcedure insertionProcedure;

    public Teamer(@NotNull Set<TeamColor> possibleColors, int maxTeamCapacity) {
        this(possibleColors, maxTeamCapacity, new RandomInsertionProcedure(false));
    }

    public Teamer(@NotNull Set<TeamColor> possibleColors, int maxTeamCapacity, InsertionProcedure insertionProcedure) {
        this.insertionProcedure = insertionProcedure;
        Set<Team> teams = new HashSet<>();
        for (TeamColor possibleColor : possibleColors) {
            teams.add(new Team(possibleColor,() -> maxTeamCapacity));
        }
        possibleTeams = teams;
    }

    public Teamer(int maxTeamCapacity, InsertionProcedure insertionProcedure) {
        this(TeamColor.getAllDefaultNamed(), maxTeamCapacity, insertionProcedure);
    }

    @ApiStatus.Experimental
    public Map<UUID,Team> teaming() {
        Map<UUID,Team> mapping = new HashMap<>();
        for (Team possibleTeam : possibleTeams) {
            possibleTeam.getMembers().forEach(uuid -> mapping.put(uuid,possibleTeam));
        }
        return mapping;
    }

    public Set<Team> usedTeams() {
        return possibleTeams.stream().filter(team -> !team.getMembers().isEmpty()).collect(Collectors.toUnmodifiableSet());
    }

    public boolean addUUID(UUID uuid) {
        if(teaming().containsKey(uuid)) {
            return false; //already teamed
        }
        return insertionProcedure.apply(possibleTeams,uuid);
    }

    public boolean addUUID(UUID uuid, Team team) {
       if(possibleTeams.contains(team)) {

       }else {
           throw new IllegalArgumentException("the given team is not allowed here!");
       }
    }

    public boolean addUUID(UUID uuid, Predicate<Team> teamPredicate) {
        AtomicBoolean r = new AtomicBoolean(true);
       possibleTeams.stream().filter(teamPredicate).findFirst().ifPresentOrElse(team -> addUUID(uuid,team),() -> r.set(false));
       return r.get();
    }
}
