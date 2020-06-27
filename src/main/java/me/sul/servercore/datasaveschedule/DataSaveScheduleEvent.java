package me.sul.servercore.datasaveschedule;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DataSaveScheduleEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private boolean serverIsDisabling;
	
	public DataSaveScheduleEvent(boolean serverIsDisabling) {
		this.serverIsDisabling = serverIsDisabling;
	}
		
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public boolean isServerDisabling() {
		return serverIsDisabling;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() { return handlers; } // static!
}
