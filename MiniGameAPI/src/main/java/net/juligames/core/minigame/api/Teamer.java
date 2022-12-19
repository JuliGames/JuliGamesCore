package net.juligames.core.minigame.api;

import de.bentzin.tools.collection.Subscription;
import net.juligames.core.api.API;
import net.juligames.core.minigame.api.team.Team;
import net.juligames.core.minigame.api.team.TeamColor;
import net.juligames.core.minigame.api.team.procedures.InsertionProcedure;
import net.juligames.core.minigame.api.team.procedures.RandomInsertionProcedure;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
@SuppressWarnings({"ProtectedMemberInFinalClass", "unused"})
public final class Teamer {

    private final Set<Team> possibleTeams;

    private final InsertionProcedure insertionProcedure;

    private final BiConsumer<UUID, Team> teamJoinConsumer;

    private final BiConsumer<UUID, Team> teamLeaveConsumer;


    public Teamer(@NotNull Set<TeamColor> possibleColors, int maxTeamCapacity, BiConsumer<UUID, Team> teamLeaveConsumer, BiConsumer<UUID, Team> teamJoinConsumer) {
        this(possibleColors, maxTeamCapacity, new RandomInsertionProcedure(false), teamJoinConsumer, teamLeaveConsumer);
        subscribePossibleTeams();
    }

    public Teamer(@NotNull Set<TeamColor> possibleColors, int maxTeamCapacity, InsertionProcedure insertionProcedure, BiConsumer<UUID, Team> teamJoinConsumer, BiConsumer<UUID, Team> teamLeaveConsumer) {
        this.insertionProcedure = insertionProcedure;
        this.teamJoinConsumer = teamJoinConsumer;
        this.teamLeaveConsumer = teamLeaveConsumer;
        Set<Team> teams = new HashSet<>();
        for (TeamColor possibleColor : possibleColors) {
            teams.add(new Team(possibleColor, () -> maxTeamCapacity));
        }
        possibleTeams = teams;

        subscribePossibleTeams();
    }

    public Teamer(int maxTeamCapacity, InsertionProcedure insertionProcedure, BiConsumer<UUID, Team> teamLeaveConsumer, BiConsumer<UUID, Team> teamJoinConsumer) {
        this(TeamColor.getAllDefaultNamed(), maxTeamCapacity, insertionProcedure, teamJoinConsumer, teamLeaveConsumer);
        subscribePossibleTeams();
    }

    @ApiStatus.Internal
    private void subscribePossibleTeams() {
        for (Team possibleTeam : possibleTeams) {
            possibleTeam.getMembers().subscribe((uuid, subscriptionType)
                    -> teamJoinConsumer.accept(uuid, possibleTeam), Subscription.SubscriptionType.ADD);

            possibleTeam.getMembers().subscribe((uuid, subscriptionType)
                    -> teamLeaveConsumer.accept(uuid, possibleTeam), Subscription.SubscriptionType.REMOVE);
        }
    }

    /**
     * @return A Map from every UUID to their Team
     */
    @ApiStatus.Experimental
    public @NotNull Map<UUID, Team> teaming() {
        Map<UUID, Team> mapping = new HashMap<>();
        for (Team possibleTeam : possibleTeams) { //may be usedTeams
            possibleTeam.getMembers().forEach(uuid -> mapping.put(uuid, possibleTeam));
        }
        return mapping;
    }

    /**
     * @return A Set of all Teams that have uuids within them
     */
    public Set<Team> usedTeams() {
        return possibleTeams.stream().filter(team -> !team.getMembers().isEmpty()).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * This will team the uuid regarding the given {@link InsertionProcedure} -> {@link Teamer#insertionProcedure}
     *
     * @param uuid the player to team
     * @return true if the uuid was teamed and false if not
     */
    public boolean addUUID(UUID uuid) {
        if (teaming().containsKey(uuid)) {
            return false; //already teamed
        }
        return insertionProcedure.apply(possibleTeams, uuid);
    }

    /**
     * @param uuid the player to team
     * @param team team the player will be assigned to if possible
     * @return true if the uuid was teamed and false if not
     */
    public boolean addUUID(UUID uuid, Team team) {
        if (possibleTeams.contains(team)) {
            return team.tryAdd(uuid);
        } else {
            throw new IllegalArgumentException("the given team is not allowed here!");
        }
    }

    /**
     * @param uuid          the player to team
     * @param preferredTeam this teamer will try to team the uuid in this team and if not possible assign the team regarding the given {@link InsertionProcedure}
     * @return true if the uuid was teamed and false if not
     */
    public boolean addUUIDWithPreferredTeam(UUID uuid, Team preferredTeam) {
        if (containsUUID(uuid)) return false;
        if (possibleTeams.contains(preferredTeam)) {
            if (preferredTeam.tryAdd(uuid)) {
                return true;
            } else {
                return addUUID(uuid);
            }
        } else {
            throw new IllegalArgumentException("the given preferredTeam is not allowed here!");
        }
    }

    //removal

    /**
     * This will remove the first (and hopefully only) occurrence of the given UUID from the Teamer
     *
     * @param uuid uuid to remove
     * @return an Optional with the team the uuid was removed from
     */
    public Optional<Team> removeUUID(UUID uuid) {
        for (Team usedTeam : usedTeams()) {
            for (UUID member : usedTeam.getMembers()) {
                if (member.equals(uuid)) {
                    //found!
                    return Optional.of(usedTeam);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @param uuid    the uuid
     * @param newTeam the new team
     * @return an optional with the oldTeam, is not absent if uuid was not teamed before
     */
    public Optional<Team> switchTeam(UUID uuid, @NotNull Team newTeam) {
        if (newTeam.hasCapacity()) {
            throw new IllegalStateException("uuid could not be inserted -> Team is full");
        }

        if (possibleTeams.contains(newTeam)) {
            Team oldTeam = teaming().get(uuid);
            if (oldTeam != null) {
                oldTeam.getMembers().remove(uuid);
                newTeam.getMembers().add(uuid);
                return Optional.of(oldTeam);

            } else {
                boolean b = addUUID(uuid, newTeam);
                assert b;
                return Optional.empty();
            }
        } else {
            throw new IllegalArgumentException("the given team is not allowed here!");
        }
    }

    /**
     * Every insertion should check on {@code if(containsUUID(uuid)) return false;}
     *
     * @param uuid the uuid to check
     * @return see {@link Map#containsKey(Object)} for further documentation
     */
    public boolean containsUUID(UUID uuid) {
        return teaming().containsKey(uuid);
    }

    public boolean addUUID(UUID uuid, Predicate<Team> teamPredicate) {
        AtomicBoolean r = new AtomicBoolean(true);
        possibleTeams.stream().filter(teamPredicate).findFirst().ifPresentOrElse(team -> addUUID(uuid, team), () -> r.set(false));
        return r.get();
    }

    protected BiConsumer<UUID, Team> getTeamJoinConsumer() {
        return teamJoinConsumer;
    }

    protected BiConsumer<UUID, Team> getTeamLeaveConsumer() {
        return teamLeaveConsumer;
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    public int maxTeamerCapacity() {
        int i = 0;
        for (Team possibleTeam : possibleTeams) {
            @Range(from = 0, to = 1_000_000) final int a = possibleTeam.maxCapacity();
            i += a;
        }
        return i;
    }

    public void resetUUIDs() {
        for (Team usedTeam : usedTeams()) {
            usedTeam.getMembers().clear();
        }
    }

    /**
     * Drop possibleTeams and wait for them to be "collected"
     */
    public void resetEverything() {
        resetUUIDs(); //just as failsafe... and to call the subscriptions
        possibleTeams.clear();
        //-> API.get().collectGarbage();
    }
}
