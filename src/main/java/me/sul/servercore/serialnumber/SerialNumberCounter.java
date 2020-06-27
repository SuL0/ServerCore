package me.sul.servercore.serialnumber;

import me.sul.servercore.datasaveschedule.DataSaveScheduleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SerialNumberCounter implements Listener {
    private static SerialNumberCounter serialNumberCounter;
    private long count;

    public SerialNumberCounter() {
        serialNumberCounter = this;
        loadData();
    }
    public static SerialNumberCounter getInstance() { return serialNumberCounter; }

    public void loadData() {
        // count�ҷ�����
        count = (long)0;
    }
    @EventHandler
    public void onSave(DataSaveScheduleEvent event) {
        // count ����
    }

    public long getCountAndAddOne() {
        return count++;
    }

}
