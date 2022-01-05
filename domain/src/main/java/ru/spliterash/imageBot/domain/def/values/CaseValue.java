package ru.spliterash.imageBot.domain.def.values;

public interface CaseValue {
    Type getType();

    enum Type {
        IMAGE, TEXT
    }
}
