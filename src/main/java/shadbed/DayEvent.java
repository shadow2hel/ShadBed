package shadbed;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public DayEvent(){
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }


}
