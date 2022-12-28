package net.juligames.core.minigame.api.team;

import de.bentzin.tools.collection.SubscribableSet;
import org.jetbrains.annotations.ApiStatus;
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
    @Range(from = 0,to = 1_000_000)
    private final Supplier<Integer> maxCapacity;
    private final SubscribableSet<UUID> members;

    public Team(TeamColor color, @Range(from = 0,to = 1_000_000) Supplier<Integer> maxCapacity,
                Set<UUID> initialMembers) {

        this.color = color;
        this.maxCapacity = maxCapacity;
        this.members = new SubscribableSet<>();
        members.addAll(initialMembers);
    }

    public Team(TeamColor color, @Range(from = 0,to = 1_000_000) Supplier<Integer> maxCapacity) {
        this(color,maxCapacity,new HashSet<>());
    }

    @Range(from = 0,to = 1_000_000)
    public int maxCapacity() {
        return Objects.requireNonNull(maxCapacity.get());
    }

    public TeamColor getColor() {
        return color;
    }

    public Supplier<Integer> getMaxCapacity() {
        return maxCapacity;
    }

    public SubscribableSet<UUID> getMembers() {
        return members;
    }

    public boolean tryAdd(UUID newMember) {
        if(hasCapacity()) {

            return getMembers().add(newMember);

        }else {
            return false;
        }
    }

    /**
     * Indicates that this Team is not full
     */
    public boolean hasCapacity() {
        return getMembers().size() < maxCapacity.get();
    }

    public boolean isFull() {
        return getMembers().size() == maxCapacity.get();
    }

    @ApiStatus.Internal
    public boolean isOverflown() {
        return getMembers().size() > maxCapacity.get();
    }

    public boolean isEmpty() {
        return getMembers().isEmpty();
    }
}
