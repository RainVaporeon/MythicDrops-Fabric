package com.spiritlight.mythicdrops.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spiritlight.adapters.serializers.JacksonSerializable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Database implements JacksonSerializable<Database> {

    @JsonProperty("whitelist")
    private final List<String> whitelistedItems = new ArrayList<>();

    @JsonIgnore
    private boolean active = true;

    @Unmodifiable
    public List<String> getWhitelistedItems() {
        return List.copyOf(whitelistedItems);
    }

    private boolean secret = false;

    public boolean addWhitelist(String in) {
        if(whitelistedItems.contains(in.toLowerCase(Locale.ROOT))) return false;
        return whitelistedItems.add(in.toLowerCase(Locale.ROOT));
    }

    public boolean isActive() {
        return active;
    }

    public boolean toggleActive() {
        this.active = !this.active;
        return this.active;
    }

    public boolean isSecret() {
        return secret;
    }

    public boolean toggleSecret() {
        this.secret = !this.secret;
        return this.secret;
    }

    public boolean removeWhitelist(String in) {
        return whitelistedItems.remove(in.toLowerCase(Locale.ROOT));
    }

    public boolean inWhitelist(String in) {
        return whitelistedItems.contains(in.toLowerCase(Locale.ROOT));
    }
}
