package net.juligames.core.minigame.api.team;

import org.jetbrains.annotations.Range;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Ture Bentzin
 * 18.12.2022
 */
public final class Team {


    private final TeamColor color;
    private final Supplier<Integer> maxCapacity;
    private final Set<UUID> members;

    public Team(TeamColor color, @Range(from = 1, to = Integer.MAX_VALUE) Supplier<Integer> maxCapacity, Set<UUID> initialMembers) {
        this.color = color;
        this.maxCapacity = maxCapacity;
        this.members = initialMembers;
    }

    public Team(TeamColor color, @Range(from = 1, to = Integer.MAX_VALUE) Supplier<Integer> maxCapacity) {
        this(color,maxCapacity,new HashSet<>());
    }

    public int maxCapacity() {
        return Objects.requireNonNull(maxCapacity.get());
    }

    public TeamColor getColor() {
        return color;
    }

    public Supplier<Integer> getMaxCapacity() {
        return maxCapacity;
    }

    //TODO SubscribableList!!!! -> maybe SubscribableSet via subscription....
    public Set<UUID> getMembers() {
        return members;
    }

    public boolean tryAdd(UUID newMember) {
        if(members.size() < maxCapacity()) {

            return getMembers().add(newMember);

        }else {
            return false;
        }
    }
}
