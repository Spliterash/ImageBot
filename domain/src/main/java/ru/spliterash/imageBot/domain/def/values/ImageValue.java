package ru.spliterash.imageBot.domain.def.values;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spliterash.imageBot.domain.entities.DomainImage;

@Getter
@RequiredArgsConstructor
public class ImageValue implements CaseValue {
    private final DomainImage image;

    @Override
    public Type getType() {
        return Type.IMAGE;
    }
}
