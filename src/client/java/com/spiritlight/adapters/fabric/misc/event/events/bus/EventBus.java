package com.spiritlight.adapters.fabric.misc.event.events.bus;

import com.spiritlight.adapters.fabric.misc.event.events.Event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final List<IEventBusSubscriber> subscribers = new CopyOnWriteArrayList<>();

    public static final EventBus instance = new EventBus();

    private EventBus() {}

    public void subscribe(IEventBusSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(IEventBusSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void fire(Event event) {
        for(IEventBusSubscriber subscriber : subscribers) {
            subscriber.onEvent(event);
        }
    }
}
