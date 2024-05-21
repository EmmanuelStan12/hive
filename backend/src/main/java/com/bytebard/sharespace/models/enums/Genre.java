package com.bytebard.sharespace.models.enums;

import lombok.Getter;

@Getter
public enum Genre {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy"),
    MYSTERY("Mystery"),
    THRILLER("Thriller"),
    ROMANCE("Romance"),
    HISTORICAL_FICTION("Historical Fiction"),
    BIOGRAPHY("Biography"),
    AUTOBIOGRAPHY("Autobiography"),
    MEMOIR("Memoir"),
    SELF_HELP("Self-Help"),
    HISTORY("History"),
    SCIENCE("Science"),
    ART("Art"),
    POETRY("Poetry"),
    TRAVEL("Travel"),
    COOKING("Cooking"),
    YOUNG_ADULT("Young Adult"),
    CHILDREN("Children"),
    GRAPHIC_NOVEL("Graphic Novel"),
    PHILOSOPHY("Philosophy"),
    RELIGION("Religion"),
    OTHER("Other");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public static Genre fromString(String str) {
        for (Genre genre : Genre.values()) {
            if (genre.name.equalsIgnoreCase(str)) {
                return genre;
            }
        }
        return null;
    }

}
